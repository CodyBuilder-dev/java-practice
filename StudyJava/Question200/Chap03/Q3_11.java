package StudyJava.Chap03;

import java.util.Scanner;

public class Q3_11 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int score = sc.nextInt();

        if (score < 0)
            System.out.println("잘못된 점수입니다.");
        else {
            if (score <= 59)
                System.out.println("가");
            else if(score <= 69)
                System.out.println("양");
            else if(score <= 79)
                System.out.println("미");
            else if(score <= 89)
                System.out.println("우");
            else if(score <= 100)
                System.out.println("수");
            else 
                System.out.println("잘못된 점수입니다.");
        }
    }
}