package StudyJava.Chap11;

import java.util.Scanner; 
import static java.lang.System.out;
import static java.lang.Math.*; //온디맨드 정적 임포트

public class Q11_2 {
    public static void main(String[] args){
        Scanner stdin = new Scanner(System.in);
        double x = stdin.nextDouble();

        out.println("절대값" + abs(x));
        out.println("제곱근" + sqrt(x));
        out.println("넓이" + PI*x*x);
    }
}