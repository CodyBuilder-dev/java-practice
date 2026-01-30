package datastructure;

import java.util.PriorityQueue;
import java.util.Queue;

public class HeapWithPriorityQueue {
  public Queue<Integer> queue;
  public HeapWithPriorityQueue(boolean isMaxHeap) {
    queue = new PriorityQueue<>(isMaxHeap ? (a, b) -> b - a : (a, b) -> a - b);
  }


  public static void main(String[] args) {
    java.util.Random random = new java.util.Random();
    int count = 1000;

    System.out.println("Heap with PriorityQueue Max Heap Test (Random 1000 items):");
    HeapWithPriorityQueue maxHeap = new HeapWithPriorityQueue(true);
    for (int i = 0; i < count; i++) {
      maxHeap.queue.add(random.nextInt(10000));
    }

    boolean maxHeapCorrect = true;
    int prevMax = Integer.MAX_VALUE;
    for (int i = 0; i < count; i++) {
      int current = maxHeap.queue.poll();
      if (prevMax < current) {
        maxHeapCorrect = false;
        break;
      }
      prevMax = current;
    }
    System.out.println("Max Heap Sort Correctness: " + maxHeapCorrect);

    System.out.println("\nHeap with PriorityQueue Min Heap Test (Random 1000 items):");
    HeapWithPriorityQueue minHeap = new HeapWithPriorityQueue(false);
    for (int i = 0; i < count; i++) {
      minHeap.queue.add(random.nextInt(10000));
    }

    boolean minHeapCorrect = true;
    int prevMin = Integer.MIN_VALUE;
    for (int i = 0; i < count; i++) {
      int current = minHeap.queue.poll();
      if (prevMin > current) {
        minHeapCorrect = false;
        break;
      }
      prevMin = current;
    }
    System.out.println("Min Heap Sort Correctness: " + minHeapCorrect);
  }
}
