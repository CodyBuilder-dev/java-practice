package StudyJava.Chap04;


import java.util.Scanner;


public class Q4_5 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int no;

        // 입력값 검증
        do {
            no = sc.nextInt();
        }while(no <= 0);

        // 입력값 출력
        while (no >= 0){
            System.out.println(no--);    
        }
        System.out.println(no);
    }
}