package thread;

import annotations.Main;

public class ThreadSafeQueue {
  String[] array = new String[10];

  public synchronized String pop(){
    try {
      if(array[0] != null){
        String result = array[0];
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + " pop ->> "+result);
        Thread.sleep(1000);
        for(int i=0; i< array.length-1; i++){
          array[i] = array[i+1];
        }
        return result;
      }
    } catch (InterruptedException e) {
    }
    return null;
  }

  public String peek() {
    return array[0];
  }

  public void push(int value) {
    for(int i=0; i< array.length; i++){
      if(array[i] == null){
        array[i] = String.valueOf(value);
        break;
      }
    }
  }

  public String toString() {
    return String.join(",", array);
  }

  @Main
  public static void main(String[] args) {
    ThreadSafeQueue queue = new ThreadSafeQueue();
    queue.push(10);
    queue.push(20);
    queue.push(30);

    Thread[] threads = new Thread[1000];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(() -> {
        for (int j = 0; j < 10; j++) {
          queue.pop();
        }
      });
      threads[i].start();
    }
  }
}
