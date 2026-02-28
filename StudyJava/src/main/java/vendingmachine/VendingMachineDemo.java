package vendingmachine;

import java.util.*;

public class VendingMachineDemo {

  // ===== Money =====
  static final class Money {
    private final int amount;
    private Money(int amount) {
      if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
      this.amount = amount;
    }
    public static Money of(int amount) { return new Money(amount); }
    public int value() { return amount; }
    public Money plus(Money other) { return Money.of(this.amount + other.amount); }
    @Override public String toString() { return amount + "원"; }
  }

  // ===== Product =====
  static final class Product {
    private final String id;
    private final String name;
    private final Money price;

    public Product(String id, String name, Money price) {
      this.id = Objects.requireNonNull(id);
      this.name = Objects.requireNonNull(name);
      this.price = Objects.requireNonNull(price);
    }
    public String id() { return id; }
    public String name() { return name; }
    public Money price() { return price; }

    @Override public String toString() {
      return name + "(" + id + ", " + price + ")";
    }
  }

  // ===== Inventory =====
  static final class Inventory {
    private final Map<String, Product> products = new HashMap<>();
    private final Map<String, Integer> stock = new HashMap<>();

    public Product getMostCheapProduct() {
      return products.values().stream()
          .min(Comparator.comparing(product -> product.price().value()))
          .orElseThrow(() -> new IllegalStateException("No products in inventory"));
    }

    public void addProduct(Product p, int qty) {
      products.put(p.id(), p);
      stock.put(p.id(), stock.getOrDefault(p.id(), 0) + qty);
    }

    public Product getProduct(String productId) {
      Product p = products.get(productId);
      if (p == null) throw new IllegalArgumentException("Unknown productId: " + productId);
      return p;
    }

    public int getStock(String productId) {
      return stock.getOrDefault(productId, 0);
    }

    public boolean isAvailable(String productId) {
      return getStock(productId) > 0;
    }

    public void decrement(String productId) {
      int cur = getStock(productId);
      if (cur <= 0) throw new IllegalStateException("Sold out: " + productId);
      stock.put(productId, cur - 1);
    }

    public List<Product> listProductsSorted() {
      List<Product> list = new ArrayList<>(products.values());
      list.sort(Comparator.comparing(Product::id));
      return list;
    }
  }

  // ===== ChangeStore =====
  static final class ChangeStore {
    private final NavigableMap<Integer, Integer> coins = new TreeMap<>(Comparator.reverseOrder());

    public ChangeStore(int... denominationsDesc) {
      for (int d : denominationsDesc) coins.put(d, 0);
    }

    public int totalCoins() {
      int sum = 0;
      for(var e : coins.entrySet()) {
        int denom = e.getKey();
        int count = e.getValue();
        sum += denom * count;
      }
      return sum;
    }

    public void addCoins(int denom, int count) {
      coins.put(denom, coins.getOrDefault(denom, 0) + count);
    }

    public Map<Integer, Integer> snapshot() {
      return new TreeMap<>(coins);
    }

    public Optional<Map<Integer, Integer>> tryDispenseChange(int changeAmount) {
      if (changeAmount < 0) throw new IllegalArgumentException("changeAmount must be >= 0");
      if (changeAmount == 0) return Optional.of(Collections.emptyMap());

      int remaining = changeAmount;
      Map<Integer, Integer> temp = new HashMap<>();

      for (var e : coins.entrySet()) {
        int denom = e.getKey();
        int available = e.getValue();
        int need = remaining / denom;
        if (need <= 0) continue;

        int take = Math.min(need, available);
        if (take > 0) {
          temp.put(denom, take);
          remaining -= denom * take;
        }
      }

      if (remaining != 0) return Optional.empty();

      // commit
      Map<Integer, Integer> used = new LinkedHashMap<>();
      for (var entry : temp.entrySet()) {
        int denom = entry.getKey();
        int take = entry.getValue();
        coins.put(denom, coins.get(denom) - take);
        used.put(denom, take);
      }
      return Optional.of(used);
    }
  }

  // ===== Result =====
  static final class ActionResult {
    final boolean success;
    final String message;
    final Product dispensed;
    final Money refunded;
    final Map<Integer, Integer> change;

    private ActionResult(boolean success, String message, Product dispensed, Money refunded, Map<Integer, Integer> change) {
      this.success = success;
      this.message = message;
      this.dispensed = dispensed;
      this.refunded = refunded;
      this.change = change;
    }

    static ActionResult ok(String message) {
      return new ActionResult(true, message, null, Money.of(0), Collections.emptyMap());
    }

    static ActionResult fail(String message) {
      return new ActionResult(false, message, null, Money.of(0), Collections.emptyMap());
    }

    static ActionResult dispensed(Product p, Map<Integer, Integer> change) {
      return new ActionResult(true, "배출: " + p.name(), p, Money.of(0), change);
    }

    static ActionResult refunded(Money amount, Map<Integer, Integer> changeLike) {
      return new ActionResult(true, "반환: " + amount, null, amount, changeLike);
    }

    @Override public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(success ? "[OK] " : "[FAIL] ").append(message);
      if (dispensed != null) sb.append(" | item=").append(dispensed);
      if (refunded.value() > 0) sb.append(" | refunded=").append(refunded);
      if (change != null && !change.isEmpty()) sb.append(" | change=").append(change);
      return sb.toString();
    }
  }

  // ===== Payment Session =====
  static final class PaymentSession {
    private Money credit = Money.of(0);
    private Product selected = null;

    public Money credit() { return credit; }
    public Product selected() { return selected; }

    public void addCredit(Money m) { credit = credit.plus(m); }
    public void select(Product p) { selected = p; }
    public void clearSelection() { selected = null; }
    public void reset() { credit = Money.of(0); selected = null; }
  }

  // ===== State Pattern =====
  interface State {
    ActionResult insertMoney(VendingMachine vm, int amount);
    ActionResult selectProduct(VendingMachine vm, String productId);
    ActionResult purchase(VendingMachine vm);
    ActionResult cancel(VendingMachine vm);
    String name();
  }

  static final class IdleState implements State {
    @Override public ActionResult insertMoney(VendingMachine vm, int amount) {
      if (amount <= 0) return ActionResult.fail("투입 금액은 0보다 커야 함");
      int maximumRefund = vm.changeStore.totalCoins() - vm.inventory.getMostCheapProduct().price().value();
      if (vm.session.credit.value() + amount > maximumRefund) return ActionResult.fail("잔돈 부족으로 인해 큰 금액은 투입할 수 없음. 현재 남은잔돈=" + maximumRefund);
      vm.session.addCredit(Money.of(amount));
      vm.setState(new HasCreditState());
      return ActionResult.ok("투입됨: " + amount + "원, 현재=" + vm.session.credit());
    }
    @Override public ActionResult selectProduct(VendingMachine vm, String productId) {
      return ActionResult.fail("먼저 돈을 투입하세요");
    }
    @Override public ActionResult purchase(VendingMachine vm) {
      return ActionResult.fail("상품을 먼저 선택하세요");
    }
    @Override public ActionResult cancel(VendingMachine vm) {
      return ActionResult.ok("취소할 내역 없음");
    }
    @Override public String name() { return "IDLE"; }
  }

  static final class HasCreditState implements State {
    @Override public ActionResult insertMoney(VendingMachine vm, int amount) {
      if (amount <= 0) return ActionResult.fail("투입 금액은 0보다 커야 함");
      int maximumRefund = vm.changeStore.totalCoins() - vm.inventory.getMostCheapProduct().price().value();
      if (vm.session.credit.value() + amount > maximumRefund)  return ActionResult.fail("잔돈 부족으로 인해 큰 금액은 투입할 수 없음. 현재 남은잔돈=" + maximumRefund);
      vm.session.addCredit(Money.of(amount));
      return ActionResult.ok("추가 투입: " + amount + "원, 현재=" + vm.session.credit());
    }
    @Override public ActionResult selectProduct(VendingMachine vm, String productId) {
      Product p;
      try { p = vm.inventory.getProduct(productId); }
      catch (IllegalArgumentException e) { return ActionResult.fail("없는 상품 ID: " + productId); }

      if (!vm.inventory.isAvailable(productId)) return ActionResult.fail("품절: " + p.name());
      vm.session.select(p);
      vm.setState(new SelectedState());
      return ActionResult.ok("선택됨: " + p.name() + ", 가격=" + p.price() + ", 현재=" + vm.session.credit());
    }
    @Override public ActionResult purchase(VendingMachine vm) {
      return ActionResult.fail("상품을 먼저 선택하세요");
    }
    @Override public ActionResult cancel(VendingMachine vm) {
      int refund = vm.session.credit().value();
      Optional<Map<Integer, Integer>> changeOpt = Optional.of(new HashMap<>());

      vm.session.reset();
      vm.setState(new IdleState());
      return ActionResult.refunded(Money.of(refund), changeOpt.get());
    }
    @Override public String name() { return "HAS_CREDIT"; }
  }


  static final class SelectedState implements State {
    @Override public ActionResult insertMoney(VendingMachine vm, int amount) {
      if (amount <= 0) return ActionResult.fail("투입 금액은 0보다 커야 함");
      vm.session.addCredit(Money.of(amount));
      return ActionResult.ok("추가 투입: " + amount + "원, 현재=" + vm.session.credit());
    }
    @Override public ActionResult selectProduct(VendingMachine vm, String productId) {
      Product p;
      try { p = vm.inventory.getProduct(productId); }
      catch (IllegalArgumentException e) { return ActionResult.fail("없는 상품 ID: " + productId); }

      if (!vm.inventory.isAvailable(productId)) return ActionResult.fail("품절: " + p.name());
      vm.session.select(p);
      return ActionResult.ok("선택 변경: " + p.name() + ", 가격=" + p.price());
    }
    @Override public ActionResult purchase(VendingMachine vm) {
      Product selected = vm.session.selected();
      if (selected == null) {
        vm.setState(new HasCreditState());
        return ActionResult.fail("선택된 상품이 없음");
      }
      if (!vm.inventory.isAvailable(selected.id())) {
        vm.session.clearSelection();
        vm.setState(new HasCreditState());
        return ActionResult.fail("품절: " + selected.name());
      }

      int credit = vm.session.credit().value();
      int price = selected.price().value();
      if (credit < price) return ActionResult.fail("금액 부족: 필요=" + price + "원, 현재=" + credit + "원");

      int change = credit - price;

      Optional<Map<Integer, Integer>> changeOpt = vm.changeStore.tryDispenseChange(change);
      if (changeOpt.isEmpty()) return ActionResult.fail("잔돈 부족으로 구매 불가: change=" + change + "원");

      vm.inventory.decrement(selected.id());

      vm.session.reset();
      vm.setState(new IdleState());

      return ActionResult.dispensed(selected, changeOpt.get());
    }
    @Override public ActionResult cancel(VendingMachine vm) {
      int refund = vm.session.credit().value();
      Optional<Map<Integer, Integer>> changeOpt = Optional.of(new HashMap<>());

      vm.session.reset();
      vm.setState(new IdleState());
      return ActionResult.refunded(Money.of(refund), changeOpt.get());
    }
    @Override public String name() { return "SELECTED"; }
  }

  // ===== VendingMachine =====
  static final class VendingMachine {
    private State state = new IdleState();

    private final Inventory inventory = new Inventory();
    private final ChangeStore changeStore;
    private final PaymentSession session = new PaymentSession();

    public VendingMachine(ChangeStore changeStore) {
      this.changeStore = changeStore;
    }

    void setState(State newState) { this.state = newState; }
    public String stateName() { return state.name(); }

    public ActionResult insertMoney(int amount) { return state.insertMoney(this, amount); }
    public ActionResult selectProduct(String productId) { return state.selectProduct(this, productId); }
    public ActionResult purchase() { return state.purchase(this); }
    public ActionResult cancel() { return state.cancel(this); }

    public Money credit() { return session.credit(); }
    public Product selected() { return session.selected(); }

    public void stock(Product p, int qty) { inventory.addProduct(p, qty); }
    public List<Product> products() { return inventory.listProductsSorted(); }
    public int stockOf(String productId) { return inventory.getStock(productId); }
    public Map<Integer, Integer> changeSnapshot() { return changeStore.snapshot(); }
  }

  // ===== Console UI =====
  private static void printHelp() {
    System.out.println("""
        명령어:
          help                    도움말
          list                    상품 목록 보기
          status                  현재 상태(투입금/선택상품/상태)
          insert <금액>           돈 투입 (예: insert 1000)
          select <상품ID>          상품 선택 (예: select COKE)
          buy                     구매(배출)
          cancel                  취소(환불)
          coins                   잔돈 재고 보기(디버그)
          exit                    종료
        """);
  }

  private static void printProducts(VendingMachine vm) {
    System.out.println("== 상품 목록 ==");
    for (Product p : vm.products()) {
      System.out.printf("- %-6s | %-6s | price=%s | stock=%d%n",
          p.id(), p.name(), p.price(), vm.stockOf(p.id()));
    }
  }

  private static void printStatus(VendingMachine vm) {
    System.out.println("== 상태 ==");
    System.out.println("state   : " + vm.stateName());
    System.out.println("credit  : " + vm.credit());
    System.out.println("selected: " + (vm.selected() == null ? "-" : vm.selected()));
  }

  public static void main(String[] args) {
    // ===== Setup Machine =====
    ChangeStore cs = new ChangeStore(1000, 500, 100, 50, 10);
    cs.addCoins(1000, 10);
    cs.addCoins(500, 10);
    cs.addCoins(100, 30);
    cs.addCoins(50, 20);
    cs.addCoins(10, 50);

    VendingMachine vm = new VendingMachine(cs);

    Product coke = new Product("COKE", "콜라", Money.of(1200));
    Product water = new Product("WATER", "물", Money.of(700));
    Product coffee = new Product("COFFEE", "커피", Money.of(900));

    vm.stock(coke, 5);
    vm.stock(water, 3);
    vm.stock(coffee, 2);

    // ===== Console Loop =====
    System.out.println("콘솔 자판기 시작!");
    printHelp();
    printProducts(vm);
    printStatus(vm);

    Scanner sc = new Scanner(System.in);

    while (true) {
      System.out.print("\n> ");
      String line = sc.nextLine().trim();
      if (line.isEmpty()) continue;

      String[] parts = line.split("\\s+");
      String cmd = parts[0].toLowerCase(Locale.ROOT);

      try {
        switch (cmd) {
          case "help" -> printHelp();
          case "list" -> printProducts(vm);
          case "status" -> printStatus(vm);
          case "insert" -> {
            if (parts.length < 2) {
              System.out.println("[FAIL] 사용법: insert <금액>");
              break;
            }
            int amount = Integer.parseInt(parts[1]);
            System.out.println(vm.insertMoney(amount));
          }
          case "select" -> {
            if (parts.length < 2) {
              System.out.println("[FAIL] 사용법: select <상품ID>");
              break;
            }
            String id = parts[1].toUpperCase(Locale.ROOT);
            System.out.println(vm.selectProduct(id));
          }
          case "buy" -> System.out.println(vm.purchase());
          case "cancel" -> System.out.println(vm.cancel());
          case "coins" -> System.out.println("잔돈 재고: " + vm.changeSnapshot());
          case "exit", "quit" -> {
            System.out.println("종료합니다.");
            return;
          }
          default -> System.out.println("[FAIL] 알 수 없는 명령어. help를 입력하세요.");
        }
      } catch (NumberFormatException e) {
        System.out.println("[FAIL] 숫자 형식이 올바르지 않음");
      } catch (Exception e) {
        System.out.println("[FAIL] 에러: " + e.getMessage());
      }
    }
  }
}
