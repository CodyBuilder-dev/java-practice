package thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest {

  public static void main(String[] args) throws Exception {
      withDefaultExecutor();
      withCustomExecutor();
      withCallback();
      withException();
  }

  private static void withDefaultExecutor() throws InterruptedException, ExecutionException {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> System.out.println("Thread: " + Thread.currentThread().getName()));

    future.get();
    System.out.println("Thread: " + Thread.currentThread().getName());

    CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(3000L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return "Thread: " + Thread.currentThread().getName();
    });
    System.out.println(result.get());
  }

  private static void withCustomExecutor() throws InterruptedException, ExecutionException {
    ExecutorService executorService = Executors.newFixedThreadPool(2);


    // executorService.submit(callable)의 결과로 나오는 Future을 강제 캐스팅하여 CompletableFuture로 변경?
    // ClassCastException: class java.util.concurrent.FutureTask cannot be cast to class java.util.concurrent.CompletableFuture (java.util.concurrent.FutureTask and java.util.concurrent.CompletableFuture are in module java.base of loader 'bootstrap')
//    Callable<String> callable = () -> {
//      Thread.sleep(3000L);
//      return "Thread: " + Thread.currentThread().getName();
//    };

//    CompletableFuture<String> result =  (CompletableFuture<String>) executorService.submit(callable);
//    System.out.println(result.get());

    // CompletableFuture의 runAsync 메서드는 Runnable을 인자로 받고, ExecutorService를 인자로 받는 오버로드된 버전도 제공됩니다. ExecutorService를 제공하지 않으면 ForkJoinPool.commonPool()을 사용합니다.
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> System.out.println("runAsync Thread: " + Thread.currentThread().getName()), executorService);

    future.get();
    System.out.println("Thread: " + Thread.currentThread().getName());

    // CompletableFuture의 supplyAsync 메서드는 Supplier를 인자로 받고, ExecutorService를 인자로 받는 오버로드된 버전도 제공됩니다. ExecutorService를 제공하지 않으면 ForkJoinPool.commonPool()을 사용합니다.
    CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
      System.out.println("supplyAsync:");
      try {
        Thread.sleep(3000L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return "Thread: " + Thread.currentThread().getName();
    }, executorService);
    System.out.println(result.get());

    executorService.shutdown();
  }

  private static void withCallback() {
    CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> "first");
    CompletableFuture<String> second = first.thenApply(s -> {
      System.out.println("first.thenApply Thread: " + Thread.currentThread().getName()); // 콜백은 main스레드에서 실행되게 됨
      return s + " second";
    });
    second.thenAccept(System.out::println);

    CompletableFuture<String> third = CompletableFuture.supplyAsync(() -> "third");
    CompletableFuture<String> fourth = third.thenCompose(s -> CompletableFuture.supplyAsync(() -> {
      System.out.println("third.thenCompose Thread: " + Thread.currentThread().getName()); // fourth또한 CompletableFuture이므로, 별도의 스레드에서 실행되게 됨
      return s + " fourth";
    }));
    fourth.thenAccept(System.out::println);
  }

  private static void withException() throws ExecutionException, InterruptedException {
    CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
        throw new IllegalArgumentException("Invalid Argument");
    }).exceptionally(Throwable::getMessage);

    System.out.println(future.get());

    // CompletableFuture.<String>supplyAsync()로 명시해야 하는 이유:
    // 람다(공급자)가 실제로 아무 값도 반환하지 않고 예외만 던지기 때문에, 컴파일러가 supplyAsync의 제네릭 타입 파라미터 T를 추론하지 못합니다.

    // CompletableFuture<Object> 경우에는 좌측의 할당 대상(타입 힌트)인 CompletableFuture<Object>가 타입 추론에 도움을 줍니다.
    // 즉 컴파일러는 T = Object로 결정할 수 있고, method reference Throwable::getMessage(String 반환)는 Object로 넓혀질 수 있으므로 컴파일이 통과합니다.
    // 반면 CompletableFuture<String>에서는 컴파일러가 T를 String으로 추론해야 하는데(좌측이 CompletableFuture<String>이므로), 공급자 람다에 반환 값 정보가 없어 추론이 실패합니다.
    CompletableFuture<String> future2 = CompletableFuture.<String>supplyAsync(() -> {
      throw new RuntimeException("Exception occurred");
    }).exceptionally(e -> "Exception handled inside of future2: " + e.getMessage());

    System.out.println(future2.get());


    // exceptionally의 동작 시점
    // CompletableFuture의 선언부와 exceptionally를 분리하면, 제대로 에러를 잡지 못하는 경우가 있다.
    // CompletableFuture가 exceptionally 메서드를 호출하기 전에 완료되었거나, 이미 완료된 상태에서 exceptionally를 호출하면, 예외 처리가 제대로 동작하지 않을 수 있다.
    try {
      CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
        throw new RuntimeException("Exception occurred");
      });
      future3.exceptionally(e -> "Exception handled inside of future3: " + e.getMessage());

      System.out.println(future3.get());
    } catch (ExecutionException e) {
      System.out.println("Exception handled outside of future3: " + e.getMessage());
    }

    try {
      CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
        throw new RuntimeException("Exception occurred");
      });
      future4.exceptionally(e -> "Exception handled inside of future4: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Exception handled outside of future4: " + e.getMessage());
    }

    CompletableFuture<String> future5 = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      throw new RuntimeException("Exception occurred");
    });

    CompletableFuture<String> future6 = future5.exceptionally(e ->
        "Exception handled inside of future5: " + e.getMessage());

    // future5를 get하면 에러가 exceptionally에서 처리되지 못한다
    // System.out.println(future5.get());
    // future5 대신 future6을 get하면 정상적으로 동작한다.
    System.out.println(future6.get());

  }
}
