package StudyJava.Chap02;

import java.util.Scanner;

public class Q2_14 {
    public static void main(String[] args){
        Scanner stdIn = new Scanner(System.in,"utf-8");

        // String lastName = stdIn.nextLine();
        // String firstName = stdIn.nextLine();
        String lastName = stdIn.next();
        String firstName = stdIn.next();

        System.out.print("안녕하세요." + lastName + firstName);
    }
}