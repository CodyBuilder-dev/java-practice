package StudyJava.Chap03;

import java.util.Scanner;

public class Q3_3 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int a = sc.nextInt();
        int b = sc.nextInt();
        
        String c;
        
        if ( a % b == 0) 
            c = "약수이";
        else 
            c = "약수가 아니";
            
        String statement = b +"은(는) "+a+"의 "+c+"다.";
        System.out.println(statement);
    }
}