import java.util.Scanner;

class RangeError extends RuntimeException {
    //RuntimeException의 생성자에 에러메시지를 넣어서 상속
    RangeError(int n) { super("범위 밖 값 : "+n);} 
}

class ParameterRangeError extends RangeError {
    ParameterRangeError(int n){ super(n);}
}

class ResultRangeError extends RangeError {
    ResultRangeError(int n) { super(n); }
}

public class RangeErrorTester {
    static boolean isValid(int n) {
        return n >= 0 && n <= 9;
    }

    static int add(int a,int b) throws ParameterRangeError,ResultRangeError { 
        // 인자 하나하나에 대해서 isValid로 검사 후 throw
        if(!isValid(a)) throw new ParameterRangeError(a);
        if(!isValid(b)) throw new ParameterRangeError(b);

        int result = a + b;

        if(!isValid(result)) throw new ResultRangeError(result);
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("정수 : a"); int a = sc.nextInt();
        System.out.println("정수 : b"); int b = sc.nextInt();
        
        // System.out.println(add(a,b)); // add가 RuntimeException을 throws하므로, try 안에 안 넣어도 컴파일에러 없음
        try {
            System.out.println(add(a,b));
        }
        catch (ResultRangeError e) {
            System.out.println("계산 결과가 범위 밖입니다.\n" + e);
        }
        catch (ParameterRangeError e) {
            System.out.println("더하는 수가 범위 밖입니다.\n" + e);
        }
    }
}