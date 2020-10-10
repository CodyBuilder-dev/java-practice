
public class MainClass {
    public static void main(String[] args) {
        MyCalculator mycalc;

        mycalc = new MyCalculator(10,5,new CalcAdd());
        mycalc.result();

        mycalc = new MyCalculator(10,5,new CalcSub());
        mycalc.result();

    }
}