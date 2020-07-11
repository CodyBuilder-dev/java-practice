package StudyJava.Chap02;

import java.util.Random;

public class Q2_13 {
    public static void main(String[] args){
        Random rand = new Random();
        double r1 = rand.nextDouble();
        double r2 = 10 * rand.nextDouble();
        double r3 = -1 + 2*rand.nextDouble();

        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);
    }
}