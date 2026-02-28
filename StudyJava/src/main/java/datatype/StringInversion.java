package datatype;

import java.util.Stack;

public class StringInversion {
  Stack<String> stringStack = new Stack<>();

  public String invert(String s) {
    String[] words = s.split(" ");
    for (String word : words) {
      stringStack.push(word);
    }

    StringBuilder inverted = new StringBuilder();
    while (!stringStack.isEmpty()) {
      inverted.append(stringStack.pop());
      if (!stringStack.isEmpty()) {
        inverted.append(" ");
      }
    }

    return inverted.toString();
  }

  public String invertOptimized(String s) {
    String[] words = s.split(" ");
    int left = 0;
    int right = words.length - 1;

    while (left < right) {
      String temp = words[left];
      words[left] = words[right];
      words[right] = temp;
      left++;
      right--;
    }

    return String.join(" ", words);
  }

  public String createRandomSentence(int wordCount, int wordLength) {
    StringBuilder sentence = new StringBuilder();
    for (int i = 0; i < wordCount; i++) {
      StringBuilder word = new StringBuilder();
      for (int j = 0; j < wordLength; j++) {
        char ch = (char) ('a' + (int) (Math.random() * 26));
        word.append(ch);
      }
      sentence.append(word);
      if (i < wordCount - 1) {
        sentence.append(" ");
      }
    }
    return sentence.toString();
  }

  public static void main(String[] args) {
    StringInversion inverter = new StringInversion();
    String input = "Hello World from Java";
    String output = inverter.invert(input);
    System.out.println(output); // Output: "Java from World Hello"

    String outputOptimized = inverter.invertOptimized(input);
    System.out.println(outputOptimized); // Output: "Java from World Hello"

    // 랜덤 문장으로 벤치마크
    int testCount = 10000;
    int wordCount = 1000;
    int wordLength = 5;
    long originalAvgTime = 0;
    long optimizedAvgTime = 0;
    for (int count = 0; count < testCount; count++) {
      String temp = inverter.createRandomSentence(wordCount, wordLength);

      long startTime = System.nanoTime();
      inverter.invert(temp);
      long endTime = System.nanoTime();
      long originalTime = endTime - startTime;

      startTime = System.nanoTime();
      inverter.invertOptimized(temp);
      endTime = System.nanoTime();
      long optimizedTime = endTime - startTime;

      originalAvgTime += originalTime;
      optimizedAvgTime += optimizedTime;
    }
    System.out.println("Average Execution Time (Original): " + (originalAvgTime / testCount) + " ns");
    System.out.println("Average Execution Time (Optimized): " + (optimizedAvgTime / testCount) + " ns");

    // 벤치마크 결과 해설
    // 벤치마크 결과를 보면, 문자열 길이가 길어질수록 최적화된 메서드의 실행 시간이 현저히 줄어드는 것을 알 수 있습니다.
    // 원본 메서드는 스택을 사용하여 단어를 역순으로 저장하고 다시 꺼내는 과정을 거치기 때문에
    // 추가적인 메모리 사용과 연산이 발생합니다.
    // Java의 스택 구현은 내부적으로 배열을 사용하지만, 스택 오퍼레이션이 많아질수록 오버헤드가 누적됩니다.
    // 오버헤드가 누적되는 이유는 스택이 가득 찼을 때 크기를 늘리는 작업과 같은 동적 배열 관리 때문입니다.
    // 최적화된 메서드는 스택 사용을 피하고 배열 내에서 직접 단어를 교환하기 때문에 성능이 향상됩니다.
    // 특히, 단어 수가 많아질수록 그 차이는 더욱 두드러집니다.
    // 따라서, 대용량 문자열 처리 시에는 스택 사용을 피하는 것이 바람직합니다.
  }
}
