package thread;

public class VolatileCounter {
  public int count = 0;

  public static void main(String[] args) throws Exception {
    VolatileCounter counter = new VolatileCounter();

    Thread reader = new Thread(() -> {
      // IO/sleep 없이 순수 스핀
      while (counter.count < 1000) {
        // 의도적으로 비움
      }
      System.out.println("Reader saw: " + counter.count);
    });

    Thread writer = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        counter.count++;
      }
      System.out.println("Writer done: " + counter.count);
    });

    reader.start();
    Thread.sleep(50);  // Reader가 먼저 대기 상태에 들어가도록
    writer.start();

    writer.join();
    reader.join(10000);

    if (reader.isAlive()) {
      System.out.println("\n=== volatile 없이는 Reader가 멈추지 않습니다! ===");
      System.out.println("Reader가 running=false를 보지 못해 무한 루프에 빠졌습니다.");
      System.exit(0);
    } else {
      System.out.println("\n=== volatile이 있어 Reader가 정상적으로 종료되었습니다! ===");
      System.out.println("Reader가 running=false를 확인하고 종료했습니다.");
    }
  }
}

