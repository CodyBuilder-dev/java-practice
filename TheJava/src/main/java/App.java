package main.java;


import main.java.bytecode.Hat;
import main.java.functional.AbsInterface;
import main.java.functional.AbsInterfaceCalc;
import main.java.functional.UserFunction;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.named;

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

    public void stream() {
        List<String> names = new ArrayList<>();
        names.add("apple");
        names.add("banana");
        names.add("kiwi");
        names.add("john-maynard-keynes");

        //stream 사용

        // 중개 오퍼레이션만 사용할 경우 함수가 실행되지 않음
//        names.stream().map(s->{
//            System.out.println(s);
//            return s.toUpperCase();
//        });

        List<String> collectedNames = names.stream().map(s->{
            System.out.println(s);
            return s.toUpperCase();
        }).collect(Collectors.toList());

        System.out.println(collectedNames);

        names.forEach(System.out::println);
    }

    public void magician() {
//        new ByteBuddy().redefine(Hat.class)
//                .method(named("pullOut")).intercept(FixedValue.value("Rabbit!"))
//                .make().saveIn();

        System.out.println(new Hat().pullOut());
    }

    public static void main(String[] args){
        App app = new App();
//        app.functional();
//        app.datetime();
//        app.stream();
        app.magician();
    }
}
