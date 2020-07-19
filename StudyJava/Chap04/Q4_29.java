package StudyJava.Chap04;

import java.util.Scanner;

import javax.lang.model.util.ElementScanner6;

public class Q4_29 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        
        System.out.println("정수를 더합니다");
        int total = 0;
        
        Outer:
        for(int i = 1; i<=10;++i){
            System.out.println(i+"번째 그룹");
            for(int j = 1; j<=5;++j){
                int num = sc.nextInt();
                if(num == 99999)
                    break Outer;
                else if (num == 88888)
                    continue Outer;
                else
                    total += num;
            }
        }
    }
}