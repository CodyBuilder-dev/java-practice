package StudyJava.Chap04;

import java.util.Scanner;

class SignRepeat {
    public static void main(String[] args){
        Scanner stdin = new Scanner(System.in);

        int retry;

        do {
            System.out.print("정숫값:");
            int n = stdin.nextInt();

            if(n > 0)
                System.out.println("이 값은 양수입니다.");
            else if(n < 0)
                System.out.println("이 값은 음수입니다.");
            else
                System.out.println("이 값은 0입니다.");
            
            do {
                System.out.println("한번더? 1- Yes / 0- No");
                retry = stdin.nextInt();

            }while(retry != 0 && retry != 1);
            

        }while (retry == 1);
    }
}