package StudyJava.Chap04;

import java.util.Scanner;

public class Q4_7 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("찍을 별 개수 입력");
        int no = sc.nextInt();
        
        if( no > 0 ){
            while(no > 0){
                System.out.print('*');
                no--;
            }
        }
    }
}