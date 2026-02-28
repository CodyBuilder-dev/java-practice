package thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS (Compare-And-Swap) 내부 동작 시뮬레이션
 * Atomic 클래스가 어떻게 충돌을 방지하는지 직접 확인
 */
public class AtomicCASDemo {

  private static AtomicInteger counter = new AtomicInteger(0);
  private static AtomicInteger successCount = new AtomicInteger(0);
  private static AtomicInteger retryCount = new AtomicInteger(0);

  /**
   * CAS 동작을 직접 구현하여 재시도 과정 확인
   */
  static class CASWorker implements Runnable {
    private final String threadName;

    public CASWorker(String threadName) {
      this.threadName = threadName;
    }

    @Override
    public void run() {
      for (int i = 0; i < 100; i++) {
        incrementWithCAS();
      }
    }

    private void incrementWithCAS() {
      while (true) {
        int current = counter.get();           // 1. 현재 값 읽기
        int next = current + 1;                // 2. 새 값 계산

        // 3. CAS 시도: "현재 값이 아직 current라면 next로 변경"
        if (counter.compareAndSet(current, next)) {
          // 성공: 다른 스레드가 값을 바꾸지 않았음
          successCount.incrementAndGet();
          break;
        } else {
          // 실패: 다른 스레드가 이미 값을 바꿨음 → 재시도
          retryCount.incrementAndGet();
          // while 루프로 다시 시도
        }
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    int threadCount = 10;

    System.out.println("=== CAS (Compare-And-Swap) 동작 원리 ===\n");

    Thread[] threads = new Thread[threadCount];
    long startTime = System.currentTimeMillis();

    // 10개 스레드가 동시에 CAS 수행
    for (int i = 0; i < threadCount; i++) {
      threads[i] = new Thread(new CASWorker("Thread-" + i));
      threads[i].start();
    }

    for (Thread thread : threads) {
      thread.join();
    }

    long endTime = System.currentTimeMillis();

    int expectedValue = threadCount * 100;
    int actualValue = counter.get();
    int totalAttempts = successCount.get();
    int totalRetries = retryCount.get();

    System.out.println("최종 카운터 값: " + actualValue);
    System.out.println("예상 값: " + expectedValue);
    System.out.println();
    System.out.println("총 CAS 성공: " + totalAttempts + "번");
    System.out.println("총 CAS 재시도: " + totalRetries + "번");
    System.out.println("재시도율: " + String.format("%.1f%%",
        (double)totalRetries / totalAttempts * 100));
    System.out.println("실행 시간: " + (endTime - startTime) + "ms");

    System.out.println("\n=== CAS 동작 과정 ===");
    System.out.println("1. 현재 값(current) 읽기");
    System.out.println("2. 새 값(next = current + 1) 계산");
    System.out.println("3. compareAndSet(current, next) 시도:");
    System.out.println("   - 성공: 메모리 값이 아직 current → next로 변경");
    System.out.println("   - 실패: 다른 스레드가 이미 변경함 → 1번부터 재시도");
    System.out.println();
    System.out.println("✅ " + totalRetries + "번의 충돌이 있었지만 CAS로 모두 해결!");
    System.out.println("Lock 없이도 재시도 메커니즘으로 안전한 동시성 제어");
  }
}
