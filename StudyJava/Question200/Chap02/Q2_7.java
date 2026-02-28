package StudyJava.Chap02;

import java.util.Scanner;

public class Q2_7{
    public static void main(String[] args){
        Scanner stdIn = new Scanner(System.in);

        int x = stdIn.nextInt();

        System.out.println(x/10);
        System.out.println(x%10);
    }
}