package datastructure;

import annotations.Main;

public class Node<T> {
  private T value;
  private Node<T> prev;
  private Node<T> next;

  public Node(T value) {
    this.value = value;
  }

  public Node(T value, Node<T> prev, Node<T> next) {
    this.value = value;
    this.prev = prev;
    this.next = next;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public Node<T> getPrev() {
    return prev;
  }

  public void setPrev(Node<T> prev) {
    this.prev = prev;
  }

  public void pushPrev(Node<T> node){
    node.setNext(this);
    node.setPrev(this.prev);
    if(this.prev != null) this.prev.setNext(node);
    this.prev = node;
  }

  public Node<T> getNext() {
    return next;
  }

  public void setNext(Node<T> next) {
    this.next = next;
  }

  public void pushNext(Node<T> node){
    node.setPrev(this);
    node.setNext(this.next);
    if(this.next != null) this.next.setPrev(node);
    this.next = node;
  }

  public void pop(){
    if(this.prev != null) this.prev.setNext(this.next);
    if(this.next != null) this.next.setPrev(this.prev);
  }

  @Main
  public static void main(String[] args) {
    System.out.println("--- Node Class Functional Test ---");

    // 1. Basic Initialization & Getters/Setters
    Node<Integer> node10 = new Node<>(10);
    Node<Integer> node20 = new Node<>(20);
    Node<Integer> node30 = new Node<>(30);

    System.out.println("Initial Values: " + node10.getValue() + ", " + node20.getValue() + ", " + node30.getValue());

    // 2. Manual Linking
    node10.setNext(node20);
    node20.setPrev(node10);
    node20.setNext(node30);
    node30.setPrev(node20);

    System.out.print("Manual Link Check (1-2-3): ");
    printSequence(node10);

    // 3. pushNext Test
    Node<Integer> node15 = new Node<>(15);
    node10.pushNext(node15); // 10 -> 15 -> 20 -> 30
    System.out.print("After pushNext(15) after 10: ");
    printSequence(node10);

    // 3. pushNext Test (Edge)
    Node<Integer> node35 = new Node<>(35);
    node30.pushNext(node35); // 10 -> 15 -> 20 -> 30 -> 35
    System.out.print("After pushNext(35) after 30: ");
    printSequence(node10);

    // 4. pushPrev Test
    Node<Integer> node25 = new Node<>(25);
    node30.pushPrev(node25); // 10 -> 15 -> 20 -> 25 -> 30 -> 35
    System.out.print("After pushPrev(25) before 30: ");
    printSequence(node10);

    // 4. pushPrev Test (Edge)
    Node<Integer> node5 = new Node<>(5);
    node10.pushPrev(node5); // 5 -> 10 -> 15 -> 20 -> 25 -> 30 -> 35
    System.out.print("After pushPrev(5) before 10: ");
    printSequence(node5);

    // 5. pop Test (Middle)
    node15.pop(); // 15 제거: 5 -> 10 -> 20 -> 25 -> 30 -> 35
    System.out.print("After popping 15 (middle): ");
    printSequence(node5);

    // 6. pop Test (Edge)
    node5.pop(); // 5 제거: 10 -> 20 -> 25 -> 30 -> 35
    System.out.print("After popping 5 (head): ");
    printSequence(node10);

    node35.pop(); // 35 제거: 10 -> 20 -> 25 -> 30
    System.out.print("After popping 35 (tail): ");
    printSequence(node10);

    // 7. setValue Test
    node10.setValue(100);
    System.out.println("After setValue(100): " + node10.getValue());

    System.out.println("--- Test Completed ---");
  }

  private static void printSequence(Node<?> startNode) {
    Node<?> current = startNode;
    while (current != null) {
      System.out.print(current.getValue() + (current.getNext() != null ? " <-> " : ""));
      current = current.getNext();
    }
    System.out.println();
  }
}