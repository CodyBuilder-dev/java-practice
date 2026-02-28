package thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicInteger vs synchronized 성능 비교
 * CAS(lock-free) vs 락(lock-based) 비교
 */
public class AtomicVsLockComparison {

  // 1. AtomicInteger 사용
  static class AtomicCounter {
    private AtomicInteger counter = new AtomicInteger(0);

    public void increment() {
      counter.incrementAndGet();
    }

    public int get() {
      return counter.get();
    }
  }

  // 2. synchronized 사용
  static class SynchronizedCounter {
    private int counter = 0;

    public synchronized void increment() {
      counter++;
    }

    public synchronized int get() {
      return counter;
    }
  }

  // 3. volatile 사용 (잘못된 방법 - Race Condition 발생)
  static class VolatileCounter {
    private volatile int counter = 0;

    public void increment() {
      counter++;  // volatile이어도 복합 연산은 안전하지 않음!
    }

    public int get() {
      return counter;
    }
  }

  public static void main(String[] args) throws InterruptedException {
    int threadCount = 10;
    int incrementsPerThread = 100_000;
    int expected = threadCount * incrementsPerThread;

    System.out.println("=== 동시성 제어 방법 비교 ===");
    System.out.println("스레드 수: " + threadCount);
    System.out.println("스레드당 증가 횟수: " + incrementsPerThread);
    System.out.println("예상 최종 값: " + expected);
    System.out.println();

    // 1. AtomicInteger 테스트
    AtomicCounter atomicCounter = new AtomicCounter();
    long atomicTime = runTest("AtomicInteger", threadCount, incrementsPerThread,
        atomicCounter::increment, atomicCounter::get, expected);

    // 2. synchronized 테스트
    SynchronizedCounter syncCounter = new SynchronizedCounter();
    long syncTime = runTest("synchronized ", threadCount, incrementsPerThread,
        syncCounter::increment, syncCounter::get, expected);

    // 3. volatile 테스트 (문제 있는 방법)
    VolatileCounter volatileCounter = new VolatileCounter();
    long volatileTime = runTest("volatile (잘못된 사용)", threadCount, incrementsPerThread,
        volatileCounter::increment, volatileCounter::get, expected);

    // 성능 비교
    System.out.println("\n=== 성능 비교 ===");
    System.out.println("AtomicInteger: " + atomicTime + "ms (기준)");
    System.out.println("synchronized:  " + syncTime + "ms (" +
        String.format("%.1fx", (double)syncTime/atomicTime) + " 배)");
    System.out.println("volatile:      " + volatileTime + "ms (결과 부정확)");

    System.out.println("\n=== 결론 ===");
    System.out.println("✅ AtomicInteger: 빠르고 안전 (lock-free CAS)");
    System.out.println("✅ synchronized:  안전하지만 느림 (lock contention)");
    System.out.println("❌ volatile:      가시성만 보장, 복합 연산은 불안전");
  }

  private static long runTest(String name, int threadCount, int incrementsPerThread,
                               Runnable incrementOp, java.util.function.Supplier<Integer> getOp,
                               int expected) throws InterruptedException {
    Thread[] threads = new Thread[threadCount];
    long startTime = System.currentTimeMillis();

    for (int i = 0; i < threadCount; i++) {
      threads[i] = new Thread(() -> {
        for (int j = 0; j < incrementsPerThread; j++) {
          incrementOp.run();
        }
      });
      threads[i].start();
    }

    for (Thread thread : threads) {
      thread.join();
    }

    long endTime = System.currentTimeMillis();
    long elapsed = endTime - startTime;
    int actual = getOp.get();

    String status = (actual == expected) ? "✅" : "❌";
    System.out.println(status + " " + name + ": " + actual + " / " + expected +
        " (" + elapsed + "ms)");

    return elapsed;
  }
}
