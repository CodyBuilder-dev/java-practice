package math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FindBinaryOneTest {

  @Test
  void testAllMethodsWithZero() {
    assertEquals(0, FindBinaryOne.countOnesUsingBitCount(0));
    assertEquals(0, FindBinaryOne.countOnesUsingBitShift(0));
    assertEquals(0, FindBinaryOne.countOnesUsingBrianKernighan(0));
    assertEquals(0, FindBinaryOne.countOnesUsingModulo(0));
  }

  @Test
  void testAllMethodsWithOne() {
    assertEquals(1, FindBinaryOne.countOnesUsingBitCount(1));
    assertEquals(1, FindBinaryOne.countOnesUsingBitShift(1));
    assertEquals(1, FindBinaryOne.countOnesUsingBrianKernighan(1));
    assertEquals(1, FindBinaryOne.countOnesUsingModulo(1));
  }

  @Test
  void testAllMethodsWithPowerOfTwo() {
    // 8 = 1000 (binary) -> 1개의 1
    assertEquals(1, FindBinaryOne.countOnesUsingBitCount(8));
    assertEquals(1, FindBinaryOne.countOnesUsingBitShift(8));
    assertEquals(1, FindBinaryOne.countOnesUsingBrianKernighan(8));
    assertEquals(1, FindBinaryOne.countOnesUsingModulo(8));
  }

  @Test
  void testAllMethodsWithMixedBits() {
    // 15 = 1111 (binary) -> 4개의 1
    assertEquals(4, FindBinaryOne.countOnesUsingBitCount(15));
    assertEquals(4, FindBinaryOne.countOnesUsingBitShift(15));
    assertEquals(4, FindBinaryOne.countOnesUsingBrianKernighan(15));
    assertEquals(4, FindBinaryOne.countOnesUsingModulo(15));
  }

  @Test
  void testAllMethodsWithLargeNumber() {
    // 255 = 11111111 (binary) -> 8개의 1
    assertEquals(8, FindBinaryOne.countOnesUsingBitCount(255));
    assertEquals(8, FindBinaryOne.countOnesUsingBitShift(255));
    assertEquals(8, FindBinaryOne.countOnesUsingBrianKernighan(255));
    assertEquals(8, FindBinaryOne.countOnesUsingModulo(255));
  }

  @Test
  void testAllMethodsWithAlternatingBits() {
    // 85 = 01010101 (binary) -> 4개의 1
    assertEquals(4, FindBinaryOne.countOnesUsingBitCount(85));
    assertEquals(4, FindBinaryOne.countOnesUsingBitShift(85));
    assertEquals(4, FindBinaryOne.countOnesUsingBrianKernighan(85));
    assertEquals(4, FindBinaryOne.countOnesUsingModulo(85));
  }

  @Test
  void testAllMethodsWithLargeValue() {
    // 1023 = 1111111111 (binary) -> 10개의 1
    assertEquals(10, FindBinaryOne.countOnesUsingBitCount(1023));
    assertEquals(10, FindBinaryOne.countOnesUsingBitShift(1023));
    assertEquals(10, FindBinaryOne.countOnesUsingBrianKernighan(1023));
    assertEquals(10, FindBinaryOne.countOnesUsingModulo(1023));
  }

  @Test
  void testPerformanceComparison() {
    int testValue = 123456789;
    int iterations = 1_000_000;

    // 각 메서드의 성능 측정
    long start1 = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      FindBinaryOne.countOnesUsingBitCount(testValue);
    }
    long time1 = System.nanoTime() - start1;

    long start2 = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      FindBinaryOne.countOnesUsingBitShift(testValue);
    }
    long time2 = System.nanoTime() - start2;

    long start3 = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      FindBinaryOne.countOnesUsingBrianKernighan(testValue);
    }
    long time3 = System.nanoTime() - start3;

    long start4 = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      FindBinaryOne.countOnesUsingModulo(testValue);
    }
    long time4 = System.nanoTime() - start4;

    // 결과 출력
    System.out.println("\n========== 성능 비교 결과 ==========");
    System.out.println("테스트 값: " + testValue + " (이진수: " + Integer.toBinaryString(testValue) + ")");
    System.out.println("반복 횟수: " + String.format("%,d", iterations));
    System.out.println("-----------------------------------");
    System.out.printf("방법 1 (Integer.bitCount):  %,d ns (%.2f ms) - 기준\n", time1, time1 / 1_000_000.0);
    System.out.printf("방법 2 (비트 시프트):       %,d ns (%.2f ms) - 방법1 대비 %.2fx\n", 
        time2, time2 / 1_000_000.0, (double) time2 / time1);
    System.out.printf("방법 3 (Brian Kernighan):   %,d ns (%.2f ms) - 방법1 대비 %.2fx\n", 
        time3, time3 / 1_000_000.0, (double) time3 / time1);
    System.out.printf("방법 4 (나머지 연산):       %,d ns (%.2f ms) - 방법1 대비 %.2fx\n", 
        time4, time4 / 1_000_000.0, (double) time4 / time1);
    System.out.println("===================================\n");

    // 모든 메서드가 같은 결과를 반환하는지 확인
    int result1 = FindBinaryOne.countOnesUsingBitCount(testValue);
    int result2 = FindBinaryOne.countOnesUsingBitShift(testValue);
    int result3 = FindBinaryOne.countOnesUsingBrianKernighan(testValue);
    int result4 = FindBinaryOne.countOnesUsingModulo(testValue);
    
    assertEquals(result1, result2);
    assertEquals(result1, result3);
    assertEquals(result1, result4);
  }
}
