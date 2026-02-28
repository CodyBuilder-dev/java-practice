package thread;

import net.openhft.affinity.AffinityLock;

public class VolatileCounterWithOtherCPU {
  private boolean running = true;  // non-volatile flag
  private int count = 0;

  class Reader implements Runnable {
    @Override
    public void run() {
      try(AffinityLock a1 = AffinityLock.acquireLock(1)){ // 1번 CPU를 사용하도록 고정
        System.out.println("Reader started on CPU: " + a1.cpuId());

        // busy-wait: running이 false가 될 때까지 대기
        while (running) {
          // 아무것도 하지 않음 - 로컬 캐시의 값만 계속 읽음
        }

        System.out.println("Reader finished! Final count: " + count);
      } catch (RuntimeException e) {
        throw new RuntimeException(e);
      }
    }
  }

  class Writer implements Runnable {
    @Override
    public void run() {
        try(AffinityLock a2 = AffinityLock.acquireLock(2)){ // 2번 CPU를 사용하도록 고정
        System.out.println("Writer started on CPU: " + a2.cpuId());

        // 카운트를 증가시키고 잠시 대기
        for(int i=0; i<1000; i++){
          count++;
        }
        System.out.println("Writer finished counting: " + count);

        // 충분한 시간을 주어 Reader가 캐시된 값을 읽도록 함
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }

        // running을 false로 변경 후 메모리에 반영 - 하지만 Reader는 이를 못 볼 수 있음!
        running = false;
        System.out.println("Writer set running=false");

      } catch (RuntimeException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    VolatileCounterWithOtherCPU instance = new VolatileCounterWithOtherCPU();  // 같은 인스턴스 사용

    Thread t1 = new Thread(instance.new Reader());
    Thread t2 = new Thread(instance.new Writer());

    t1.start();
    Thread.sleep(50);  // Reader가 먼저 대기 상태에 들어가도록
    t2.start();

    // 최대 5초 대기
    t2.join();
    t1.join(5000);

    if (t1.isAlive()) {
      System.out.println("\n=== volatile 없이는 Reader가 멈추지 않습니다! ===");
      System.out.println("Reader가 running=false를 보지 못해 무한 루프에 빠졌습니다.");
      System.exit(0);
    } else {
      System.out.println("\n=== volatile이 있어 Reader가 정상적으로 종료되었습니다! ===");
      System.out.println("Reader가 running=false를 확인하고 종료했습니다.");
    }
  }
}

