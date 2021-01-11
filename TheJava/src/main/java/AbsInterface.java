package main.java;

public interface AbsInterface {
    abstract void doTask();

    static void printName() {
        System.out.println("이름은 김명수입니다");
    }
}
