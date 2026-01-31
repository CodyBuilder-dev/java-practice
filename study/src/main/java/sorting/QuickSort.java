package sorting;

import static sorting.SortingComparison.isSorted;
import static sorting.SortingComparison.swap;

import java.util.Random;

/**
 * 퀵 정렬(Quick Sort) 구현
 * 시간 복잡도:
 * - 평균: O(n log n)
 * - 최악: O(n^2) (피벗이 항상 최댓값이나 최솟값으로 선택될 경우)
 * 공간 복잡도: O(log n) (재귀 호출 스택)
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] sizes = {1000, 5000, 10000, 20000};
        Random random = new Random();

        System.out.println("Quick Sort Time Complexity Test:");
        System.out.println("--------------------------------");

        for (int size : sizes) {
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = random.nextInt(size * 10);
            }

            long startTime = System.nanoTime();
            quickSort(arr, 0, arr.length - 1);
            long endTime = System.nanoTime();

            long duration = (endTime - startTime) / 1_000_000; // milliseconds
            boolean sorted = isSorted(arr);
            System.out.printf("Array Size: %6d | Time Taken: %4d ms | Sorted: %s%n", size, duration, sorted);
        }
    }

    /**
     * 퀵 정렬 시작 메서드
     */
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // 피벗을 기준으로 배열을 분할하고 피벗의 위치를 반환
            int pivotIndex = partition(arr, low, high);

            // 피벗을 제외한 왼쪽과 오른쪽 부분 배열을 재귀적으로 정렬
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    /**
     * 배열을 피벗을 기준으로 분할하는 메서드 (Lomuto partition scheme)
     */
    private static int partition(int[] arr, int low, int high) {

        int pivot = arr[high];      // 여기서는 가장 오른쪽 요소를 피벗으로 선택
        int i = (low - 1);          // 피벗보다 작은 요소들의 마지막 인덱스

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {  // 피벗보다 작거나 같은 요소 발견!
                i++;                // 작은 요소 구역 한 칸 확장
                swap(arr, i, j);    // 그 자리에 현재 값(작은 값)을 넣어줌
            }
        }

        // 마지막에 피벗을 자기보다 작은 구역 바로 다음(i + 1)으로 이동
        swap(arr, i + 1, high);
        return i + 1;
    }
}
