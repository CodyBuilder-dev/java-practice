package thread;

/**
 * Race Condition 발생 예제
 * 여러 스레드가 동시에 일반 int를 증가시킬 때 충돌 발생
 */
public class RaceConditionDemo {

  private int unsafeCounter = 0;

  public void increment() {
    // 3단계 연산: 읽기 → 증가 → 쓰기
    // 이 사이에 다른 스레드가 끼어들 수 있음!
    unsafeCounter++;
  }

  public int getCounter() {
    return unsafeCounter;
  }

  public static void main(String[] args) throws InterruptedException {
    RaceConditionDemo demo = new RaceConditionDemo();
    int threadCount = 10;
    int incrementsPerThread = 1000;

    Thread[] threads = new Thread[threadCount];

    // 10개 스레드가 각각 1000번씩 증가
    for (int i = 0; i < threadCount; i++) {
      threads[i] = new Thread(() -> {
        for (int j = 0; j < incrementsPerThread; j++) {
          demo.increment();
        }
      });
      threads[i].start();
    }

    // 모든 스레드 완료 대기
    for (Thread thread : threads) {
      thread.join();
    }

    int expected = threadCount * incrementsPerThread;
    int actual = demo.getCounter();

    System.out.println("=== Race Condition 발생 ===");
    System.out.println("예상 값: " + expected);
    System.out.println("실제 값: " + actual);
    System.out.println("손실된 증가: " + (expected - actual));

    if (actual < expected) {
      System.out.println("\n❌ Race Condition으로 인해 값이 손실되었습니다!");
      System.out.println("여러 스레드가 동시에 읽기-증가-쓰기를 하면서 충돌 발생");
    } else {
      System.out.println("\n⚠️  이번엔 운 좋게 충돌이 없었습니다 (다시 실행해보세요)");
    }
  }
}
