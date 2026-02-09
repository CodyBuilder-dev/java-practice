package math;

public class PositiveNumberSystem {
  public static int pow(int base, int exponent) throws IllegalArgumentException {
    if (exponent < 0) {
      throw new IllegalArgumentException("Exponent must be non-negative");
    }
    int result = 1;

    for(int i = 0; i < exponent; i++) {
      result = result * base;
      if(result < 0) {
        throw new IllegalArgumentException("Integer overflow occurred");
      }
    }
    return result;
  }

  public static int hexToDecimal(String hex) throws IllegalArgumentException {
    int decimal = 0;
    if(hex == null || hex.isEmpty()) {
      throw new IllegalArgumentException("Input hexadecimal string is null or empty");
    }
    if(!hex.startsWith("0x")) {
      throw new IllegalArgumentException("Input hexadecimal string must start with '0x'");
    }
    hex = hex.substring(2); // '0x' 접두사 제거
    if(hex.length() >= 8) {
      throw new IllegalArgumentException("Input hexadecimal string is too long");
    }
    int length = hex.length();
    for (int i = 0; i < length; i++) {
      char ch = hex.charAt(length - 1 - i);
      int value;
      if (ch >= '0' && ch <= '9') {
        value = ch - '0';
      } else if (ch >= 'A' && ch <= 'F') {
        value = ch - 'A' + 10;
      } else if (ch >= 'a' && ch <= 'f') {
        value = ch - 'a' + 10;
      } else {
        throw new IllegalArgumentException("Invalid hexadecimal character: " + ch);
      }
      decimal += value * pow(16, i);
    }
    return decimal;
  }

  public static int binaryToDecimal(String binary) throws IllegalArgumentException {
    int decimal = 0;
    if(binary == null || binary.isEmpty()) {
      throw new IllegalArgumentException("Input binary string is null or empty");
    }
    if(!binary.startsWith("0b")) {
      throw new IllegalArgumentException("Input binary string must start with '0b'");
    }
    binary = binary.substring(2); // '0b' 접두사 제거
    if(binary.length() > 31) {
      throw new IllegalArgumentException("Input binary string is too long");
    }
    int length = binary.length();
    for (int i = 0; i < length; i++) {
      char ch = binary.charAt(length - 1 - i);
      if (ch == '1') {
        decimal += pow(2, i);
      } else if (ch != '0') {
        throw new IllegalArgumentException("Invalid binary character: " + ch);
      }
    }
    return decimal;
  }

  public static void main(String[] args) {
    System.out.println(pow(2, 30));
    System.out.println(pow(16, 7));
    System.out.println(hexToDecimal("0x1A3F"));
    assert hexToDecimal("0x1A3F") == 6719;
    System.out.println(binaryToDecimal("0b1101"));
    assert binaryToDecimal("0b1101") == 13;
    System.out.println("All tests passed.");
  }
}
