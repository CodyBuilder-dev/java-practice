package StudyJava.Chap03;

import java.util.Scanner;

public class Q3_12 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        double a = sc.nextDouble();
        double b = sc.nextDouble();

        double max = a > b ? a : b; //삼향연산자 이용한 쉬운 비교
        System.out.println(max);

        //삼항연산자를 그냥 바로 출력하는 경우
        // 삼항연산자의 결과로 a가 반환되기 때문에 가능
        System.out.println( a > b ? a : b);

        // 실행문 안에 system out을 넣는 경우
        // 이렇게는 안되는듯 ㅎ
        //a > b ? System.out.println(a) : System.out.println(b);

    }
}