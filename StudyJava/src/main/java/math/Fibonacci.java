package math;

import java.util.Arrays;

public class Fibonacci {
  static int[] dpCache = new int[1000];

  public static void main(String[] args) {
    // 동작 테스트
    System.out.println("=== 동작 확인 ===");
    int testN = 10;
    System.out.println("fibonacci(10) = " + fibonacciFor(testN));
    System.out.println("재귀: " + fibonacciRecursive(testN));
    System.out.println("DP(배열): " + fibonacciDPWithoutCache(testN));
    System.out.println("DP(캐시): " + fibonacciDPWithCache(testN));

    // 성능 비교 (재귀는 너무 느려서 n=30으로 제한)
    System.out.println("\n=== 성능 비교 (fibonacci(30) 계산 10,000회) ===");
    int n = 30;
    int iterations = 10_000;

    long start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      fibonacciFor(n);
    }
    long forTime = System.nanoTime() - start;

    start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      fibonacciRecursive(n);
    }
    long recursiveTime = System.nanoTime() - start;

    start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      fibonacciDPWithoutCache(n);
    }
    long dpTime = System.nanoTime() - start;

    // 캐시 초기화 후 측정
    Arrays.fill(dpCache, 0);
    start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      fibonacciDPWithCache(n);
    }
    long dpCacheTime = System.nanoTime() - start;

    System.out.printf("For문:      %.2f ms\n", forTime / 1_000_000.0);
    System.out.printf("재귀:       %.2f ms\n", recursiveTime / 1_000_000.0);
    System.out.printf("DP(배열):   %.2f ms\n", dpTime / 1_000_000.0);
    System.out.printf("DP(캐시):   %.2f ms\n", dpCacheTime / 1_000_000.0);
  }

  // for문
  public static int fibonacciFor(int n ) {
    if (n <= 1) {
      return n;
    }
    int a = 0, b = 1;
    for (int i = 1; i < n; i++) {
      int c = a + b;
      a = b;
      b = c;
    }
    return b;
  }

  // 재귀

  /**
   * 피보나치 재귀가 느린 이유는 **엄청난 중복 계산** 때문입니다.
   * 예를 들어 `fibonacci(5)`를 계산하면:```
   *                     fib(5)
   *                    /      \
   *               fib(4)      fib(3)
   *              /     \      /     \
   *         fib(3)   fib(2) fib(2) fib(1)
   *         /   \    /   \   /   \
   *     fib(2) fib(1) ...
   * ```
   * - `fib(3)`이 **2번** 계산됨
   * - `fib(2)`가 **3번** 계산됨
   * - `fib(1)`이 **5번** 계산됨
   *
   * `fibonacci(30)`의 경우:
   * - **총 함수 호출 횟수**: 약 2,692,537번
   * - **실제 필요한 계산**: 30번
   *
   * **시간 복잡도**:
   * - 재귀: **O(2^n)** - 지수적 증가
   * - For문/DP: **O(n)** - 선형
   *
   * `fibonacci(40)`만 되어도 재귀는 몇 초가 걸리지만, For문은 밀리초도 안 걸립니다.
   * **팩토리얼과의 차이**:
   * - 팩토리얼 재귀: `n * factorial(n-1)` - 중복 없음, O(n)
   * - 피보나치 재귀: `fib(n-1) + fib(n-2)` - 중복 폭발, O(2^n)
   * @param n 원소 인덱스
   * @return 피보나치 값
   */
  public static int fibonacciRecursive(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("n must be non-negative");
    }

    if (n <= 1) {
      return n;
    }

    return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
  }

  // DP
  public static int fibonacciDPWithoutCache(int n) {
    int[] dp = new int[n + 1];
    dp[0] = 0;
    dp[1] = 1;

    for (int i = 2; i <= n; i++) {
      dp[i] = dp[i - 1] + dp[i - 2];
    }

    return dp[n];
  }

  // DP(Cache)
  public static int fibonacciDPWithCache(int n) {
    if (n < 0 || n >= dpCache.length) {
      throw new IllegalArgumentException("n must be non-negative and less than " + dpCache.length);
    }

    if (n <= 1) {
      return n;
    }
    if (dpCache[n] != 0) {
      return dpCache[n];
    }
    dpCache[0] = 0;
    dpCache[1] = 1;
    for(int i = 2; i <= n; i++) {
      dpCache[i] = dpCache[i - 1] + dpCache[i - 2];
    }
    return dpCache[n];
  }
}
