package StudyJava.Chap03;

import java.util.Scanner;

public class Q3_17 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int a = sc.nextInt();
        int b = sc.nextInt();
        
        if (a==b) System.out.println("result");
        else {
            int min,max;
            if (a<b) {
                min = a;
                max = b;
            }
            else {
                min = b;
                max = a;
            }
            System.out.println("result"+min+","+max);
        }
        //System.out.println(min+max); //block scope 바깥에서 변수를 사용할 수 없음
    }
}