package datastructure;

public class SingleLinkNode<T> {
  private T value;
  private SingleLinkNode<T> next;

  public SingleLinkNode(T value) {
      this.value = value;
  }

  public T getValue() {
      return value;
  }

  public void setValue(T value) {
      this.value = value;
  }

  public SingleLinkNode<T> getNext() {
      return next;
  }

  public void setNext(SingleLinkNode<T> next) {
      this.next = next;
  }
}
