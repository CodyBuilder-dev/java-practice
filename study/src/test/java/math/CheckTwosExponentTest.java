package math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckTwosExponentTest {

  @Test
  void testWithZero() {
    System.out.println("\n=== 테스트: 0 (2의 제곱수 아님) ===");
    assertTrue(CheckTwosExponent.isPowerOfTwo(0), "isPowerOfTwo(0) - 실패!");
    assertFalse(CheckTwosExponent.isPowerOfTwo2(0), "isPowerOfTwo2(0)");
    assertFalse(CheckTwosExponent.isPowerOfTwo3(0), "isPowerOfTwo3(0)");
    assertFalse(CheckTwosExponent.isPowerOfTwo4(0), "isPowerOfTwo4(0)");
    assertFalse(CheckTwosExponent.isPowerOfTwo6(0), "isPowerOfTwo6(0)");
    assertFalse(CheckTwosExponent.isPowerOfTwo7(0), "isPowerOfTwo7(0)");
    assertFalse(CheckTwosExponent.isPowerOfTwo8(0), "isPowerOfTwo8(0)");
    assertFalse(CheckTwosExponent.isPowerOfTwo9(0), "isPowerOfTwo9(0)");
    assertFalse(CheckTwosExponent.isPowerOfTwo10(0), "isPowerOfTwo10(0)");
  }

  @Test
  void testWithOne() {
    System.out.println("\n=== 테스트: 1 (2^0 = 1) ===");
    assertTrue(CheckTwosExponent.isPowerOfTwo(1), "isPowerOfTwo(1)");
    assertTrue(CheckTwosExponent.isPowerOfTwo2(1), "isPowerOfTwo2(1)");
    assertTrue(CheckTwosExponent.isPowerOfTwo3(1), "isPowerOfTwo3(1)");
    assertTrue(CheckTwosExponent.isPowerOfTwo4(1), "isPowerOfTwo4(1)");
    assertFalse(CheckTwosExponent.isPowerOfTwo6(1), "isPowerOfTwo6(1)");
    assertTrue(CheckTwosExponent.isPowerOfTwo7(1), "isPowerOfTwo7(1)");
    assertTrue(CheckTwosExponent.isPowerOfTwo8(1), "isPowerOfTwo8(1)");
    assertTrue(CheckTwosExponent.isPowerOfTwo9(1), "isPowerOfTwo9(1)");
    assertTrue(CheckTwosExponent.isPowerOfTwo10(1), "isPowerOfTwo10(1)");
  }

  @Test
  void testWithPowersOfTwo() {
    System.out.println("\n=== 테스트: 2의 제곱수들 ===");
    int[] powersOfTwo = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};
    
    for (int num : powersOfTwo) {
      assertTrue(CheckTwosExponent.isPowerOfTwo(num), "isPowerOfTwo(" + num + ")");
      assertTrue(CheckTwosExponent.isPowerOfTwo2(num), "isPowerOfTwo2(" + num + ")");
      assertTrue(CheckTwosExponent.isPowerOfTwo3(num), "isPowerOfTwo3(" + num + ")");
      assertTrue(CheckTwosExponent.isPowerOfTwo4(num), "isPowerOfTwo4(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo6(num), "isPowerOfTwo6(" + num + ")");
      assertTrue(CheckTwosExponent.isPowerOfTwo7(num), "isPowerOfTwo7(" + num + ")");
      assertTrue(CheckTwosExponent.isPowerOfTwo8(num), "isPowerOfTwo8(" + num + ")");
      assertTrue(CheckTwosExponent.isPowerOfTwo9(num), "isPowerOfTwo9(" + num + ")");
      assertTrue(CheckTwosExponent.isPowerOfTwo10(num), "isPowerOfTwo10(" + num + ")");
    }
  }

  @Test
  void testWithNonPowersOfTwo() {
    System.out.println("\n=== 테스트: 2의 제곱수가 아닌 수들 ===");
    int[] nonPowers = {3, 5, 6, 7, 9, 10, 15, 17, 100, 255, 1000};
    
    for (int num : nonPowers) {
      assertFalse(CheckTwosExponent.isPowerOfTwo(num), "isPowerOfTwo(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo2(num), "isPowerOfTwo2(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo3(num), "isPowerOfTwo3(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo4(num), "isPowerOfTwo4(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo6(num), "isPowerOfTwo6(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo7(num), "isPowerOfTwo7(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo8(num), "isPowerOfTwo8(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo9(num), "isPowerOfTwo9(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo10(num), "isPowerOfTwo10(" + num + ")");
    }
  }

  @Test
  void testWithNegativeNumbers() {
    System.out.println("\n=== 테스트: 음수들 (2의 제곱수 아님) ===");
    int[] negatives = {-1, -2, -4, -8, -16};
    
    for (int num : negatives) {
      assertFalse(CheckTwosExponent.isPowerOfTwo(num), "isPowerOfTwo(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo2(num), "isPowerOfTwo2(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo3(num), "isPowerOfTwo3(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo4(num), "isPowerOfTwo4(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo6(num), "isPowerOfTwo6(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo7(num), "isPowerOfTwo7(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo8(num), "isPowerOfTwo8(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo9(num), "isPowerOfTwo9(" + num + ")");
      assertFalse(CheckTwosExponent.isPowerOfTwo10(num), "isPowerOfTwo10(" + num + ")");
    }
  }

  @Test
  void testWithLargePowerOfTwo() {
    System.out.println("\n=== 테스트: 큰 2의 제곱수 ===");
    int largeValue = 1073741824; // 2^30
    
    assertTrue(CheckTwosExponent.isPowerOfTwo(largeValue), "isPowerOfTwo(2^30)");
    assertTrue(CheckTwosExponent.isPowerOfTwo2(largeValue), "isPowerOfTwo2(2^30)");
    assertTrue(CheckTwosExponent.isPowerOfTwo3(largeValue), "isPowerOfTwo3(2^30)");
    assertTrue(CheckTwosExponent.isPowerOfTwo4(largeValue), "isPowerOfTwo4(2^30)");
    assertFalse(CheckTwosExponent.isPowerOfTwo6(largeValue), "isPowerOfTwo6(2^30)");
    assertTrue(CheckTwosExponent.isPowerOfTwo7(largeValue), "isPowerOfTwo7(2^30)");
    assertTrue(CheckTwosExponent.isPowerOfTwo8(largeValue), "isPowerOfTwo8(2^30)");
    assertTrue(CheckTwosExponent.isPowerOfTwo9(largeValue), "isPowerOfTwo9(2^30)");
    assertTrue(CheckTwosExponent.isPowerOfTwo10(largeValue), "isPowerOfTwo10(2^30)");
  }

  @Test
  void testPerformanceComparison() {
    System.out.println("\n========== 성능 비교 ==========");
    int iterations = 10_000_000;
    int[] testValues = {0, 1, 2, 3, 16, 17, 1024, 1025};

    // 각 메서드의 성능 측정
    long time1 = measurePerformance(iterations, testValues, CheckTwosExponent::isPowerOfTwo);
    long time2 = measurePerformance(iterations, testValues, CheckTwosExponent::isPowerOfTwo2);
    long time3 = measurePerformance(iterations, testValues, CheckTwosExponent::isPowerOfTwo3);
    long time4 = measurePerformance(iterations, testValues, CheckTwosExponent::isPowerOfTwo4);
    long time6 = measurePerformance(iterations, testValues, CheckTwosExponent::isPowerOfTwo6);
    long time7 = measurePerformance(iterations, testValues, CheckTwosExponent::isPowerOfTwo7);
    long time8 = measurePerformance(iterations, testValues, CheckTwosExponent::isPowerOfTwo8);
    long time9 = measurePerformance(iterations, testValues, CheckTwosExponent::isPowerOfTwo9);
    long time10 = measurePerformance(iterations, testValues, CheckTwosExponent::isPowerOfTwo10);

    System.out.println("반복 횟수: " + String.format("%,d", iterations * testValues.length));
    System.out.println("-----------------------------------");
    System.out.printf("방법 1  (n & n-1):              %,10d ns (%.2f ms) - 기준\n", time1, time1 / 1_000_000.0);
    System.out.printf("방법 2  (n & n-1 + n!=0):       %,10d ns (%.2f ms) - %.2fx\n", time2, time2 / 1_000_000.0, (double) time2 / time1);
    System.out.printf("방법 3  (bitCount):             %,10d ns (%.2f ms) - %.2fx\n", time3, time3 / 1_000_000.0, (double) time3 / time1);
    System.out.printf("방법 4  (n>0 + n & n-1):        %,10d ns (%.2f ms) - %.2fx\n", time4, time4 / 1_000_000.0, (double) time4 / time1);
    System.out.printf("방법 6  (leading/trailing):     %,10d ns (%.2f ms) - %.2fx\n", time6, time6 / 1_000_000.0, (double) time6 / time1);
    System.out.printf("방법 7  (n & -n):               %,10d ns (%.2f ms) - %.2fx\n", time7, time7 / 1_000_000.0, (double) time7 / time1);
    System.out.printf("방법 8  (log):                  %,10d ns (%.2f ms) - %.2fx\n", time8, time8 / 1_000_000.0, (double) time8 / time1);
    System.out.printf("방법 9  (나눗셈 루프):          %,10d ns (%.2f ms) - %.2fx\n", time9, time9 / 1_000_000.0, (double) time9 / time1);
    System.out.printf("방법 10 (비트 카운트 루프):     %,10d ns (%.2f ms) - %.2fx\n", time10, time10 / 1_000_000.0, (double) time10 / time1);
    System.out.println("===================================\n");
  }

  @FunctionalInterface
  interface PowerOfTwoChecker {
    boolean check(int n);
  }

  private long measurePerformance(int iterations, int[] testValues, PowerOfTwoChecker checker) {
    long start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      for (int value : testValues) {
        checker.check(value);
      }
    }
    return System.nanoTime() - start;
  }
}
