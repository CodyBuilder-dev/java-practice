package StudyJava.Chap02;

import java.util.Scanner;

public class Q2_8 {
    public static void main(String[] args){
        Scanner stdIn = new Scanner(System.in);

        double x = stdIn.nextDouble(), y = stdIn.nextDouble();

        System.out.println(x+y);
        System.out.println((x+y)/2);

    }
}