package datastructure;

public class LinkedList {
    private Node<Integer> head;

    // 노드 추가 (끝에 추가)
    public void add(int value) {
        Node<Integer> newNode = new Node<>(value);
        if (head == null) {
            head = newNode;
            return;
        }
        Node<Integer> current = head;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        current.setNext(newNode);
        newNode.setPrev(current);
    }

    public void inverse() {
        Node<Integer> prev = null;
        Node<Integer> curr = head;
        while (curr != null) {
            Node<Integer> nextTemp = curr.getNext();
            curr.setNext(prev);
            curr.setPrev(nextTemp); // doubly linked list 이므로 prev 도 갱신해줘야 함
            prev = curr;
            curr = nextTemp;
        }
        head = prev;
    }

    // 병합 정렬 시작
    public void sort() {
        head = mergeSort(head);
    }

    private Node<Integer> mergeSort(Node<Integer> h) {
        // 기저 조건: 리스트가 비었거나 노드가 하나뿐일 때
        if (h == null || h.getNext() == null) {
            return h;
        }

        // 1. 중간 지점 찾기
        Node<Integer> middle = getMiddle(h);
        Node<Integer> nextToMiddle = middle.getNext();

        // 2. 리스트를 두 개로 쪼개기
        middle.setNext(null);
        if (nextToMiddle != null) {
            nextToMiddle.setPrev(null);
        }

        // 3. 각각 재귀적으로 정렬
        Node<Integer> left = mergeSort(h);
        Node<Integer> right = mergeSort(nextToMiddle);

        // 4. 정렬된 두 리스트 병합
        return sortedMerge(left, right);
    }

    // 정렬된 두 리스트를 하나로 합치는 메서드 (반복문 기반으로 StackOverflow 방지)
    private Node<Integer> sortedMerge(Node<Integer> a, Node<Integer> b) {
        // (1) 가짜 시작 노드(Dummy Node) 생성: 결과 리스트의 시작점을 쉽게 관리하기 위함
        Node<Integer> dummy = new Node<>(0);
        Node<Integer> tail = dummy;

        // (2) 두 리스트를 비교하며 한쪽이 빌 때까지 반복하며 연결
        while (a != null && b != null) {
            if (a.getValue() <= b.getValue()) {
                tail.setNext(a);    // 작은 쪽(a)을 결과 리스트 뒤에 붙임
                a.setPrev(tail);    // 양방향 링크 연결
                a = a.getNext();    // a 포인터 이동
            } else {
                tail.setNext(b);    // 작은 쪽(b)을 결과 리스트 뒤에 붙임
                b.setPrev(tail);    // 양방향 링크 연결
                b = b.getNext();    // b 포인터 이동
            }
            tail = tail.getNext();  // 결과 리스트의 끝점(tail) 갱신
        }

        // (3) 남은 노드들 처리 (한쪽 리스트가 먼저 소진된 경우 나머지를 통째로 붙임)
        if (a != null) {
            tail.setNext(a);
            a.setPrev(tail);
        } else if (b != null) {
            tail.setNext(b);
            b.setPrev(tail);
        }

        // (4) 가짜 노드 다음부터가 실제 정렬된 리스트의 시작
        Node<Integer> result = dummy.getNext();
        if (result != null) {
            result.setPrev(null); // 헤드의 prev는 항상 null이어야 함
        }
        return result;
    }

    // 중간 노드를 찾는 유틸리티 (Fast & Slow Pointer)
    private Node<Integer> getMiddle(Node<Integer> h) {
        if (h == null) return h;
        Node<Integer> fast = h;
        Node<Integer> slow = h;

        while (fast.getNext() != null && fast.getNext().getNext() != null) {
            fast = fast.getNext().getNext();
            slow = slow.getNext();
        }
        return slow;
    }

    // 리스트 출력
    public void printList() {
        Node<Integer> curr = head;
        while (curr != null) {
            System.out.print(curr.getValue() + " ");
            curr = curr.getNext();
        }
        System.out.println();
    }

    // 정렬 여부 확인 (테스트용)
    public boolean isSorted() {
        if (head == null || head.getNext() == null) return true;
        Node<Integer> curr = head;
        while (curr.getNext() != null) {
            if (curr.getValue() > curr.getNext().getValue()) return false;
            curr = curr.getNext();
        }
        return true;
    }

    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        java.util.Random random = new java.util.Random();
        
        System.out.println("--- LinkedList Inverse Test ---");
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
        Node<Integer> curr = list.head;
        boolean correct = true;
        Node<Integer> last = null;
        for (int i = 5; i >= 1; i--) {
            if (curr == null || !curr.getValue().equals(i)) {
                correct = false;
                break;
            }
            last = curr;
            curr = curr.getNext();
        }
        
        if (correct) {
            System.out.print("Backward Data: ");
            curr = last;
            for (int i = 1; i <= 5; i++) {
                if (curr == null || !curr.getValue().equals(i)) {
                    correct = false;
                    break;
                }
                System.out.print(curr.getValue() + " ");
                curr = curr.getPrev();
            }
            System.out.println();
        }
        
        System.out.println("Inverse Correctness: " + correct);

        System.out.println("\n--- LinkedList Merge Sort Test ---");
        list = new LinkedList();
        System.out.print("Original Data: ");
        for (int i = 0; i < 10; i++) {
            int val = random.nextInt(100);
            System.out.print(val + " ");
            list.add(val);
        }
        System.out.println();

        list.sort();
        System.out.print("Sorted Data:   ");
        list.printList();
        
        System.out.println("Correctness: " + list.isSorted());
    }
}
