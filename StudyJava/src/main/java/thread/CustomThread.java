package thread;

public class CustomThread extends Thread {

  public CustomThread() {
  }

  public CustomThread(Runnable target) {
    super(target);
  }


  public static void main(String[] args) {
    Thread t = new CustomThread();
    t.start(); // run()메서드를 오버라이딩 하지 않으면 아무것도 하지 않음

    Thread t2 = new CustomThread(new CustomRunnable());
    t2.start(); // runnable을 받으면 runnable의 동작을 수행
   }
}
