package main.java.functional;

import java.util.function.Function;

public class UserFunction implements Function<Integer, String> {
    @Override
    public String apply(Integer i) {
        return "들어온 숫자는" + i + "입니다";
    }
}
