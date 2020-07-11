package StudyJava.Chap02;

import java.util.Scanner;

public class Q2_9 {
    public static void main(String[] args){
        Scanner stdIn = new Scanner(System.in);

        double height = stdIn.nextDouble();
        double bottom = stdIn.nextDouble();

        System.out.println(height*bottom/2);

    }
}