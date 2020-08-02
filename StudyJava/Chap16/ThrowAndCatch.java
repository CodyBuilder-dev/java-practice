import java.util.Scanner;

class ThrowAndCatch {
    static void check(int sw) throws Exception {
        switch (sw) {
            case 1:throw new Exception("검사 예외 발생!!");
            case 2:throw new RuntimeException("비검사 예외 발생!!");
            //case 3:throw new ThrowAndCatch(); //컴파일에러 : Throwable의 하위 클래스만 throw가능
        }
    }

    //static void test(int sw) { //컴파일에러 : check()가 throws이므로 test도 throws 필요
    static void test(int sw) throws Exception {
        check(sw); //
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("sw : ");
        int sw = sc.nextInt();

        //test(sw); //컴파일에러 : test가 Exception을 throws 이므로, try문 안에서만 실행 가능
        try{
            test(sw); 
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}