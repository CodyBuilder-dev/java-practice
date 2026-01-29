package sorting;

import static sorting.SortingComparison.isSorted;

import java.util.Random;

public class DoubleSelectionSort {

  public static void main(String[] args) {
    int[] sizes = {1000, 5000, 10000, 20000};
    Random random = new Random();

    System.out.println("Double Selection Sort Time Complexity Test:");
    System.out.println("-------------------------------------------");

    for (int size : sizes) {
      int[] arr = new int[size];
      for (int i = 0; i < size; i++) {
        arr[i] = random.nextInt(size * 10);
      }

      long startTime = System.nanoTime();
      doubleSelectionSort(arr);
      long endTime = System.nanoTime();

      long duration = (endTime - startTime) / 1_000_000; // milliseconds
      boolean sorted = isSorted(arr);
      System.out.printf("Array Size: %6d | Time Taken: %4d ms | Sorted: %s%n", size, duration, sorted);
    }
  }

  public static void doubleSelectionSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n / 2; i++) {
      int minIndex = i;
      int maxIndex = i;

      for (int j = i + 1; j < n - i; j++) {
        if (arr[j] < arr[minIndex]) {
          minIndex = j;
        }
        if (arr[j] > arr[maxIndex]) {
          maxIndex = j;
        }
      }

      // 최소값 교환
      swap(arr, i, minIndex);

      // 만약 maxIndex가 i였다면, 방금 전 최소값 교환으로 인해 maxIndex의 값이 minIndex 위치로 옮겨짐
      if (maxIndex == i) {
        maxIndex = minIndex;
      }

      // 최대값 교환
      swap(arr, n - i - 1, maxIndex);
    }
  }

  private static void swap(int[] arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }
}
