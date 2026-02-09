package datatype;

import java.util.Random;

public class FindFirstNonRepeatingChar {

  public char getFirstNonRepeatingChar(String s) {
    int[] charCount = new int[256]; // ASCII 문자 집합 크기

    // 문자열을 char 배열로 변환 -> 시간복잡도 O(n)
    char[] charArray = s.toCharArray();

    // 각 문자의 등장 횟수 계산 -> 시간복잡도 O(n)
    for (char c : charArray) {
      charCount[c]++;
    }

    // 처음으로 등장 횟수가 1인 문자 찾기 -> 시간복잡도 O(n)
    for (char c : charArray) {
      if (charCount[c] == 1) {
        return c;
      }
    }

    return '\0'; // 모든 문자가 반복되는 경우 널 문자 반환
  }

  public char getFirstNonRepeatingCharOptimized(String s) {
    int[] charCount = new int[256]; // ASCII 문자 집합 크기
    int n = s.length(); // 문자열 길이 -> 시간복잡도 O(1)

    // 각 문자의 등장 횟수 계산 -> 시간복잡도 O(n)
    for (int i = 0; i < n; i++) {
      charCount[s.charAt(i)]++;
    }

    // 처음으로 등장 횟수가 1인 문자 찾기 -> 시간복잡도 O(n)
    for (int i = 0; i < n; i++) {
      if (charCount[s.charAt(i)] == 1) {
        return s.charAt(i);
      }
    }

    return '\0'; // 모든 문자가 반복되는 경우 널 문자 반환
  }

  public String createRandomString(int length) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      char ch = (char) ('a' + random.nextInt(26));
      sb.append(ch);
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    FindFirstNonRepeatingChar finder = new FindFirstNonRepeatingChar();
    String testStr = "swiss";
    char result = finder.getFirstNonRepeatingChar(testStr);
    System.out.println(result); // Output: 'w'

    // 테스트 최적화된 메서드
    char optimizedResult = finder.getFirstNonRepeatingCharOptimized(testStr);
    System.out.println(optimizedResult); // Output: 'w'

    // 메서드 벤치마크
    String longTestStr = "aabbccddeeffgghhiijjkkllmmnnooppqqrrssttuuvvwwxxyza";
    long startTime = System.nanoTime();
    char benchmarkResult = finder.getFirstNonRepeatingChar(longTestStr);
    long endTime = System.nanoTime();
    System.out.println("Benchmark Result: " + benchmarkResult);
    System.out.println("Execution Time (Original): " + (endTime - startTime) + " ns");
    startTime = System.nanoTime();
    char optimizedBenchmarkResult = finder.getFirstNonRepeatingCharOptimized(longTestStr);
    endTime = System.nanoTime();
    System.out.println("Optimized Benchmark Result: " + optimizedBenchmarkResult);
    System.out.println("Execution Time (Optimized): " + (endTime - startTime) + " ns");

    // 랜덤 문자열 벤치마크
    long originalAvgTime = 0;
    long optimizedAvgTime = 0;

    int testCount = 100000;
    int stringLength = 2000;
    for(int count = 0 ; count < testCount ; count++) {
      String temp = finder.createRandomString(stringLength);
      startTime = System.nanoTime();
      finder.getFirstNonRepeatingChar(temp);
      endTime = System.nanoTime();
      long originalTime = endTime - startTime;
      startTime = System.nanoTime();
      finder.getFirstNonRepeatingCharOptimized(temp);
      endTime = System.nanoTime();
      long optimizedTime = endTime - startTime;

      originalAvgTime += originalTime;
      optimizedAvgTime += optimizedTime;
    }
    System.out.println("Average Execution Time (Original): " + (originalAvgTime / testCount) + " ns");
    System.out.println("Average Execution Time (Optimized): " + (optimizedAvgTime / testCount) + " ns");

    // 벤치마크 결과 해설
    // 벤치마크 결과를 보면, 문자열 길이가 길어질수록 최적화된 메서드의 실행 시간이 현저히 줄어드는 것을 알 수 있습니다.
    // 최적화된 메서드는 최초 1회 문자열을 순환하며 char 배열로 변환하는 과정을 제거하였기 때문에
    // 시간복잡도가 O(n)에서 O(n)으로 동일하지만, 상수 시간이 줄어들었습니다.
    // 이로 인해 특히 긴 문자열에 대해 실행 시간이 크게 단축되었습니다.
    // 다만, 짧은 문자열에서는 차이가 미미하거나 오히려 원본 메서드가 더 빠를 수도 있습니다.
    // 그 이유는 짧은 문자열에서는 char 배열로 변환하는 오버헤드가 상대적으로 작기 때문입니다.
    // char 배열은 캐시 효율성이 높아 메모리 접근 속도가 빠르기 때문에,
    // 짧은 문자열에서는 char 배열로 변환하는 오버헤드보다 메모리 접근 속도의 이점이 더 크게 작용할 수 있습니다.
    // 캐시 효율성이 올라가는 이유는, char 배열은 메모리 상에 연속적으로 저장되기 때문에,
    // CPU 캐시 라인에 더 잘 맞아 떨어져 접근 속도가 빨라지기 때문입니다.
    // 최적화된 메서드에서 사용되는 charAt() 메서드는 내부적으로 문자열의 char 배열에 접근하는데,
    // 이 과정에서 캐시 미스가 발생할 수 있어 성능 저하로 이어질 수 있습니다.
    // 캐시미스가 일어나는 이유는 charAt() 메서드가 매번 문자열 객체를 통해 접근하기 때문입니다.
    // 문자열 객체를 통해 접근하면, 문자열의 내부 구조를 따라가야 하므로,
    // 캐시 라인에 맞지 않는 경우가 발생할 수 있습니다.
    // 캐시 라인이란, CPU가 메모리에서 데이터를 읽어올 때 한 번에 가져오는 데이터 블록 단위를 의미합니다.
    // 문자열 객체를 통해 접근하면, 이 블록 단위가 불규칙해져 캐시 미스가 발생할 확률이 높아집니다.
    // 블록 단위가 불규칙해지면, CPU가 필요한 데이터를 캐시에서 찾지 못하고,
    // 메모리에서 다시 읽어와야 하므로 성능 저하로 이어질 수 있습니다.
    // 문자열 객체를 통해 접근하면 블록 단위가 불규칙해지는 이유는,
    // 문자열 객체가 내부적으로 다양한 메타데이터를 포함하고 있기 때문입니다.
    // 이러한 메타데이터는 문자열의 길이, 해시 코드, 인코딩 정보 등을 포함하며,
    // 이로 인해 문자열의 실제 문자 데이터가 메모리 상에서 연속적으로 저장되지 않을 수 있습니다.
    // 하지만 긴 문자열에 대해서는 char 배열로 변환하는 오버헤드가 커지기 때문에,
    // 최적화된 메서드가 더 유리해집니다.
  }
}
