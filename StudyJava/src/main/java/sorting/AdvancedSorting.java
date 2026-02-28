package sorting;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * 효율적인 정렬 알고리즘 구현 예제
 * 계수 정렬(Counting Sort)과 기수 정렬(Radix Sort)
 */
public class AdvancedSorting {

    /**
     * 계수 정렬 (배열 기반 - 표준 방식)
     * 범위가 좁고 양이 많을 때 최적 (O(n + K), K는 데이터의 범위)
     */
    public static int[] countingSortArray(int[] arr) {
        if (arr.length == 0) return arr;

        // 1. 최댓값 찾기 (범위 설정)
        int max = Arrays.stream(arr).max().getAsInt();
        int min = Arrays.stream(arr).min().getAsInt();
        int range = max - min + 1;

        // 2. 빈도수를 저장할 배열 생성
        int[] count = new int[range];

        // 3. 빈도수 계산
        for (int num : arr) {
            count[num - min]++;
        }

        // 4. 결과 배열 구성
        int[] result = new int[arr.length];
        int idx = 0;
        for (int i = 0; i < range; i++) {
            while (count[i] > 0) {
                result[idx++] = i + min;
                count[i]--;
            }
        }
        return result;
    }

    /**
     * 계수 정렬 (TreeMap 기반 - Sparse 데이터용)
     * 범위가 매우 넓지만 데이터 종류는 적을 때 효율적
     * 시간 복잡도: O(n log k), k는 서로 다른 데이터 종류의 개수
     */
    public static int[] countingSortMap(int[] arr) {
        // TreeMap은 키를 자동으로 정렬된 상태로 유지함
        Map<Integer, Integer> frequencyMap = new TreeMap<>();

        for (int num : arr) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        int[] result = new int[arr.length];
        int idx = 0;
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            int val = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                result[idx++] = val;
            }
        }
        return result;
    }

    /**
     * 기수 정렬 (LSD 방식)
     * 자릿수(d)별로 계수 정렬을 반복 수행
     * 시간 복잡도: O(d(n + R)), R은 기수(Radix, 10진수면 10)
     */
    public static void radixSort(int[] arr) {
        if (arr.length == 0) return;

        int max = Arrays.stream(arr).max().getAsInt();

        // 일의 자리(1)부터 시작해서 자릿수를 높여가며 정렬
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortForRadix(arr, exp);
        }
    }

    // 기수 정렬에서 내부적으로 사용하는 안정적인 계수 정렬
    private static void countingSortForRadix(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10]; // 10진수 기준 버킷 (0-9)

        // 현재 자릿수(exp)의 숫자 빈도 계산
        for (int j : arr) {
            int digit = (j / exp) % 10;
            count[digit]++;
        }

        // count 배열을 누적 합으로 변환 (안정 정렬을 위해 위치 계산)
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // 뒤에서부터 순회하여 안정성(Stability) 유지
        for (int i = n - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % 10;
            output[count[digit] - 1] = arr[i];
            count[digit]--;
        }

        // 원본 배열에 복사
        System.arraycopy(output, 0, arr, 0, n);
    }

    public static void main(String[] args) {
        int[] testData = {170, 45, 75, 90, 802, 24, 2, 66};
        
        System.out.println("Original: " + Arrays.toString(testData));

        // 1. Array Counting Sort
        int[] result1 = countingSortArray(testData.clone());
        System.out.println("Array Counting Sort: " + Arrays.toString(result1));

        // 2. Map Counting Sort
        int[] result2 = countingSortMap(testData.clone());
        System.out.println("Map Counting Sort: " + Arrays.toString(result2));

        // 3. Radix Sort
        int[] result3 = testData.clone();
        radixSort(result3);
        System.out.println("Radix Sort: " + Arrays.toString(result3));
        
        // 데이터 범위가 넓은 경우 테스트
        int[] sparseData = {1, 1000000, 50, 1000000, 1};
        System.out.println("\nSparse Data: " + Arrays.toString(sparseData));
        System.out.println("Map Counting Sort (Space Efficient): " + Arrays.toString(countingSortMap(sparseData)));
    }
}
