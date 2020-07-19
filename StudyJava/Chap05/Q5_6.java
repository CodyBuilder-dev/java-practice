package StudyJava.Chap05;

import java.util.Scanner;

public class Q5_6 {
    public static void main(String[] args){
        Scanner stdin = new Scanner(System.in);

        System.out.println("정수 3개 입력바람");
        int x = stdin.nextInt();
        int y = stdin.nextInt();
        int z = stdin.nextInt();

        // 연산전 cast : 연산 전에 cast하므로 암묵적 형변환 통해 실수로 나옴
        System.out.println("평균 :" + (x+y+z)/(float)3);
        System.out.println("평균 :" + (float)(x+y+z)/3);

        System.out.println("평균 :" + (x+y+z)/(double)3);
        System.out.println("평균 :" + (double)(x+y+z)/3);

        // 연산후 cast : 정수간 연산의 결과는 정수이므로 cast해도 .0
        System.out.println("평균 :" + (float) ((x+y+z)/3));


        System.out.println("----------변수에 저장-----------");
        // 변수에 저장 : 실수형 변수에 정수간 연산을 넣으면 연산 후 형변환
        float f = (float)(x+y+z)/3;
        double d = (float)(x+y+z)/3;
        System.out.println("평균 :"+d);
        System.out.printf("평균 : %f",d);
        System.out.println();
        System.out.println("평균 :"+f);
        System.out.printf("평균 : %f",f);

    }
}