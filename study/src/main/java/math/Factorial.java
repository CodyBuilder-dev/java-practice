package math;

public class Factorial {
  static long[] dpCache = new long[100];

  public static void main(String[] args) {
    // 동작 테스트
    System.out.println("=== 동작 확인 ===");
    int testN = 10;
    System.out.println("10! = " + factorialFor(testN));
    System.out.println("재귀: " + factorialRecursive(testN));
    System.out.println("DP: " + factorialWithDPWithoutCaching(testN));
    System.out.println("DP(Cache): " + factorialWithDPWithCaching(testN));

    // 성능 비교
    System.out.println("\n=== 성능 비교 (30! 계산 100,000회) ===");
    int n = 30;
    int iterations = 100_000;

    long start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      factorialFor(n);
    }
    long forTime = System.nanoTime() - start;

    start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      factorialRecursive(n);
    }
    long recursiveTime = System.nanoTime() - start;

    start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      factorialWithDPWithoutCaching(n);
    }
    long dpTime = System.nanoTime() - start;

    start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      factorialWithDPWithCaching(n);
    }
    long dpCacheTime = System.nanoTime() - start;

    System.out.printf("For문:         %.2f ms\n", forTime / 1_000_000.0);
    System.out.printf("재귀:           %.2f ms\n", recursiveTime / 1_000_000.0);
    System.out.printf("DP:            %.2f ms\n", dpTime / 1_000_000.0);
    System.out.printf("DP(Cache):     %.2f ms\n", dpCacheTime / 1_000_000.0);
  }

  // for문을 이용한 구현
  public static long factorialFor(int n) {
    long result = 1;
    for (int i = 2; i <= n; i++) {
      result *= i;
    }
    return result;
  }

  // 재귀를 이용한 구현
  public static long factorialRecursive(int n) {
    return n <= 1 ? 1 : n * factorialRecursive(n - 1);
  }

  // DP를 이용한 구현 (캐싱 없음)
  public static long factorialWithDPWithoutCaching(int n) {
    long[] dp = new long[n + 1];
    dp[0] = 1;
    for (int i = 1; i <= n; i++) {
      dp[i] = dp[i - 1] * i;
    }
    return dp[n];
  }

  // DP를 이용한 구현 (캐싱 사용)
  public static long factorialWithDPWithCaching(int n) {
    if (n < 0 || n >= dpCache.length) {
      throw new IllegalArgumentException("n must be non-negative and less than " + dpCache.length);
    }

    if (dpCache[n] != 0) {
      return dpCache[n];
    }

    dpCache[0] = 1;
    for (int i = 1; i <= n; i++) {
      dpCache[i] = dpCache[i - 1] * i;
    }
    return dpCache[n];
  }
}
