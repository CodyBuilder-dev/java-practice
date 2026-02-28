package math;

public class CheckTwosExponent {

  public static void main(String[] args) {
    int number = 16;
    boolean isPowerOfTwo = isPowerOfTwo2(number);
    System.out.println(number + " is a power of two: " + isPowerOfTwo);
  }

  public static boolean isPowerOfTwo(int number) {
    return (number & (number - 1)) == 0;
  }

  public static boolean isPowerOfTwo2(int number) {
    return (number & (number - 1)) == 0 && number != 0;
  }

  public static boolean isPowerOfTwo3(int number) {
    return Integer.bitCount(number) == 1 && number > 0;
  }

  public static boolean isPowerOfTwo4(int number) {
    return number > 0 && (number & (number - 1)) == 0;
  }

  public static boolean isPowerOfTwo6(int number) {
    return number > 0 && Integer.numberOfTrailingZeros(number) == Integer.numberOfLeadingZeros(number) - 1;
  }

  public static boolean isPowerOfTwo7(int number) {
    return number > 0 && (number & -number) == number;
  }

  public static boolean isPowerOfTwo8(int number) {
    return number > 0 && Math.log(number) / Math.log(2) % 1 == 0;
  }

  public static boolean isPowerOfTwo9(int number) {
    if(number <= 0)
      return false;
    while (number % 2 == 0) {
      number /= 2;
    }
    return number == 1;
  }

  public static boolean isPowerOfTwo10(int number) {
    if(number <= 0)
      return false;
    int count = 0;
    while (number > 0) {
      if( (number & 1) == 1)
      {
        count++;
        if(count > 1)
          return false;
      }
      number >>= 1;
    }
    return true;
  }
}
