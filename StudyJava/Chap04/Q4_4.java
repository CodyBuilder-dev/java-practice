package StudyJava.Chap04;

import java.util.Scanner;


public class Q4_4 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        
        int x = sc.nextInt(), y = sc.nextInt();

        if (x > y){
            int temp = x;
            x = y;
            y = temp;
        }

        if (y-x <= 1)
            System.out.println("");

        else{
            int result =x+1;
            do {
                System.out.println(result++);
            } while(result<y);
        }
    }
}