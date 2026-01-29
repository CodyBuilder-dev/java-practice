package sorting;

import java.util.Arrays;
import java.util.Random;

public class SortingComparison {
    public static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[] sizes = {1000, 5000, 10000, 20000};
        Random random = new Random();

        System.out.println("Sorting Algorithms Performance Comparison:");
        System.out.println("===========================================");
        System.out.printf("%-12s | %-12s | %-12s | %-15s%n", "Array Size", "Bubble Sort", "Cocktail Sort", "Cocktail (Opt)");
        System.out.println("---------------------------------------------------------------------------");

        for (int size : sizes) {
            int[] baseArr = new int[size];
            for (int i = 0; i < size; i++) {
                baseArr[i] = random.nextInt(size * 10);
            }

            // Test Bubble Sort
            int[] arr1 = Arrays.copyOf(baseArr, size);
            long start1 = System.nanoTime();
            BubbleSort.bubbleSort(arr1);
            long end1 = System.nanoTime();
            long time1 = (end1 - start1) / 1_000_000;

            // Test Cocktail Sort (Original)
            int[] arr2 = Arrays.copyOf(baseArr, size);
            long start2 = System.nanoTime();
            CocktailSort.cocktailSort(arr2);
            long end2 = System.nanoTime();
            long time2 = (end2 - start2) / 1_000_000;

            // Test Cocktail Sort (Optimized)
            int[] arr3 = Arrays.copyOf(baseArr, size);
            long start3 = System.nanoTime();
            CocktailSortOptimized.cocktailSort(arr3);
            long end3 = System.nanoTime();
            long time3 = (end3 - start3) / 1_000_000;

            System.out.printf("%-12d | %8d ms | %10d ms | %12d ms%n", size, time1, time2, time3);
        }
        System.out.println("===========================================");
    }
}
