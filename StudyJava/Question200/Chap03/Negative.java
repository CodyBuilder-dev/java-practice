package StudyJava.Chap03;

import java.util.Scanner;

public class Negative {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int x = sc.nextInt();

        if (x < 0 )
            System.out.println("이 값은 음의 값입니다.");
    }
}