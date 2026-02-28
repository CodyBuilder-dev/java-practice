package StudyJava.Chap04;

import java.util.Scanner;


public class Q4_2 {
    public static void main(String[] args){
        Scanner stdin = new Scanner(System.in);

        int retry;

        do {
            System.out.print("양의 3자리 정숫값:");
            int n = stdin.nextInt();

            if(n >= 100 && n < 1000){
                System.out.println("이 값은 3자리 정수입니다.");
                retry = 1;
            }
            else{
                retry = 0;
                System.out.println("다시 입력하쇼");
            }

        } while( retry == 0);
            
    }
}