import java.util.Scanner;
import java.util.InputMismatchException; // 예외처리를 위해 import 해줘야 함

class MulDiv1 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("x값 : "); int x = sc.nextInt();
        System.out.println("y값 : "); int y = sc.nextInt();

        System.out.println("x * y = "+(x*y));
        System.out.println("x / y = "+(x/y));
    }
}

class MulDiv2 {
    static int mul(int x,int y){
        return x*y;
    }
    static int div(int x,int y){
        return x/y;
    }
    
    static void muldiv(int x,int y) {
        System.out.println("x * y = "+ mul(x,y));
        System.out.println("x / y = "+ div(x,y));
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        try {//입력값에 대한 예외처리
            System.out.println("x값 : "); int x = sc.nextInt();
            System.out.println("y값 : "); int y = sc.nextInt();
            muldiv(x,y);
        }
        catch (InputMismatchException e) {
            System.out.println("입력값 오류 발생 : " + e);
            e.printStackTrace(); // 예외 전파과정을 표기
            System.out.println(e.getMessage()); // 메시지 표기
            System.out.println(e.getCause()); // 원인 표기
        }
        catch (ArithmeticException e) {
            System.out.println("산술 오류 발생 : " + e);
            e.printStackTrace();
            System.out.println(e.getMessage()); // 메시지 표기
            System.out.println(e.getCause()); // 원인 표기
        }
        finally {
            System.out.println("프로그램을 종료합니다.");
        }
    }
}