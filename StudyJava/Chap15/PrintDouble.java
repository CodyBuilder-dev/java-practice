/**
 * 부동소수점을 문자열로 출력하는 방법
 */

 import java.util.Scanner;

 class PrintDouble {
    static void printDouble(double x, int p, int w){
        // 중첩 format문 ㅋㅋ
        System.out.printf(String.format("%%%d.%df",w,p),x);
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("실수값:");
        double x = sc.nextDouble();

        System.out.println("전체 자리수:");
        int w = sc.nextInt();

        System.out.println("소수점 이하 자리수:");
        int p = sc.nextInt();

        printDouble(x,p,w);


    }
 }