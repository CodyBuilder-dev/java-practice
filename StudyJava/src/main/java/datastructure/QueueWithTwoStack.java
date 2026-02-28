package datastructure;

import java.util.EmptyStackException;
import java.util.Stack;

public class QueueWithTwoStack<E> {
  Stack<E> input = new Stack<>();
  Stack<E> output = new Stack<>();

  public boolean add(E e) {
    input.push(e);
    return true;
  }

  public boolean offer(E e) {
    input.push(e);
    return true;
  }

  public E remove() {
    if (output.isEmpty()) {
      while (!input.isEmpty()) {
        output.push(input.pop());
      }
      return output.pop();
    }
    return output.pop();
  }

  public E poll() {
    try {
      return remove();
    } catch (EmptyStackException e) {
      return null;
    }
  }

  public E element() {
    if (output.isEmpty()) {
      while (!input.isEmpty()) {
        output.push(input.pop());
      }
      return output.peek();
    }
    return output.peek();
  }

  public E peek() {
    try {
      return element();
    } catch (EmptyStackException e) {
      return null;
    }
  }
}
