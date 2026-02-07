package searching;

public class BinarySearch {
    public static int binarySearch(int[] arr, int target) {
      int left = 0;
      int right = arr.length - 1;
      while(left <= right) {
        int min = (left + right) / 2;
        if(arr[min] == target) {
          return min;
        } else if(arr[min] < target) {
          left = min + 1;
        } else {
          right = min - 1;
        }
      }
      return -1;
    }

    public int findClosest(int[] arr, int target) {
      int left = 0;
      int right = arr.length - 1;
      while(left < right) {
        int mid = (left + right) / 2;
        if(arr[mid] < target) {
          left = mid + 1;
        } else {
          right = mid - 1;
        }
      }

      // left is the smallest number greater than or equal to target
      if(left == 0) {
        return arr[0];
      }
      if(Math.abs(arr[left] - target) < Math.abs(arr[left - 1] - target)) {
        return arr[left];
      } else {
        return arr[left - 1];
      }
    }

    public static void main(String[] args) {
      int[] arr = {1, 3, 5, 7, 9, 11, 13, 15, 18, 20};
      int target = 8;
      int result = binarySearch(arr, target);
      if (result != -1) {
        System.out.println("Element found at index: " + result);
      } else {
        System.out.println("Element not found in the array.");
      }

      target = 9;
      result = binarySearch(arr, target);
      if (result != -1) {
        System.out.println("Element found at index: " + result);
      } else {
        System.out.println("Element not found in the array.");
      }

      target = 1;
      result = binarySearch(arr, target);
      if (result != -1) {
        System.out.println("Element found at index: " + result);
      } else {
        System.out.println("Element not found in the array.");
      }

      target = 15;
      result = binarySearch(arr, target);
      if (result != -1) {
        System.out.println("Element found at index: " + result);
      } else {
        System.out.println("Element not found in the array.");
      }

      BinarySearch bs = new BinarySearch();
      target = 4;
      int closest = bs.findClosest(arr, target);
      System.out.println("Closest element to " + target + " is: " + closest);

      target = 8;
      closest = bs.findClosest(arr, target);
      System.out.println("Closest element to " + target + " is: " + closest);

      target = 9;
      closest = bs.findClosest(arr, target);
      System.out.println("Closest element to " + target + " is: " + closest);

      target = 2;
      closest = bs.findClosest(arr, target);
      System.out.println("Closest element to " + target + " is: " + closest);

      target = 15;
      closest = bs.findClosest(arr, target);
      System.out.println("Closest element to " + target + " is: " + closest);

      target = 16;
      closest = bs.findClosest(arr, target);
      System.out.println("Closest element to " + target + " is: " + closest);

      target = 17;
      closest = bs.findClosest(arr, target);
      System.out.println("Closest element to " + target + " is: " + closest);

      target = -1;
      closest = bs.findClosest(arr, target);
      System.out.println("Closest element to " + target + " is: " + closest);

    }
}
