package sorting;

import static sorting.SortingComparison.isSorted;

import java.util.Random;

public class InsertionSort {

  public static void main(String[] args) {
    int[] sizes = {1000, 5000, 10000, 20000};
    Random random = new Random();

    System.out.println("Insertion Sort Time Complexity Test:");
    System.out.println("------------------------------------");

    for (int size : sizes) {
      int[] arr = new int[size];
      for (int i = 0; i < size; i++) {
        arr[i] = random.nextInt(size * 10);
      }

      long startTime = System.nanoTime();
      insertionSort(arr);
      long endTime = System.nanoTime();

      long duration = (endTime - startTime) / 1_000_000; // milliseconds
      boolean sorted = isSorted(arr);
      System.out.printf("Array Size: %6d | Time Taken: %4d ms | Sorted: %s%n", size, duration, sorted);
    }
  }

  public static void insertionSort(int[] arr) {
    int n = arr.length;
    for (int i = 1; i < n; i++) {
      int key = arr[i]; // key를 쓰는 이유 : 매번 swap하면서 진행하지 말고, 한칸씩 밀어낸 후 마지막에 대입. 매번 swap하는 경우 선택정렬보다 더 느릴 수 있음
      int j = i - 1;
      while (j >= 0 && arr[j] > key) {
        arr[j + 1] = arr[j];
        j--;
      }
      arr[j + 1] = key;
    }
  }
}
