package main.java;

import main.java.functional.AbsInterface;
import main.java.functional.AbsInterfaceCalc;
import main.java.functional.UserFunction;

import java.time.*;
import java.util.function.Function;

public class App {
    public void functional() {
        //        AbsInterface runSomething = new AbsInterface() {
//            @Override
//            public void doTask() {
//                System.out.println("안녕하세요!");
//            }
//        };

        AbsInterface runSomething = () -> {
            System.out.println("안녕못한데?");
        };

        runSomething.doTask();
        // 아래 라인은 에러 발생
        //runSomething.printName();
        AbsInterface.printName();

        AbsInterfaceCalc calcSomething = (a) -> {
            return a+1;
        };

        System.out.println(calcSomething.calcTask(3));
        System.out.println(calcSomething.calcTask(3));

        // 사용자 함수 클래스의 인스턴스를 생성해 사용
        UserFunction f = new UserFunction();
        System.out.println(f.apply(5));

        // 익명클래스로 바로 사용
        Function<Integer, String> userFunction = (i) -> {
            return "입력된 숫자는" + i +"인데 이건 익명함수로 처리했습니다";
        };

        System.out.println(userFunction.apply(5));

        // 함수끼리 조합도 가능
        Function<Integer, Integer> add10 = (i) -> i+10;
        Function<Integer, Integer> mult10 = (i) -> i*10;

        System.out.println(mult10.apply(add10.apply(10)));

        Function<Integer, Integer> higherFunction = mult10.compose(add10);
        System.out.println(higherFunction.apply(5));

    }

    public void datetime() {
        // 컴퓨터용 시간
        Instant instant = Instant.now();
        System.out.println(instant); // Human Time UTC
        ZoneId zone = ZoneId.systemDefault();
        System.out.println(zone);
        ZonedDateTime zonedDateTime = instant.atZone(zone);

        System.out.println(zonedDateTime);

        // 휴먼옹 시간
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        LocalDateTime birthday = LocalDateTime.of(1993, 8, 16,0,0,0);
        System.out.println(birthday);
    }

    public static void main(String[] args){
        App app = new App();
//        app.functional();
        app.datetime();
    }
}
