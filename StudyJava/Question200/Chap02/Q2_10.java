package StudyJava.Chap02;

import java.util.Scanner;

public class Q2_10 {
    public static void main(String[] args){
        Scanner stdIn = new Scanner(System.in);

        System.out.print("구의 반지름을 입력하세요 : ");
        double radius = stdIn.nextDouble();
        final double PI = 3.1416;
        
        System.out.println(4*radius*radius*PI);
        System.out.println(4.0/3*radius*radius*radius*PI);
    }
}