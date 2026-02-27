package staticKeyword;

import java.util.ArrayList;
import java.util.List;

public class Outer {
  int[] oarray;

  Outer(int[] oarray) {
    this.oarray = oarray;
  }

  class NonStaticInner {
    int[] nsi;

    NonStaticInner(int[] nsi) {
      this.nsi = nsi;
    }
  }

  static class StaticInner {
    int[] si;

    StaticInner(int[] si) {
      this.si = si;
    }
  }

  public static void main(String[] args) {
    int maxSize = 100_000_000;
    List<Outer.StaticInner> staticInners = new ArrayList<>();
    int round = 1000;

    for(int i = 0; i < round; i++) {
      staticInners.add(new Outer.StaticInner(new int[1]));
    }

    System.out.println("static inner end");



    List<NonStaticInner> nonStaticInners = new ArrayList<>();

    for(int i = 0; i < round; i++) {
      nonStaticInners.add(new Outer(new int[maxSize]).new NonStaticInner(new int[1]));
    }

    System.out.println("nonstatic inner end");
  }
}
