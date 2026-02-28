package StudyJava.Chap02;

import java.util.Random;

class Q2_11 {
    public static void main(String[] args){

        Random rand = new Random();

        int n1 = 1 + rand.nextInt(9);
        int n2 = -1 - rand.nextInt(9);
        int n3 = 10 + rand.nextInt(90);

        System.out.println(n1);
        System.out.println(n2);
        System.out.println(n3);
    }
}