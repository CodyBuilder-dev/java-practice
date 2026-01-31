package datastructure;

public class SingleLinkedList {
  SingleLinkNode<Integer> head;

  public void add(int value) {
      SingleLinkNode<Integer> newNode = new SingleLinkNode<>(value);
      if (head == null) {
          head = newNode;
          return;
      }
      SingleLinkNode<Integer> current = head;
      while (current.getNext() != null) {
          current = current.getNext();
      }
      current.setNext(newNode);
  }

  public void inverse() {
      SingleLinkNode<Integer> prev = null;
      SingleLinkNode<Integer> curr = head;
      while (curr != null) {
          SingleLinkNode<Integer> nextTemp = curr.getNext();
          curr.setNext(prev);
          prev = curr;
          curr = nextTemp;
      }
      head = prev;
  }

  /**
   * 뒤에서 n번째 노드를 찾는 기존 방식 (2회 순회: 길이 측정 후 이동)
   * @param n 뒤에서부터의 인덱스 (0이 마지막 노드)
   */
  public SingleLinkNode<Integer> getNthNodeFromBackwordOriginal(int n) {
      if (n < 0 || head == null) return null;

      // 1. 리스트의 전체 길이 구하기
      int length = 0;
      SingleLinkNode<Integer> temp = head;
      while (temp != null) {
          length++;
          temp = temp.getNext();
      }

      // 2. 찾으려는 인덱스가 범위를 벗어난 경우
      if (n >= length) return null;

      // 3. 앞에서부터 (length - n - 1)만큼 이동
      SingleLinkNode<Integer> current = head;
      for (int i = 0; i < length - n - 1; i++) {
          current = current.getNext();
      }

      return current;
  }

  /**
   * 뒤에서 n번째 노드를 찾는 최적화된 방식 (Two-Pointer 방식, Single Pass)
   * @param n 뒤에서부터의 인덱스 (0이 마지막 노드)
   */
  public SingleLinkNode<Integer> getNthNodeFromBackwordOptimized(int n) {
      if (n < 0 || head == null) return null;

      SingleLinkNode<Integer> first = head;
      SingleLinkNode<Integer> second = head;

      // 1. first 포인터를 n만큼 먼저 이동시킴
      for (int i = 0; i < n; i++) {
          if (first.getNext() == null) return null; // n이 리스트 길이보다 큰 경우
          first = first.getNext();
      }

      // 2. first가 끝에 도달할 때까지 두 포인터를 동시에 이동
      while (first.getNext() != null) {
          first = first.getNext();
          second = second.getNext();
      }

      return second;
  }

  public void printList() {
      SingleLinkNode<Integer> current = head;
      while (current != null) {
          System.out.print(current.getValue() + " ");
          current = current.getNext();
      }
      System.out.println();
  }

  public static void main(String[] args) {
      SingleLinkedList list = new SingleLinkedList();
      System.out.println("--- SingleLinkedList Inverse Test ---");
      System.out.print("Original Data: ");
      for (int i = 1; i <= 5; i++) {
          System.out.print(i + " ");
          list.add(i);
      }
      System.out.println();

      list.inverse();
      System.out.print("Inversed Data: ");
      list.printList();
      
      // 검증: 5 4 3 2 1 순서여야 함
      SingleLinkNode<Integer> curr = list.head;
      boolean correct = true;
      for (int i = 5; i >= 1; i--) {
          if (curr == null || curr.getValue() != i) {
              correct = false;
              break;
          }
          curr = curr.getNext();
      }
      System.out.println("Inverse Correctness: " + correct);

      System.out.println("\n--- Comparison: Original vs Optimized ---");
      // 현재 리스트 상태: 5 4 3 2 1
      
      int[] testN = {0, 2, 4, -1, 5};
      int[] expectedValues = {1, 3, 5, -1, -1};

      boolean allCorrect = true;
      for (int i = 0; i < testN.length; i++) {
          int n = testN[i];
          SingleLinkNode<Integer> nodeOriginal = list.getNthNodeFromBackwordOriginal(n);
          SingleLinkNode<Integer> nodeOptimized = list.getNthNodeFromBackwordOptimized(n);
          
          Integer valOri = (nodeOriginal != null) ? nodeOriginal.getValue() : null;
          Integer valOpt = (nodeOptimized != null) ? nodeOptimized.getValue() : null;
          
          System.out.printf("n=%2d | Original: %4s | Optimized: %4s | ", n, valOri, valOpt);
          
          boolean match = (valOri == null && valOpt == null) || (valOri != null && valOri.equals(valOpt));
          boolean isEntryCorrect = false;
          if (expectedValues[i] == -1) {
              isEntryCorrect = (valOri == null && valOpt == null);
          } else {
              isEntryCorrect = (valOri != null && valOri.equals(expectedValues[i]) && valOpt != null && valOpt.equals(expectedValues[i]));
          }

          if (!match || !isEntryCorrect) allCorrect = false;
          System.out.println(match && isEntryCorrect ? "[OK]" : "[FAIL]");
      }
      System.out.println("Total Correctness: " + allCorrect);

      // 대량 데이터 성능 테스트 (평균값 측정)
      System.out.println("\n--- Performance Benchmarking (100,000 nodes, 100 iterations) ---");
      SingleLinkedList largeList = new SingleLinkedList();
      for (int i = 0; i < 100000; i++) largeList.add(i);

      int testIndex = 50000;
      int iterations = 100;
      
      // Warm up
      for (int i = 0; i < 1000; i++) {
          largeList.getNthNodeFromBackwordOriginal(testIndex);
          largeList.getNthNodeFromBackwordOptimized(testIndex);
      }

      long totalOriginal = 0;
      for (int i = 0; i < iterations; i++) {
          long start = System.nanoTime();
          largeList.getNthNodeFromBackwordOriginal(testIndex);
          long end = System.nanoTime();
          totalOriginal += (end - start);
      }
      System.out.println("Original (2-Pass) Avg Time : " + (totalOriginal / iterations / 1000) + " us");

      long totalOptimized = 0;
      for (int i = 0; i < iterations; i++) {
          long start = System.nanoTime();
          largeList.getNthNodeFromBackwordOptimized(testIndex);
          long end = System.nanoTime();
          totalOptimized += (end - start);
      }
      System.out.println("Optimized (1-Pass) Avg Time: " + (totalOptimized / iterations / 1000) + " us");
  }
}
