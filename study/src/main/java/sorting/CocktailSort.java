package sorting;

import static sorting.SortingComparison.isSorted;

import java.util.Random;

public class CocktailSort {

  public static void main(String[] args) {
    int[] sizes = {1000, 5000, 10000, 20000};
    Random random = new Random();

    System.out.println("Cocktail Sort Time Complexity Test:");
    System.out.println("------------------------------------");

    for (int size : sizes) {
      int[] arr = new int[size];
      for (int i = 0; i < size; i++) {
        arr[i] = random.nextInt(size * 10);
      }

      long startTime = System.nanoTime();
      cocktailSort(arr);
      long endTime = System.nanoTime();

      long duration = (endTime - startTime) / 1_000_000; // milliseconds
      boolean sorted = isSorted(arr);
      System.out.printf("Array Size: %6d | Time Taken: %4d ms | Sorted: %s%n", size, duration, sorted);
    }
  }

  public static void cocktailSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n -1 ; i++) {
      int j = i;
      while (j < n - i - 1){
        if (arr[j] > arr[j+1]){
          int temp = arr[j];
          arr[j] = arr[j+1];
          arr[j+1] = temp;
        }
        j++;
      }
      while (j > i) {
        if( arr[j-1] > arr[j]){
          int temp = arr[j-1];
          arr[j-1] = arr[j];
          arr[j] = temp;
        }
        j--;
      }
    }
  }
}
