package datastructure;

import java.util.EmptyStackException;

public class Stack<T> {
  Node<T> top;
  int maxSize;
  int size;

  public Stack(int maxSize) {
    this.maxSize = maxSize;
  }

  public Node<T> push(Node<T> item) {
    if(item == null || size() >= maxSize) {
      return null;
    }
    top.setNext(item);
    item.setPrev(top);
    top = item;
    size++;
    return item;
  }

  public Node<T> pop() {
    if(top == null) return null;
    Node<T> temp = top;
    if (temp.getPrev() == null) {
      size = 0;
      top = null;
    } else {
      top = top.getPrev();
      top.setNext(null);
      temp.setPrev(null);
      size--;
    }
    return temp;
  }

  public Node<T> peek() throws EmptyStackException {
    if(top == null) {
      throw new EmptyStackException();
    }
    return top;
  }

  public int size() {
    return size;
  }
}