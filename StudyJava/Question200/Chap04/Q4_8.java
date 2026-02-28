package StudyJava.Chap04;

import java.util.Scanner;

public class Q4_8 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("찍을 별 개수 입력");
        int no = sc.nextInt();
        
        int i = 0;
        if( no > 0 ){
            while(i < no){
                if (i%2 == 0)
                    System.out.print('*');
                else
                    System.out.print('+');
                i++;
            }
        }
        System.out.println();
    }
}