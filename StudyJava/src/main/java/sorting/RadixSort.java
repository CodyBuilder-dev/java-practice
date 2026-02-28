package sorting;

/**
 * Implementation of Radix Sort algorithm.
 * O(d(n + R)) time complexity, where:
 *    d: the number of digits in the largest number in the array (passes)
 *    n: number of elements in the array
 *    R: the radix (base) of the numbers (e.g., 10 for decimal)
 * O(n + R) space complexity, where R is the number of buckets (radix)
 *    n: space for the temporary storage
 *    R: space for the buckets
 * Condition :
 * 1. Radix must be greater than 1.
 * 2. Arr must not be null.
 * 3. Arr must contain non-negative integers only.
 */
public class RadixSort {

  public static void main(String[] args) {
    int[] sizes = {1000, 5000, 10000, 20000};
    java.util.Random random = new java.util.Random();

    System.out.println("Radix Sort Time Complexity Test:");
    System.out.println("---------------------------------");

    for (int size : sizes) {
      int[] arr = new int[size];
      for (int i = 0; i < size; i++) {
        arr[i] = random.nextInt(size * 10);
      }

      long startTime = System.nanoTime();
      radixSort(arr, 10);
      long endTime = System.nanoTime();

      long duration = (endTime - startTime) / 1_000_000; // milliseconds
      boolean sorted = sorting.SortingComparison.isSorted(arr);
      System.out.printf("Array Size: %6d | Time Taken: %4d ms | Sorted: %s%n", size, duration, sorted);
    }
  }

  /**
   * Radix Sort implementation.
   *
   * 1. 최대 수를 구하여, 반복해야 하는 횟수를 구한다.
   * 2. 반복횟수만큼 각 자리수로 숫자들을 모은다.
   * 3. 모은 후 합친후 다시 반복한다.
   * 4. 최종적으로 나온 배열을 반환한다.
   * @param arr
   * @param radix
   */
  public static int[] radixSort(int[] arr, int radix) {
    int max = arr[0];
    for(int num : arr) {
      if(num > max) max = num;
    }

    int repeatCount = 1;
    while(max / radix > 0) {
      max /= radix;
      repeatCount++;
    }

    for(int n = 0; n < repeatCount; n++) {
      // 버킷 생성
      int[][] buckets = new int[radix][arr.length];
      int[] bucketSizes = new int[radix];

      // 각 자리수에 맞게 버킷에 숫자 넣기
      for(int num : arr) {
        int digit = geNthDigitFromRight(num, radix, n);
        buckets[digit][bucketSizes[digit]] = num;
        bucketSizes[digit]++;
      }

      // 버킷에서 다시 배열로 합치기
      int index = 0;
      for(int i = 0; i < radix; i++) {
        for(int j = 0; j < bucketSizes[i]; j++) {
          arr[index++] = buckets[i][j];
        }
      }
    }

    return arr;
  }

  public static int geNthDigitFromRight(int num, int radix, int n) {
    int divisor = 1;
    for(int i = 0; i < n; i++) {
      divisor *= radix;
    }
    return (num / divisor) % radix;
  }
}
