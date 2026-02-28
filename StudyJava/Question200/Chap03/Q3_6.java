package StudyJava.Chap03;

import java.util.Scanner;

public class Q3_6 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        String sign_n;

        if (n == 1) {
            sign_n = "1";
        }
        else if (n == 2){
            sign_n = "2";
        }
        else if (n == 3) {
            sign_n = "3";
        }
        else 
            sign_n = "1도 2도 3도 아닌 수"
        ;

        String statement = "입력값은 "+sign_n+"입니다.";

        System.out.println(statement);

    }
}