package datatype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StrCompareBenchmark {

  interface StringComparator {
    String name();
    boolean compare(String a, String b);
  }

  static class ResultSummary {
    String method;
    int length;
    String pattern;
    int iterations;
    double meanNsPerOp;
    double stddevNsPerOp;
    double opsPerSec;

    @Override
    public String toString() {
      return String.format(
          "%s | len=%d | pattern=%s | iters=%d | mean=%.1f ns/op | std=%.1f ns | ops/sec=%.1f",
          method, length, pattern, iterations, meanNsPerOp, stddevNsPerOp, opsPerSec);
    }
  }

  // --- 비교 메서드들 ---
  /**
   * Objects.equals 사용
   * @see Objects#equals(Object, Object)
   */
  static StringComparator objectsEquals = new StringComparator() {
    public String name() { return "Objects.equals"; }
    public boolean compare(String a, String b) { return Objects.equals(a, b); }
  };

  /**
   * String.equals 사용
   * @see String#equals(Object)
   */
  static StringComparator stringEquals = new StringComparator() {
    public String name() { return "String.equals"; }
    public boolean compare(String a, String b) {
      if (a == null) return b == null; // avoid NPE
      return a.equals(b);
    }
  };

  /**
   * 참조 동일성 비교 (==)
   * @see String
   */
  static StringComparator refEquals = new StringComparator() {
    public String name() { return "Reference=="; }
    public boolean compare(String a, String b) { return a == b; }
  };

  /**
   * 수동 문자 루프 비교
   * @see String#charAt(int)
   */
  static StringComparator charLoop = new StringComparator() {
    public String name() { return "ManualCharLoop"; }
    public boolean compare(String a, String b) {
      if (a == null && b == null) return true;
      if (a == null || b == null) return false;
      int n = a.length();
      if (n != b.length()) return false;
      for (int i = 0; i < n; i++) {
        if (a.charAt(i) != b.charAt(i)) return false;
      }
      return true;
    }
  };

  /**
   * Arrays.equals 사용 (toCharArray)
   * @see Arrays#equals(char[], char[])
   */
  static StringComparator arraysEquals = new StringComparator() {
    public String name() { return "Arrays.equals(toCharArray)"; }
    public boolean compare(String a, String b) {
      if (a == null && b == null) return true;
      if (a == null || b == null) return false;
      return Arrays.equals(a.toCharArray(), b.toCharArray());
    }
  };

  /**
   * compareTo == 0 사용
   * @see String#compareTo(String)
   */
  static StringComparator compareToEq = new StringComparator() {
    public String name() { return "compareTo==0"; }
    public boolean compare(String a, String b) {
      if (a == null && b == null) return true;
      if (a == null || b == null) return false;
      return a.compareTo(b) == 0;
    }
  };

  /**
   * regionMatches 사용
   * @see String#regionMatches(int, String, int, int)
   */
  static StringComparator regionMatches = new StringComparator() {
    public String name() { return "regionMatches"; }
    public boolean compare(String a, String b) {
      if (a == null && b == null) return true;
      if (a == null || b == null) return false;
      if (a.length() != b.length()) return false;
      return a.regionMatches(0, b, 0, a.length());
    }
  };

  /**
   * 해시코드만 비교 (충돌 가능성 있음)
   *
   * @see String#hashCode()
   */
  static StringComparator hashCodeOnly = new StringComparator() {
    public String name() { return "hashCodeOnly"; }
    public boolean compare(String a, String b) {
      if (a == null && b == null) return true;
      if (a == null || b == null) return false;
      return a.hashCode() == b.hashCode();
    }
  };

  /**
   * 해시코드 비교 후 동일할 때만 equals 호출 (충돌 가능성 대비)
   *
   * @see String#hashCode()
   * @see String#equals(Object)
   */
  static StringComparator hashThenEquals = new StringComparator() {
    public String name() { return "hashThenEquals"; }
    public boolean compare(String a, String b) {
      if (a == null && b == null) return true;
      if (a == null || b == null) return false;
      int ha = a.hashCode();
      int hb = b.hashCode();
      if (ha != hb) return false;
      return a.equals(b);
    }
  };

  // --- 문자열 생성 유틸 ---
  static class Pair { final String a; final String b; Pair(String a, String b) { this.a=a; this.b=b; } }

  enum Pattern { IDENTICAL_SAME_INSTANCE, IDENTICAL_DIFF_INSTANCE, DIFF_FIRST_CHAR, DIFF_LAST_CHAR, DIFF_AFTER_PREFIX, COMPLETELY_DIFFERENT }

  static Pair makePair(int length, Pattern pattern) {
    if (length < 0) length = 0;
    switch (pattern) {
      case IDENTICAL_SAME_INSTANCE:
        String s = makeBaseString(length, 'a');
        return new Pair(s, s);
      case IDENTICAL_DIFF_INSTANCE:
        return new Pair(makeBaseString(length, 'a'), new String(makeBaseString(length, 'a')));
      case DIFF_FIRST_CHAR:
        if (length == 0) return new Pair("", "");
        String a1 = makeBaseString(length, 'a');
        String b1 = 'b' + a1.substring(1);
        return new Pair(a1, b1);
      case DIFF_LAST_CHAR:
        if (length == 0) return new Pair("", "");
        String a2 = makeBaseString(length, 'a');
        char[] arr = a2.toCharArray(); arr[length-1] = 'b';
        return new Pair(a2, new String(arr));
      case DIFF_AFTER_PREFIX:
        if (length < 4) return new Pair(makeBaseString(length, 'a'), makeBaseString(length, 'b'));
        int pref = length/2;
        StringBuilder p = new StringBuilder();
        for (int i = 0; i < pref; i++) p.append('a');
        String suffixA = repeatChar('a', length - pref);
        String suffixB = repeatChar('b', length - pref);
        return new Pair(p.toString() + suffixA, p.toString() + suffixB);
      case COMPLETELY_DIFFERENT:
      default:
        return new Pair(makeBaseString(length, 'a'), makeBaseString(length, 'b'));
    }
  }

  static String makeBaseString(int length, char ch) {
    return repeatChar(ch, length);
  }

  static String repeatChar(char ch, int len) {
    if (len <= 0) return "";
    char[] a = new char[len];
    Arrays.fill(a, ch);
    return new String(a);
  }

  // --- 벤치마크 실행 ---
  public static void main(String[] args) throws Exception {
    List<StringComparator> methods = new ArrayList<>();
    methods.add(objectsEquals);
    methods.add(stringEquals);
    methods.add(refEquals);
    methods.add(charLoop);
    methods.add(arraysEquals);
    methods.add(compareToEq);
    methods.add(regionMatches);
    methods.add(hashThenEquals);
    methods.add(hashCodeOnly);

    int[] lengths = new int[] {8, 128, 16384};
    Pattern[] patterns = new Pattern[] { Pattern.IDENTICAL_SAME_INSTANCE, Pattern.IDENTICAL_DIFF_INSTANCE, Pattern.DIFF_FIRST_CHAR, Pattern.DIFF_AFTER_PREFIX, Pattern.DIFF_LAST_CHAR, Pattern.COMPLETELY_DIFFERENT };

    // iterations by length
    int itersShort = 100_000;
    int itersMid = 10_000;
    int itersLong = 1_000;

    int warmupRounds = 5;
    int measureRounds = 10;

    List<ResultSummary> allResults = new ArrayList<>();

    System.out.println("Starting benchmark. Warmup rounds="+warmupRounds+" measure rounds="+measureRounds);

    for (int len : lengths) {
      int iters = (len <= 16) ? itersShort : (len <= 1024 ? itersMid : itersLong);
      for (Pattern pat : patterns) {
        Pair p = makePair(len, pat);
        for (StringComparator method : methods) {
          // Warmup
          for (int w = 0; w < warmupRounds; w++) {
            runLoop(method, p.a, p.b, Math.max(1, iters/10));
          }

          // Measure rounds
          double[] samplesNsPerOp = new double[measureRounds];
          int dummy=0;
          for (int r = 0; r < measureRounds; r++) {
            long start = System.nanoTime();
            dummy += runLoop(method, p.a, p.b, iters);
            long end = System.nanoTime();
            long elapsed = end - start;
            double nsPerOp = (double) elapsed / (double) iters;
            samplesNsPerOp[r] = nsPerOp;
            // small pause to let JVM breathe
            try { Thread.sleep(20); } catch (InterruptedException ignore) {}
          }

          // aggregate
          double mean = mean(samplesNsPerOp);
          double std = stddev(samplesNsPerOp, mean);
          double opsPerSec = 1_000_000_000.0 / mean;

          ResultSummary res = new ResultSummary();
          res.method = method.name();
          res.length = len;
          res.pattern = pat.name();
          res.iterations = iters;
          res.meanNsPerOp = mean;
          res.stddevNsPerOp = std;
          res.opsPerSec = opsPerSec;
          allResults.add(res);

          System.out.println(res.toString()+" (dummy="+dummy+")");
        }
      }
    }

    System.out.println("\nBenchmark complete. Summaries:");
    for (ResultSummary s : allResults) {
      System.out.println(s.toString());
    }
    System.out.println("Note: hashCodeOnly can have false positives due to collisions; treat as heuristic.");
  }

  // runLoop returns number of times comparator returned true (accumulated) to avoid dead-code elimination
  static int runLoop(StringComparator method, String a, String b, int iterations) {
    int count = 0;
    for (int i = 0; i < iterations; i++) {
      if (method.compare(a, b)) count++;
    }
    return count;
  }

  static double mean(double[] arr) {
    double s = 0.0;
    for (double d : arr) s += d;
    return s / arr.length;
  }

  static double stddev(double[] arr, double mean) {
    double s = 0.0;
    for (double d : arr) {
      double diff = d - mean;
      s += diff * diff;
    }
    return Math.sqrt(s / arr.length);
  }
}

