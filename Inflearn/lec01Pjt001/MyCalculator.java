public class MyCalculator {
    private Calc calc;
    private int a,b;

    public MyCalculator(int a,int b,Calc calc){
        this.a=a;
        this.b=b;
        this.calc = calc;
    }
    public void result() {
        this.calc.doCalc(this.a,this.b);
    }
}