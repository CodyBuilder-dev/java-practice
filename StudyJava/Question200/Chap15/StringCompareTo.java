/** 문자열의 대소관계 판정
 * 대소의 기준 : 사전 순서
 * 작다 = 사전순으로 앞에 위치한다 = 음수
 * 크다 = 사전순으로 뒤에 위치한다 = 양수
 */
import java.util.Scanner;

class StringCompareTo {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("문자열 s1:"); String s1 = sc.next();
        System.out.println("문자열 s2:"); String s2 = sc.next();

        int balance = s1.compareTo(s2);
        if (balance < 0)
            System.out.println("s1이 작다");
        else if (balance > 0)
            System.out.println("s2가 작다");
        else
            System.out.println("s1과 s2의 내용이 같다(동등하다)");
            System.out.println("주의!"+((s1 == s2)?"동일함":"동일하지 않음"));
    }
}