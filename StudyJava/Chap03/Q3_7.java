package StudyJava.Chap03;

import java.util.Scanner;

public class Q3_7 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int a = sc.nextInt();
        int b = sc.nextInt();
        // double a = sc.nextDouble();
        // double b = sc.nextDouble();

        
        String sign_diff;
        
        /*아래와 같이 작성할 경우, 예외가 발생할 수 있다.
        어디서 예외가 생기는지는 잘 생각해 볼 것
        */
        double diff = a - b;
        if (diff > 0)
            sign_diff = "크";
        else if (diff < 0)
            sign_diff = "작";
        else 
            sign_diff = "같";
        //정답 : a-b라는 새로운 변수가 int의 범위를 벗어나는 over/underflow 발생 가능
        String statement = a + "가 " + b + "보다 " + sign_diff +"다";
        System.out.println(statement);
    }
}