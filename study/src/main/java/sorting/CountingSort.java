package sorting;

/**
 * Counting Sort implementation for sorting integer arrays.
 * Time Complexity: O(n + K), where n is the number of elements and K is the range of input (max value).
 *    n: time to traverse the input array
 *    K: time to traverse the counting array
 * Space Complexity: O(n + K)
 *    n: space for the output array (if needed)
 *    K: space for the counting array
 * Condition :
 * 1. Arr must not be null.
 * 2. Arr must contain non-negative integers only.
 */
public class CountingSort {

  public static void main(String[] args) {
    int[] sizes = {1000, 5000, 10000, 20000};
    java.util.Random random = new java.util.Random();

    System.out.println("Counting Sort Time Complexity Test:");
    System.out.println("------------------------------------");

    for (int size : sizes) {
      int[] arr = new int[size];
      for (int i = 0; i < size; i++) {
        arr[i] = random.nextInt(size * 10);
      }

      long startTime = System.nanoTime();
      countingSort(arr);
      long endTime = System.nanoTime();

      long duration = (endTime - startTime) / 1_000_000; // milliseconds
      boolean sorted = sorting.SortingComparison.isSorted(arr);
      System.out.printf("Array Size: %6d | Time Taken: %4d ms | Sorted: %s%n", size, duration, sorted);
    }
  }

  public static void countingSort(int[] arr) {
    int max = arr[0];
    for (int num : arr) {
      max = Math.max(max, num);
    }
    int[] count = new int[max + 1];
    for (int num : arr) {
      count[num]++;
    }
    int index = 0;
    for (int i = 0; i <= max; i++) {
      while (count[i] > 0) {
        arr[index++] = i;
        count[i]--;
      }
    }
  }
}
