package sorting;

import java.util.TreeMap;
import java.util.Map;

/**
 * Counting Sort implementation using a TreeMap for sorting integer arrays.
 * Time Complexity and Space Complexity differ based on implementation.(HashMap vs TreeMap)
 *
 * HashMap implementation:
 * Time Complexity: O(n + k log k), where n is the number of elements and k is the number of unique values.
 *    n: time to traverse the array to populate the map
 *    k log k: time to sort the unique keys (if needed)
 * Space Complexity: O(n + k)
 *    n: space for the array
 *    k: space for the map entries
 *
 * TreeMap implementation:
 * Time Complexity: O(n log k), where n is the number of elements and k is the number of unique values.
 *    n log k: time to insert n elements into a TreeMap of size k
 * Space Complexity: O(n + k)
 *    n: space for the array
 *    k: space for the map entries
 */
public class CountingSortWithMap {

  public static void main(String[] args) {
    int[] sizes = {1000, 5000, 10000, 20000};
    java.util.Random random = new java.util.Random();

    System.out.println("Counting Sort With Map Time Complexity Test:");
    System.out.println("--------------------------------------------");

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
    Map<Integer, Integer> map = new TreeMap<>(); // TreeMap으로 변경하여 키 정렬 보장
    for (int num : arr) {
      map.put(num, map.getOrDefault(num, 0) + 1);
    }
    int index = 0;
    for (int num : map.keySet()) {
      for (int i = 0; i < map.get(num); i++) {
        arr[index++] = num;
      }
    }
  }
}
