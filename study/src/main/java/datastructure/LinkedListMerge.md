### 링크드 리스트 정렬에 병합 정렬이 가장 좋은 이유

배열과 달리 링크드 리스트 구조에서 병합 정렬이 특히 선호되는 이유는 다음과 같습니다.

1.  **임의 접근(Random Access) 불필요**:
    *   퀵 정렬이나 힙 정렬은 인덱스를 통한 데이터 접근($O(1)$)이 빈번하게 발생합니다.
    *   링크드 리스트는 특정 인덱스에 접근하려면 처음부터 타고 가야 하므로($O(n)$), 인덱스 기반 알고리즘은 성능이 급격히 저하됩니다.
    *   반면 병합 정렬은 순차적인 탐색(Sequential Access)만으로도 데이터를 분할하고 합칠 수 있어 링크드 리스트에 최적화되어 있습니다.

2.  **데이터 복사 없는 제자리 정렬(In-place) 가능성**:
    *   배열 기반 병합 정렬은 데이터를 합칠 때 임시 배열($O(n)$ 공간)이 필요합니다.
    *   링크드 리스트에서는 노드가 가진 **연결 고리(next, prev 포인터)** 만 수정하면 되므로, 추가적인 데이터 복사나 메모리 할당 없이 리스트 자체를 재구성하여 정렬할 수 있습니다.

3.  **최악의 경우에도 보장되는 성능**:
    *   데이터가 이미 정렬되어 있거나 역순인 최악의 상황에서도 병합 정렬은 항상 **$O(n \log n)$** 의 일정한 성능을 보장합니다.

---

### 주요 메서드 상세 설명

`LinkedList.java`에 구현된 핵심 메서드들의 동작 원리를 설명합니다.

#### 1. `add(int value)` - 노드 추가
```java
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
```
리스트의 맨 끝에 새로운 노드를 추가하는 메서드입니다. 리스트가 비어있으면 새 노드를 `head`로 설정하고, 그렇지 않으면 리스트의 끝까지 이동하여 양방향 링크(`next`, `prev`)를 연결합니다.

#### 2. `sort()` - 정렬 시작 (Public API)
```java
public void sort() {
    head = mergeSort(head);
}
```
사용자가 외부에서 호출하는 정렬 인터페이스입니다. 내부적으로 `mergeSort` 재귀 메서드를 호출하여 정렬된 새로운 헤드를 받아와 갱신합니다.

#### 3. `mergeSort(Node<Integer> h)` - 분할 및 재귀 호출
```java
private Node<Integer> mergeSort(Node<Integer> h) {
    if (h == null || h.getNext() == null) {
        return h;
    }
    Node<Integer> middle = getMiddle(h);
    Node<Integer> nextToMiddle = middle.getNext();

    middle.setNext(null);
    if (nextToMiddle != null) {
        nextToMiddle.setPrev(null);
    }

    Node<Integer> left = mergeSort(h);
    Node<Integer> right = mergeSort(nextToMiddle);

    return sortedMerge(left, right);
}
```
병합 정렬의 핵심인 **분할 정복(Divide and Conquer)** 을 수행합니다. 
1. 기저 조건(Base Case)인 노드가 0개 또는 1개일 때까지 리스트를 쪼갭니다.
2. `getMiddle`을 통해 중간 노드를 찾고 리스트를 두 개로 분리합니다.
3. 각각에 대해 재귀적으로 `mergeSort`를 수행한 후 `sortedMerge`로 합칩니다.

#### 4. `getMiddle(Node<Integer> h)` - 중간 노드 찾기
```java
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
```
**Fast & Slow Pointer (또는 Rabbit and Tortoise)** 기법을 사용하여 리스트의 중간 지점을 찾습니다. `fast` 포인터가 두 칸 이동할 때 `slow` 포인터는 한 칸만 이동하므로, `fast`가 끝에 도달했을 때 `slow`는 정확히 중간 위치에 있게 됩니다. 인덱스 접근이 불가능한 링크드 리스트에서 중간을 찾는 가장 효율적인 방식입니다.

#### 5. `sortedMerge(Node<Integer> a, Node<Integer> b)` - 병합 수행
```java
private Node<Integer> sortedMerge(Node<Integer> a, Node<Integer> b) {
    // (1) 가짜 시작 노드(Dummy Node) 생성
    Node<Integer> dummy = new Node<>(0);
    Node<Integer> tail = dummy;

    // (2) 두 리스트를 비교하며 한쪽이 빌 때까지 반복하며 연결
    while (a != null && b != null) {
        if (a.getValue() <= b.getValue()) {
            tail.setNext(a);
            a.setPrev(tail);
            a = a.getNext();
        } else {
            tail.setNext(b);
            b.setPrev(tail);
            b = b.getNext();
        }
        tail = tail.getNext();
    }

    // (3) 남은 노드들 처리
    if (a != null) {
        tail.setNext(a);
        a.setPrev(tail);
    } else if (b != null) {
        tail.setNext(b);
        b.setPrev(tail);
    }

    // (4) 결과 반환 및 정리
    Node<Integer> result = dummy.getNext();
    if (result != null) {
        result.setPrev(null);
    }
    return result;
}
```
두 개의 정렬된 리스트를 하나로 합치는 **병합(Merge)** 단계를 수행하며, 다음과 같이 상세하게 작동합니다.

*   **① 가짜 노드(Dummy Node)의 마법 (첫 번째 노드 결정의 어려움 해결)**: 
    리스트를 병합할 때 가장 먼저 맞닥뜨리는 고민은 "어떤 노드를 결과 리스트의 머리(head)로 정할 것인가?"입니다. 가짜 노드 없이 이 작업을 하려면 다음과 같은 복잡한 문제가 발생합니다.
    *   **초기 head 결정의 번거로움**: 루프를 시작하기 전에 `a`와 `b` 중 어떤 값이 더 작은지 미리 비교하여 `head`를 따로 설정해줘야 합니다.
    *   **중복 로직 발생**: 첫 번째 노드를 끼워 넣는 로직과, 이후 루프를 돌며 나머지 노드들을 끼워 넣는 로직이 거의 동일함에도 불구하고 코드가 중복됩니다.
    *   **Null 예외 처리**: 만약 `a`나 `b` 중 하나가 처음부터 `null`이라면, 시작부터 `head`를 결정할 수 없어 별도의 조건문이 덕지덕지 붙게 됩니다.
    *   **해결책**: `dummy` 노드를 미리 하나 세워두면, "첫 번째 노드냐 아니냐"를 따질 필요 없이 **항상 `tail.next`에 연결**하는 일관된 로직을 사용할 수 있습니다. 마지막에 `dummy.getNext()`만 호출하면 번거로운 예외 처리 없이 진짜 결과의 시작점을 쏙 골라낼 수 있습니다.
*   **② tail 포인터 (기차 칸 연결 담당)**: `tail`은 현재 완성되고 있는 정렬 리스트의 '맨 끝'을 항상 가리키고 있습니다. 새로운 노드가 올 때마다 `tail.setNext()`를 호출하여 기차 칸을 하나씩 뒤로 이어 붙이는 역할을 합니다.
*   **③ 양방향 연결 (`setPrev`)**: 우리 리스트는 앞뒤가 서로 연결된 이중 연결 리스트입니다. 단순히 `setNext`로 다음 칸만 알려주는 게 아니라, `setPrev`를 통해 "내 앞은 너야"라고 뒤 칸 노드에게 알려줘야 끊기지 않는 양방향 고리가 형성됩니다.
*   **④ 남은 노드 일괄 처리**: 비교하다 보면 한쪽 리스트가 먼저 바닥날 수 있습니다. 예를 들어 왼쪽 리스트는 끝났는데 오른쪽 리스트는 `[7, 8, 9]`가 남았다면, 이들은 이미 정렬된 상태이므로 하나씩 비교할 필요 없이 통째로 `tail` 뒤에 슥 붙여주면 효율적으로 작업이 마무리됩니다.

이 방식은 재귀가 아닌 **반복문(Iterative)** 으로 구현되어 있어, 노드가 수만 개에 달하는 대규모 데이터에서도 **StackOverflowError 걱정 없이 안전하게** 동작합니다.

---

### 복잡도 분석

*   **시간 복잡도**: $O(n + m)$ ($n, m$은 각 리스트의 길이). 모든 노드를 한 번씩 순회하며 연결을 변경합니다.
*   **공간 복잡도**: $O(1)$. 새로운 데이터를 생성하지 않고 기존 노드들의 참조(Reference)만 변경하기 때문에 추가 메모리 사용이 매우 적습니다.

---

### 요약
링크드 리스트의 병합 정렬은 **"메모리를 효율적으로 쓰면서도(In-place), 인덱스 접근 없이 순차 탐색만으로 가장 빠르게 정렬할 수 있는"** 최적의 선택입니다.
