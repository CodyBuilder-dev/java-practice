package thread;

import annotations.Main;


public class CustomThread implements Runnable {
  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName());
  }

  @Main
  public static void main() {
    Thread thread = new Thread(new CustomThread());

    Thread thread1 = new Thread(new CustomThread());
    Thread thread2 = new Thread(new CustomThread());
    Thread thread3 = new Thread(new CustomThread());
    Thread thread4 = new Thread(new CustomThread());
    Thread thread5 = new Thread(new CustomThread());

    // run() 호출은 스레드를 생성하지 않는다.
    thread.run();

    // start()를 2회 호출하면 Exception 발생
    thread.start();
    try {
      thread.start();
    } catch (IllegalThreadStateException e) {
      System.out.println("thread.start() called twice.");
    }


    thread1.start();
    thread2.start();
    thread3.start();
    thread4.start();
    thread5.start();
  }
}