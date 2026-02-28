package sorting;

import static sorting.SortingComparison.isSorted;
import static sorting.SortingComparison.swap;

import java.util.Random;

public class InsertionSortInefficient {

    public static void main(String[] args) {
        int[] sizes = {1000, 5000, 10000, 20000};
        Random random = new Random();

        System.out.println("Inefficient Insertion Sort (Swap-based) Time Complexity Test:");
        System.out.println("----------------------------------------------------------");

        for (int size : sizes) {
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = random.nextInt(size * 10);
            }

            long startTime = System.nanoTime();
            insertionSortInefficient(arr);
            long endTime = System.nanoTime();

            long duration = (endTime - startTime) / 1_000_000; // milliseconds
            boolean sorted = isSorted(arr);
            System.out.printf("Array Size: %6d | Time Taken: %4d ms | Sorted: %s%n", size, duration, sorted);
        }
    }

    public static void insertionSortInefficient(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int j = i;
            // 매번 swap을 수행하는 비효율적인 방식
            while (j > 0 && arr[j - 1] > arr[j]) {
                swap(arr, j, j - 1);
                j--;
            }
        }
    }
}
