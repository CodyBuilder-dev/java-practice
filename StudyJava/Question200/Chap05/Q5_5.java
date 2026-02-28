package StudyJava.Chap05;

import java.util.Scanner;

public class Q5_5 {
    public static void main(String[] args){
        Scanner stdin = new Scanner(System.in);

        System.out.println("정수 3개 입력바람");
        int x = stdin.nextInt();
        int y = stdin.nextInt();
        int z = stdin.nextInt();

        // 단순 출력 : 정수간의 연산은 정수로 나옴
        System.out.println("평균 :" + (x+y+z)/3);

        // 단순 출력 : 정수와 실수간의 연산은 암묵적 형변환 통해 실수로 나옴
        System.out.println("평균 :" + (x+y+z)/3.0);

        // 변수에 저장 : 실수형 변수에 정수간 연산을 넣으면 연산 후 형변환
        float f = (x+y+z)/3;
        double d = (x+y+z)/3;
        System.out.println("평균 :"+d);
        System.out.printf("평균 : %f",d);
        System.out.println();
        System.out.println("평균 :"+f);
        System.out.printf("평균 : %f",f);

    }
}