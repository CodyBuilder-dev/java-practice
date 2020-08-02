import java.util.Scanner;
import java.util.regex.Pattern;

class SearchString {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("문자열 s1:"); String s1 = sc.next();
        System.out.println("문자열 s2:"); String s2 = sc.next();

        int idx = s1.indexOf(s2);

        if (idx == -1) System.out.println("s1안에 s2가 포함되어 있지 않습니다.");
        else {
            System.out.println(s1);
            for (int i = 0 ; i<idx; ++i){
                if (isHangul(Character.toString(s1.charAt(i)))) System.out.print("  ");
                else System.out.print(" ");
            }
            System.out.print(s2);
        }
    }

    public static boolean isHangul(String ch){
        return Pattern.matches("[가-힣]",ch);

    }
}