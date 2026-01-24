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

  public void pushPrev(Node<T> node){
    node.setNext(this);
    node.setPrev(this.prev);
    if(this.prev != null) this.prev.setNext(node);
    this.prev = node;
  }

  public void pop(){
    if(this.prev != null) this.prev.setNext(this.next);
    if(this.next != null) this.next.setPrev(this.prev);
  }

  @Main
  public static void main(){
    Node<Integer> node1 = new Node<>(1);
    Node<Integer> node2 = new Node<>(2);
    Node<Integer> node3 = new Node<>(3);

    node1.setNext(node2);
    node2.setPrev(node1);
    node2.setNext(node3);
    node3.setPrev(node2);

    // Print the linked nodes
    Node<Integer> current = node1;
    while (current != null) {
      System.out.println(current.getValue());
      current = current.getNext();
    }

    Node<Integer> node4 = new Node<>(4);
    Node<Integer> node5 = new Node<>(5);
    Node<Integer> node6 = new Node<>(6);

    node1.pushNext(node4);
    node3.pushNext(node5);
    node1.pushPrev(node6);

    // Print the linked nodes
    current = node6;
    while (current != null) {
      System.out.println(current.getValue());
      current = current.getNext();
    }
  }
}