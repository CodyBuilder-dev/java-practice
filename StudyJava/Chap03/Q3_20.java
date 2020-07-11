package StudyJava.Chap03;

import java.util.Random;

public class Q3_20 {
    public static void main(String[] args){
        Random rand = new Random();

        int x = rand.nextInt(3);
        String result_x;
        x = 0;
        switch(x){
            case 0 : result_x = "가위"; 
            case 1 : result_x = "바위"; break;
            case 2 : result_x = "보"; break;
            default : result_x = "그런건 없다";
        }
        System.out.println(result_x);
    }    
}