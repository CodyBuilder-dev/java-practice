package StudyJava.Chap04;

import java.util.Scanner;
import java.util.Random;

public class Q4_3 {
    public static void main(String[] args){
        Scanner stdin = new Scanner(System.in);
        Random rand = new Random();

        int answer = 10 + rand.nextInt(90);
        int n;
        do {
            System.out.println("맞춰보세요");
            n = stdin.nextInt();

            if (answer > n) {
                System.out.println("이 수보다 큰 수입니다.");
            }
            else if (answer < n){
                System.out.println("이 수보다 작은 수입니다.");
            }
        }while (answer != n);

        System.out.println("정답입니다");
    }
}