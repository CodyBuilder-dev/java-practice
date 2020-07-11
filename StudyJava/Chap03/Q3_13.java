package StudyJava.Chap03;

import java.util.Scanner;

public class Q3_13 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int a = sc.nextInt();
        int b = sc.nextInt();

        //아래와 같이 단순히 사칙연산으로 처리할 경우, 매우매우 위험함
        // 반드시 overflow-safe한 + - * / 를 따로 정의해서 사용해야 함
        // int diff;
        // if (a >= b)
        //     diff = a - b;
        // else
        //     diff = b - a;

        //System.out.println(diff);
        System.out.println(a >= b ? a-b : b - a);
    }
}