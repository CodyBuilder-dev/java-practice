package StudyJava.Chap03;

import java.util.Scanner;

public class Q3_5 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        String sign_n;

        if (n > 0) {
            sign_n = "양수";
        }
        else if (n <0){
            sign_n = "음수";
        }
        else {
            sign_n = "0";
        }
        String statement = "입력값은 "+sign_n+"입니다.";

        System.out.println(statement);

    }
}