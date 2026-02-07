package datastructure;

import java.util.NoSuchElementException;

public class Deque<T> {
  Node<T> first;
  Node<T> last;

  public void addFirst(Node<T> o) throws IllegalArgumentException {
    if(!offerFirst(o)) {
      throw new IllegalArgumentException();
    }
  }

  public void addLast(Node<T> o) throws IllegalArgumentException {
    if(!offerLast(o)) {
      throw new IllegalArgumentException();
    }
  }

  public boolean offerFirst(Node<T> o) {
    if(o == null) {
      return false;
    }
    if (first == null) {
      first = o;
      last = o;
      return true;
    } else {
      o.setNext(first);
      first.setPrev(o);
      first = o;
      return true;
    }
  }

  public boolean offerLast(Node<T> o) {
    if (o == null) {
      return false;
    }
    if (last == null) {
      first = o;
      last = o;
      return true;
    } else {
      o.setPrev(last);
      last.setNext(o);
      last = o;
      return true;
    }
  }

  public Node<T> removeFirst() throws NoSuchElementException {
    Node<T> first = pollFirst();
    if(first == null) {
      throw new NoSuchElementException();
    }
    return first;
  }

  public Node<T> removeLast() throws NoSuchElementException{
    Node<T> last = pollLast();
    if(last == null) {
      throw new NoSuchElementException();
    }
    return last;
  }

  public Node<T> pollFirst() {
    if (first == null) {
      return null;
    }

    Node<T> next = first.getNext();
    if(next == null){
      last = null;
    } else {
      next.setPrev(null);
      first.setNext(null);
    }
    return first;
  }

  public Node<T> pollLast() {
    if(last == null) {
      return null;
    }

    Node<T> prev = last.getPrev();
    if (prev == null) {
      first = null;
    } else {
      prev.setNext(null);
      last.setPrev(null);
    }
    return last;
  }

  public Node<T> getFirst() throws NoSuchElementException{
    if(first == null){
      throw new NoSuchElementException();
    }
    return first;
  }

  public Node<T> getLast() throws NoSuchElementException{
    if (last == null) {
      throw new NoSuchElementException();
    }
    return last;
  }

  public Node<T> peekFirst() {
    return first;
  }

  public Node<T> peekLast() {
    return last;
  }

  public Deque() {
    first = null;
    last = null;
  }
}