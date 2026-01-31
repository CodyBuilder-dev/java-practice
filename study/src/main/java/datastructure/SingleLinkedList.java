package datastructure;

public class SingleLinkedList {
  SingleLinkNode<Integer> head;

  public void add(int value) {
      SingleLinkNode<Integer> newNode = new SingleLinkNode<>(value);
      if (head == null) {
          head = newNode;
          return;
      }
      SingleLinkNode<Integer> current = head;
      while (current.getNext() != null) {
          current = current.getNext();
      }
      current.setNext(newNode);
  }

  public void inverse() {
      SingleLinkNode<Integer> prev = null;
      SingleLinkNode<Integer> curr = head;
      while (curr != null) {
          SingleLinkNode<Integer> nextTemp = curr.getNext();
          curr.setNext(prev);
          prev = curr;
          curr = nextTemp;
      }
      head = prev;
  }

  public void printList() {
      SingleLinkNode<Integer> current = head;
      while (current != null) {
          System.out.print(current.getValue() + " ");
          current = current.getNext();
      }
      System.out.println();
  }

  public static void main(String[] args) {
      SingleLinkedList list = new SingleLinkedList();
      System.out.println("--- SingleLinkedList Inverse Test ---");
      System.out.print("Original Data: ");
      for (int i = 1; i <= 5; i++) {
          System.out.print(i + " ");
          list.add(i);
      }
      System.out.println();

      list.inverse();
      System.out.print("Inversed Data: ");
      list.printList();
      
      // 검증: 5 4 3 2 1 순서여야 함
      SingleLinkNode<Integer> curr = list.head;
      boolean correct = true;
      for (int i = 5; i >= 1; i--) {
          if (curr == null || curr.getValue() != i) {
              correct = false;
              break;
          }
          curr = curr.getNext();
      }
      System.out.println("Correctness: " + correct);
  }
}
