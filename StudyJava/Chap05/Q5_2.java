package StudyJava.Chap05;

import java.util.Scanner;

public class Q5_2 {
    public static void main(String[] args){
        Scanner stdin = new Scanner(System.in);

        int x = stdin.nextInt();
        
        System.out.printf("10진수로 %d\n",x);
        System.out.printf("8진수로 %o\n",x);
        System.out.printf("16진수로 %x\n",x);
        
        System.out.printf("4자리 10진수로 %04d\n",x);
        System.out.printf("5자리 8진수로 %05o\n",x);
        System.out.printf("6자리 16진수로 %06x\n",x);
        
        System.out.printf("%%");
    }
}