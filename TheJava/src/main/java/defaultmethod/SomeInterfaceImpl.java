package main.java.defaultmethod;

public class SomeInterfaceImpl implements SomeInterface {
    @Override
    public void someFunction() {
        System.out.println("Implementation Complete!");
    }

    public static void main(String[] args){
        SomeInterfaceImpl me = new SomeInterfaceImpl();
        me.someFunction();
        me.secondFunction();
        me.toString();
    }
}