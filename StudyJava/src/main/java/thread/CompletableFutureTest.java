package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest {

  public static void main(String[] args) throws Exception {
      withDefaultExecutor();
      withCustomExecutor();

  }

  private static void withDefaultExecutor() throws InterruptedException, ExecutionException {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      System.out.println("Thread: " + Thread.currentThread().getName());
    });

    future.get();
    System.out.println("Thread: " + Thread.currentThread().getName());

    CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(3000L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return "Thread: " + Thread.currentThread().getName();
    });
    System.out.println(result.get());
  }

  private static void withCustomExecutor() throws InterruptedException, ExecutionException {
    ExecutorService executorService = Executors.newFixedThreadPool(2);


    // executorService.submit(callable)의 결과로 나오는 Future을 강제 캐스팅하여 CompletableFuture로 변경?
    // ClassCastException: class java.util.concurrent.FutureTask cannot be cast to class java.util.concurrent.CompletableFuture (java.util.concurrent.FutureTask and java.util.concurrent.CompletableFuture are in module java.base of loader 'bootstrap')
//    Callable<String> callable = () -> {
//      Thread.sleep(3000L);
//      return "Thread: " + Thread.currentThread().getName();
//    };

//    CompletableFuture<String> result =  (CompletableFuture<String>) executorService.submit(callable);
//    System.out.println(result.get());

    // CompletableFuture의 runAsync 메서드는 Runnable을 인자로 받고, ExecutorService를 인자로 받는 오버로드된 버전도 제공됩니다. ExecutorService를 제공하지 않으면 ForkJoinPool.commonPool()을 사용합니다.
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      System.out.println("runAsync:");
      System.out.println("Thread: " + Thread.currentThread().getName());
    }, executorService);

    future.get();
    System.out.println("Thread: " + Thread.currentThread().getName());

    // CompletableFuture의 supplyAsync 메서드는 Supplier를 인자로 받고, ExecutorService를 인자로 받는 오버로드된 버전도 제공됩니다. ExecutorService를 제공하지 않으면 ForkJoinPool.commonPool()을 사용합니다.
    CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
      System.out.println("supplyAsync:");
      try {
        Thread.sleep(3000L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return "Thread: " + Thread.currentThread().getName();
    }, executorService);
    System.out.println(result.get());

    executorService.shutdown();
  }
}
