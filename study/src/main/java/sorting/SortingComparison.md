### 정렬 알고리즘 성능 비교 결과 (Sorting Algorithms Performance Comparison)

다양한 정렬 알고리즘의 실행 시간을 배열 크기별로 측정한 벤치마크 결과입니다. 모든 시간은 밀리초(ms) 단위입니다.

| 배열 크기 (Array Size) | Bubble | Cocktail | Cocktail (Optimized) | Selection | Double Selection | Insertion | Insertion(Swap) | Merge | Quick | Heap(List) | Heap(Node) | Heap(Path) | Heap(PQ) | Count | Count(Map) | Radix | AdvCount | AdvRadix |
| :--- | :---: | :---: |:--------------------:|:---------:|:----------------:|:---------:|:---------------:| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **1,000** | 5 ms | 4 ms |         4 ms         |   3 ms    |       3 ms       |   3 ms    |      5 ms       | 1 ms | 3 ms | 4 ms | 43 ms | 4 ms | 4 ms | 1 ms | 5 ms | 1 ms | 5 ms | 0 ms |
| **5,000** | 19 ms | 17 ms |        10 ms         |   10 ms   |       8 ms       |   7 ms    |      9 ms       | 0 ms | 0 ms | 4 ms | 227 ms | 4 ms | 2 ms | 1 ms | 5 ms | 1 ms | 1 ms | 1 ms |
| **10,000** | 87 ms | 79 ms |        49 ms         |   19 ms   |      17 ms       |   7 ms    |      13 ms      | 1 ms | 0 ms | 5 ms | 721 ms | 1 ms | 1 ms | 1 ms | 9 ms | 1 ms | 1 ms | 1 ms |
| **20,000** | 457 ms | 373 ms |        330 ms        |   98 ms   |      84 ms       |   26 ms   |      59 ms      | 2 ms | 1 ms | 7 ms | 2,966 ms | 4 ms | 3 ms | 0 ms | 19 ms | 0 ms | 1 ms | 1 ms |

---

### 결과 분석 및 특징

1.  **비교 기반 정렬 ($O(n^2)$ 계열)**
    *   **Bubble, Cocktail**: 데이터 크기가 커질수록 실행 시간이 기하급수적으로 증가합니다. 20,000개 데이터에서 가장 느린 성능을 보입니다.
    *   **Selection, Double Selection**: 버블 정렬보다는 빠르지만 여전히 $O(n^2)$의 한계를 보입니다.
    *   **Insertion**: 일반적인 $O(n^2)$ 알고리즘 중에서는 상당히 빠른 편이며, 특히 `Insert(Swap)`(Swap 기반)보다 표준 `Insertion`(Shift 기반)이 훨씬 효율적임을 알 수 있습니다.

2.  **효율적인 정렬 ($O(n \log n)$ 계열)**
    *   **Merge, Quick**: 대용량 데이터에서도 매우 빠른 성능을 보장합니다. 퀵 정렬은 랜덤 피벗 최적화 덕분에 안정적인 성능을 보여줍니다.
    *   **Heap Sort**: `Heap(List)`와 `Heap(PQ)`는 매우 빠르지만, 객체 생성 오버헤드가 큰 `Heap(Node)` 방식은 데이터가 많아질수록 성능이 급격히 저하되는 것을 확인할 수 있습니다. 반면, `Heap(Path)`(`HeapWithNodeByBinaryIndexSearch`)는 노드 기반임에도 이진수 경로 찾기 최적화를 통해 일반 노드 방식보다 효율적으로 동작합니다.

3.  **특수 정렬 (비비교 기반, $O(n)$ 계열)**
    *   **Count, Radix**: 데이터의 특성을 활용하여 비교 기반 정렬보다도 압도적인 속도를 보여줍니다.
    *   **Count(Map)**: `TreeMap`을 사용한 방식은 공간 효율성은 좋으나, 삽입 시 발생하는 정렬 비용으로 인해 표준 배열 방식보다는 다소 느립니다.
    *   **AdvCount, AdvRadix**: 최적화된 로직을 통해 가장 안정적이고 빠른 성능을 제공합니다.
