package thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicInteger로 Race Condition 해결
 * CAS (Compare-And-Swap) 알고리즘으로 안전한 증가 연산
 */
public class AtomicCounterDemo {

  private AtomicInteger safeCounter = new AtomicInteger(0);

  public void increment() {
    // 원자적 연산: CAS로 충돌 없이 증가
    safeCounter.incrementAndGet();
  }

  public int getCounter() {
    return safeCounter.get();
  }

  public static void main(String[] args) throws InterruptedException {
    AtomicCounterDemo demo = new AtomicCounterDemo();
    int threadCount = 10;
    int incrementsPerThread = 1000;

    Thread[] threads = new Thread[threadCount];

    long startTime = System.nanoTime();

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

    long endTime = System.nanoTime();
    double elapsedMs = (endTime - startTime) / 1_000_000.0;

    int expected = threadCount * incrementsPerThread;
    int actual = demo.getCounter();

    System.out.println("=== AtomicInteger로 안전한 증가 ===");
    System.out.println("예상 값: " + expected);
    System.out.println("실제 값: " + actual);
    System.out.println("손실된 증가: " + (expected - actual));
    System.out.println("실행 시간: " + String.format("%.2f", elapsedMs) + "ms");

    if (actual == expected) {
      System.out.println("\n✅ CAS 알고리즘으로 모든 증가가 안전하게 적용되었습니다!");
      System.out.println("Lock 없이도 원자적 연산으로 동시성 문제 해결");
    } else {
      System.out.println("\n❌ 예상치 못한 오류 발생");
    }
  }
}
