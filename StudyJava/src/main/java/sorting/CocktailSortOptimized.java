package sorting;

import static sorting.SortingComparison.isSorted;
import static sorting.SortingComparison.swap;

import java.util.Random;

public class CocktailSortOptimized {

    public static void main(String[] args) {
        int[] sizes = {1000, 5000, 10000, 20000};
        Random random = new Random();

        System.out.println("Cocktail Sort Optimized Time Complexity Test:");
        System.out.println("---------------------------------------------");

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
        int start = 0, end = arr.length - 1;
        while (start < end) {
            int lastSwap = start;
            for (int i = start; i < end; i++) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    lastSwap = i;
                }
            }
            end = lastSwap;
            if (start >= end) break;

          for (int i = end; i > start; i--) {
                if (arr[i - 1] > arr[i]) {
                    swap(arr, i - 1, i);
                    lastSwap = i;
                }
            }
            start = lastSwap;
        }
    }
}
