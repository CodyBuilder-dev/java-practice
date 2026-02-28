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
      Node<T> temp = first;
      first = null;
      last = null;
      return temp;
    } else {
      next.setPrev(null);
      first.setNext(null);
      return first;
    }
  }

  public Node<T> pollLast() {
    if(last == null) {
      return null;
    }

    Node<T> prev = last.getPrev();
    if (prev == null) {
      Node<T> temp = last;
      first = null;
      last = null;
      return temp;
    } else {
      prev.setNext(null);
      last.setPrev(null);
      return last;
    }
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

  public static void main(String[] args) {
    Deque<Integer> deque = new Deque<>();
    Node<Integer> node1 = new Node<>(1);
    Node<Integer> node2 = new Node<>(2);
    Node<Integer> node3 = new Node<>(3);

    deque.peekFirst(); // Returns null
    deque.peekLast();  // Returns null
    try {
      deque.getFirst(); // Throws NoSuchElementException
    } catch(NoSuchElementException e) {
      System.out.println("Deque is empty, cannot get first element.");
    }

    try {
      deque.getLast();  // Throws NoSuchElementException
    } catch(NoSuchElementException e) {
      System.out.println("Deque is empty, cannot get last element.");
    }

    try {
      deque.addFirst(null);
    } catch(IllegalArgumentException e) {
      System.out.println("Cannot add null to the front of the deque.");
    }

    try {
      deque.addLast(null);
    } catch (IllegalArgumentException e) {
      System.out.println("Cannot add null to the end of the deque.");
    }

    deque.addFirst(node2); // Deque: 2
    deque.addFirst(node1); // Deque: 1, 2
    deque.addLast(node3);  // Deque: 1, 2, 3
    System.out.println(deque.getFirst().getValue()); // Outputs 1
    System.out.println(deque.getLast().getValue());  // Outputs 3

    deque.removeFirst(); // Removes 1, Deque: 2, 3
    deque.removeLast();  // Removes 3, Deque: 2
    deque.removeLast();  // Removes 2, Deque is now empty

    try {
      deque.removeFirst(); // Deque throws NoSuchElementException
    } catch(NoSuchElementException e) {
      System.out.println("Deque is empty, cannot remove first element.");
    }

    try {
      deque.removeLast();  // Deque throws NoSuchElementException
    } catch(NoSuchElementException e) {
      System.out.println("Deque is empty, cannot remove last element.");
    }

  }
}