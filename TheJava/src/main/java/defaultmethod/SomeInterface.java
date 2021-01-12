package main.java.defaultmethod;

public interface SomeInterface {
    void someFunction();

    // 메서드 추가시 에러 발생!
    //void secondFunction();

    /*
    * @impleSpec 2를 출력한다.
     */
    default void secondFunction() {
        System.out.println(2);
    }

    // 아래의 toString은 컴파일 에러가 발생한다
    // Object.toString으로 제공되는 메소드이기 때문에 default로 재정의할수 없다
//    default String toString() {
//        System.out.println("나는 toString을 재정의하겠다");
//        return "크하하";
//    }
}