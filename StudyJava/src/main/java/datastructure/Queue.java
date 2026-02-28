package datastructure;

import java.util.NoSuchElementException;

public class Queue<T> {
  SingleLinkNode<T> first;
  SingleLinkNode<T> last;
  int maxSize;

  public Queue(int maxSize) {
    this.maxSize = maxSize;
  }

  public void add(SingleLinkNode<T> o) throws IllegalArgumentException {
    if(!offer(o)) {
      throw new IllegalArgumentException();
    }
  }

  public boolean offer(SingleLinkNode<T> o) {
    if(o == null || size() >= maxSize) return false;

    if(last == null) {
      first = o;
      last = o;
    } else {
      last.setNext(o);
      last = o;
    }
    return true;
  }

  public SingleLinkNode<T> remove() throws NoSuchElementException {
    SingleLinkNode<T> first = poll();
    if(first == null) {
      throw new NoSuchElementException();
    }
    return first;
  }

  public SingleLinkNode<T> poll() {
    if(first == null) return null;
    SingleLinkNode<T> temp = first;
    first = first.getNext();
    if(first == null) {
      last = null;
    }
    temp.setNext(null);
    return temp;
  }

  public SingleLinkNode<T> get() throws NoSuchElementException{
    if(first == null) {
      throw new NoSuchElementException();
    }
    return peek();
  }

  public SingleLinkNode<T> peek() {
    return first;
  }

  public int size() {
    if(first == null) return 0;
    SingleLinkNode<T> current = first;
    int count = 0;

    while(current != null) {
      count++;
      current = current.getNext();
    }
    return count;
  }
}