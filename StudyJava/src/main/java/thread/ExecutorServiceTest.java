package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceTest {
  public static void main(String[] args) throws Exception {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    Callable<String> callable = new Callable<>() {
      @Override
      public String call() throws InterruptedException {
        Thread.sleep(3000L);
        return "Thread: " + Thread.currentThread().getName();
      }
    };

    Runnable runnable = () -> {
      System.out.println("Thread: " + Thread.currentThread().getName());
    };


    // It takes 3 seconds by blocking(블로킹에 의해 3초 걸림)
    Future<String> future = executorService.submit(callable);

    System.out.println(future.get());

    executorService.submit(runnable);

    executorService.shutdown();
  }
}
