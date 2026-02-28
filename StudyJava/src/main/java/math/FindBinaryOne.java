package math;

public class FindBinaryOne {

  public static void main(String[] args) {

  }

  /**
   * 방법 1: Java 내장 메서드 사용
   * Integer.bitCount()를 사용하여 1의 개수를 세는 가장 간단한 방법
   * 내부적으로 최적화된 알고리즘을 사용하므로 성능이 가장 좋음
   * 시간 복잡도: O(1) - 내부적으로 lookup table 사용
   */
  public static int countOnesUsingBitCount(int n) {
    return Integer.bitCount(n);
  }

  /**
   * 방법 2: 비트 연산 - 각 비트를 오른쪽으로 이동하며 확인
   * n & 1: 가장 오른쪽 비트가 1인지 확인 (1이면 1, 0이면 0)
   * n >>= 1: 비트를 오른쪽으로 한 칸 이동 (2로 나누는 것과 동일)
   * 모든 비트를 순회하며 1의 개수를 카운트
   * 시간 복잡도: O(log n) - 비트 수만큼 반복
   */
  public static int countOnesUsingBitShift(int n) {
    int count = 0;
    while (n > 0) {
      count += n & 1;
      n >>= 1;
    }
    return count;
  }

  /**
   * 방법 3: Brian Kernighan 알고리즘 (최적화된 비트 연산)
   * n &= (n - 1): 가장 오른쪽의 1 비트를 제거
   * 예: 12(1100) & 11(1011) = 8(1000), 8(1000) & 7(0111) = 0(0000)
   * 1이 있는 횟수만큼만 반복하므로 방법 2보다 효율적
   * 시간 복잡도: O(k) - k는 1의 개수
   */
  public static int countOnesUsingBrianKernighan(int n) {
    int count = 0;
    while (n > 0) {
      n &= (n - 1);
      count++;
    }
    return count;
  }

  /**
   * 방법 4: 나머지 연산 사용 (가장 직관적)
   * n % 2: 2로 나눈 나머지 (0 또는 1)
   * n /= 2: 2로 나누기 (오른쪽으로 한 칸 이동)
   * 가장 이해하기 쉽지만 나눗셈 연산이 포함되어 상대적으로 느림
   * 시간 복잡도: O(log n) - 비트 수만큼 반복
   */
  public static int countOnesUsingModulo(int n) {
    int count = 0;
    while (n > 0) {
      int reside = n % 2;
      count += reside;
      n /= 2;
    }
    return count;
  }
}
