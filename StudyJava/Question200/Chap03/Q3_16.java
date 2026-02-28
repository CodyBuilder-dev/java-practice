package StudyJava.Chap03;


import java.util.Scanner;

public class Q3_16 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int a = sc.nextInt();
        int b = sc.nextInt();
        int c = sc.nextInt();

        //중앙값을 찾아보자
        // a -> b a -> c b -> c
        int mid; 
        if (a >= b) {
            if (a >= c)
                mid = (b >= c) ? b : c;
            else 
                mid = a;
        }
        else {
            if (b >= c)
                mid = (a >= c) ? a : c;
            else
                mid = b;
        }

        System.out.println(mid);
    }
}