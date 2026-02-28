package datastructure;

import java.util.LinkedList;
import java.util.Queue;

public class HeapWithNode {
  Queue<Node> queue = new LinkedList<>();
  public Node root;
  public boolean isMaxHeap = true;

  public HeapWithNode(int[] arr) {
    for (int i : arr) {
      push(i);
    }
  }

  public void push(int value) {
    Node newNode = new Node(value);
    if (root == null) {
      root = newNode;
      queue.add(newNode);
      return;
    }
    queue.add(newNode);
    Node parent = queue.peek();
    if (parent.left == null) {
      newNode.parent = parent;
      parent.left = newNode;
      while(newNode.parent != null && compare(newNode, newNode.parent)) {
        swap(newNode, newNode.parent);
        newNode = newNode.parent;
      }
    } else if (parent.right == null) {
      newNode.parent = parent;
      parent.right = newNode;
      queue.poll();
      while(newNode.parent != null && compare(newNode, newNode.parent)) {
        swap(newNode, newNode.parent);
        newNode = newNode.parent;
      }
    }
  }

  public int pop() {
    if (root == null) {
      throw new IllegalStateException("Heap is empty");
    }
    int rootValue = root.value;

    // 1. Find the last node and its parent to remove it
    // We can use a BFS to find the last node, but since we have 'queue',
    // the last node added is at the end of some list.
    // However, the 'queue' in current push implementation is used to find the next insertion point.
    // To find the actual last node, we need a different approach or keep track of it.
    // Let's use BFS to find the last node for now as a simple fix.
    Node lastNode = findLastNode();

    if (lastNode == root) {
      root = null;
      queue.clear();
      return rootValue;
    }

    // 2. Move last node's value to root
    root.value = lastNode.value;

    // 3. Remove last node
    Node lastParent = lastNode.parent;
    if (lastParent.left == lastNode) {
      lastParent.left = null;
    } else {
      lastParent.right = null;
    }
    lastNode.parent = null;

    // 4. Rebuild the insertion queue
    rebuildQueue();

    // 5. Bubble down
    bubbleDown(root);

    return rootValue;
  }

  private Node findLastNode() {
    Queue<Node> bfsQueue = new LinkedList<>();
    bfsQueue.add(root);
    Node last = null;
    while (!bfsQueue.isEmpty()) {
      last = bfsQueue.poll();
      if (last.left != null) bfsQueue.add(last.left);
      if (last.right != null) bfsQueue.add(last.right);
    }
    return last;
  }

  private void rebuildQueue() {
    queue.clear();
    if (root == null) return;
    Queue<Node> bfsQueue = new LinkedList<>();
    bfsQueue.add(root);
    while (!bfsQueue.isEmpty()) {
      Node current = bfsQueue.poll();
      if (current.left == null || current.right == null) {
        queue.add(current);
      }
      if (current.left != null) bfsQueue.add(current.left);
      if (current.right != null) bfsQueue.add(current.right);
    }
    // After rebuild, the peek of queue should be the first node that has a null child
    // However, the standard BFS queue logic for insertion needs to be careful.
    // The current push logic expects 'queue.peek()' to be the parent of the next node.
    // In rebuildQueue, we added all nodes that have at least one null child.
    // This matches the expectation of the push logic.
  }

  private void bubbleDown(Node node) {
    while (node != null) {
      Node target = node;
      if (node.left != null && compare(node.left, target)) {
        target = node.left;
      }
      if (node.right != null && compare(node.right, target)) {
        target = node.right;
      }

      if (target != node) {
        swap(node, target);
        node = target;
      } else {
        break;
      }
    }
  }

  public void swap(Node node1, Node node2) {
    int temp = node1.value;
    node1.value = node2.value;
    node2.value = temp;
  }

  public void printHeap() {
    Queue<Node> printQueue = new LinkedList<>();
    printQueue.add(root);
    while (!printQueue.isEmpty()) {
      Node current = printQueue.poll();
      System.out.print(current.value + " ");
      if (current.left != null) {
        printQueue.add(current.left);
      }
      if (current.right != null) {
        printQueue.add(current.right);
      }
    }
    System.out.println();
  }

  private boolean compare(Node child, Node parent) {
    if (isMaxHeap) {
      return child.value > parent.value;
    } else {
      return child.value < parent.value;
    }
  }

  static class Node {
    int value;
    Node left;
    Node right;
    Node parent;

    public Node(int value) {
      this.value = value;
    }
  }

  public static void main(String[] args) {
    java.util.Random random = new java.util.Random();
    int count = 50; // 노드 방식은 삽입/삭제 로직상 대량 테스트시 시간이 걸릴 수 있으므로 50개로 우선 테스트

    System.out.println("HeapWithNode (Max Heap) Test:");
    HeapWithNode heap = new HeapWithNode(new int[]{});
    for (int i = 0; i < count; i++) {
      heap.push(random.nextInt(1000));
    }

    heap.printHeap();

    boolean correct = true;
    int prev = Integer.MAX_VALUE;
    for (int i = 0; i < count; i++) {
      int current = heap.pop();
      if (prev < current) {
        correct = false;
        break;
      }
      prev = current;
    }
    System.out.println("Heap Sort Correctness: " + correct);
  }
}


