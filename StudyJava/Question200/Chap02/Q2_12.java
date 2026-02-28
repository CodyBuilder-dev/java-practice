package StudyJava.Chap02;

import java.util.Random;
import java.util.Scanner;

public class Q2_12 {
    public static void main(String[] args){
        Random rand = new Random();
        Scanner stdIn = new Scanner(System.in);

        int x = stdIn.nextInt();
        int rand_x = x-5 + rand.nextInt(11);

        System.out.println(rand_x);

    }

}