import java.util.Scanner;

class CompareString {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("문자열 s1:"); String s1 = sc.next();
        System.out.println("문자열 s2:"); String s2 = sc.next();

        System.out.println("동일성 비교"); // 문자열 인스턴스 자체의 동일성을 비교
        if(s1 == s2)
            System.out.println("s1 == s2입니다.");
        else
            System.out.println("s1 != s2입니다.");

        System.out.println("동등성 비교"); // 문자열 인스턴스 내부의 char[] 을 비교
        if(s1.equals(s2))
            System.out.println("s1과 s2의 내용이 같습니다.");
        else
            System.out.println("s1과 s2의 내용이 다릅니다.");
    }
}