package sorting;

import static sorting.SortingComparison.isSorted;

import java.util.Random;

public class BubbleSort {

  public static void main(String[] args) {
    int[] sizes = {1000, 5000, 10000, 20000};
    Random random = new Random();

    System.out.println("Bubble Sort Time Complexity Test:");
    System.out.println("---------------------------------");

    for (int size : sizes) {
      int[] arr = new int[size];
      for (int i = 0; i < size; i++) {
        arr[i] = random.nextInt(size * 10);
      }

      long startTime = System.nanoTime();
      bubbleSort(arr);
      long endTime = System.nanoTime();

      long duration = (endTime - startTime) / 1_000_000; // milliseconds
      boolean sorted = isSorted(arr);
      System.out.printf("Array Size: %6d | Time Taken: %4d ms | Sorted: %s%n", size, duration, sorted);
    }
  }



  public static void bubbleSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n -1 ; i++) {
      for (int j = 0; j < n - i - 1; j++){
        if (arr[j] > arr[j+1]) {
          int temp = arr[j];
          arr[j] = arr[j+1];
          arr[j+1] = temp;
        }
      }
    }
  }
}
