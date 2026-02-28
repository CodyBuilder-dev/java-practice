package datastructure;

import java.util.ArrayList;
import java.util.List;

public class HeapWithList {
  List<Integer> list = new ArrayList<>();
  boolean isMaxHeap = true;

  public HeapWithList() {}

  public HeapWithList(boolean isMaxHeap) {
    this.isMaxHeap = isMaxHeap;
  }

  private boolean compare(int child, int parent) {
    if (isMaxHeap) {
      return list.get(child) > list.get(parent);
    } else {
      return list.get(child) < list.get(parent);
    }
  }

  private void swap(int i, int j) {
    int temp = list.get(i);
    list.set(i, list.get(j));
    list.set(j, temp);
  }

  public void push(int value) {
    list.add(value);
    int index = list.size() - 1;
    while (index > 0) {
      int parentIndex = (index - 1) / 2;
      if (compare(index, parentIndex)) {
        swap(index, parentIndex);
        index = parentIndex;
      } else {
        break;
      }
    }
  }

  public int pop() {
    if (list.isEmpty()) {
      throw new IllegalStateException("Heap is empty");
    }
    int rootValue = list.get(0);
    int lastValue = list.remove(list.size() - 1);
    if (list.isEmpty()) {
      return rootValue;
    }
    list.set(0, lastValue);
    int index = 0;
    while (index < list.size()) {
      int leftChildIndex = 2 * index + 1;
      int rightChildIndex = 2 * index + 2;
      int targetIndex = index;

      if (leftChildIndex < list.size() && compare(leftChildIndex, targetIndex)) {
        targetIndex = leftChildIndex;
      }
      if (rightChildIndex < list.size() && compare(rightChildIndex, targetIndex)) {
        targetIndex = rightChildIndex;
      }
      if (targetIndex != index) {
        swap(index, targetIndex);
        index = targetIndex;
      } else {
        break;
      }
    }
    return rootValue;
  }

  public int peek() {
    if (list.isEmpty()) {
      throw new IllegalStateException("Heap is empty");
    }
    return list.get(0);
  }

  public static void main(String[] args) {
    java.util.Random random = new java.util.Random();
    int count = 1000;

    System.out.println("Max Heap Test (Random 1000 items):");
    HeapWithList maxHeap = new HeapWithList(true);
    for (int i = 0; i < count; i++) {
      maxHeap.push(random.nextInt(10000));
    }

    boolean maxHeapCorrect = true;
    int prevMax = Integer.MAX_VALUE;
    for (int i = 0; i < count; i++) {
      int current = maxHeap.pop();
      if (prevMax < current) {
        maxHeapCorrect = false;
        break;
      }
      prevMax = current;
    }
    System.out.println("Max Heap Sort Correctness: " + maxHeapCorrect);

    System.out.println("\nMin Heap Test (Random 1000 items):");
    HeapWithList minHeap = new HeapWithList(false);
    for (int i = 0; i < count; i++) {
      minHeap.push(random.nextInt(10000));
    }

    boolean minHeapCorrect = true;
    int prevMin = Integer.MIN_VALUE;
    for (int i = 0; i < count; i++) {
      int current = minHeap.pop();
      if (prevMin > current) {
        minHeapCorrect = false;
        break;
      }
      prevMin = current;
    }
    System.out.println("Min Heap Sort Correctness: " + minHeapCorrect);
  }
}
