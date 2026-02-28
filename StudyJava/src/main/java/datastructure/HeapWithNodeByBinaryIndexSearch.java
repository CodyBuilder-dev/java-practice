package datastructure;

import java.util.Random;

public class HeapWithNodeByBinaryIndexSearch {

    private static class HeapNode {
        int data;
        HeapNode left, right, parent;

        HeapNode(int data) {
            this.data = data;
        }
    }

    private HeapNode root;
    private int size = 0;

    // 1. 삽입 연산
    public void insert(int value) {
        size++;
        HeapNode newNode = new HeapNode(value);

        if (root == null) {
            root = newNode;
            return;
        }

        // 이진수 경로를 이용해 부모 노드 찾기
        HeapNode parent = findParentNode(size);
        newNode.parent = parent;

        if (parent.left == null) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        // Up-heap (Bubble-up)
        bubbleUp(newNode);
    }

    // 2. 최소값 삭제 및 반환
    public int extractMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");

        int min = root.data;

        if (size == 1) {
            root = null;
            size--;
            return min;
        }

        // 마지막 노드 찾기
        HeapNode lastNode = findNodeByIndex(size);
        
        // 마지막 노드의 값을 루트로 복사
        root.data = lastNode.data;

        // 마지막 노드 연결 해제
        HeapNode parentOfLast = lastNode.parent;
        if (parentOfLast.left == lastNode) {
            parentOfLast.left = null;
        } else {
            parentOfLast.right = null;
        }

        size--;

        // Down-heap (Bubble-down)
        bubbleDown(root);

        return min;
    }

    // 이진수 경로로 특정 인덱스의 노드 찾기 (1-based index)
    private HeapNode findNodeByIndex(int index) {
        String path = Integer.toBinaryString(index);
        HeapNode current = root;
        // 첫 번째 비트는 루트이므로 무시하고 다음부터 따라감
        for (int i = 1; i < path.length(); i++) {
            if (path.charAt(i) == '0') {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return current;
    }

    // 새 노드가 붙을 부모 노드 찾기
    private HeapNode findParentNode(int targetIndex) {
        return findNodeByIndex(targetIndex / 2);
    }

    private void bubbleUp(HeapNode node) {
        while (node.parent != null && node.parent.data > node.data) {
            swapData(node, node.parent);
            node = node.parent;
        }
    }

    private void bubbleDown(HeapNode node) {
        while (node.left != null) {
            HeapNode smallerChild = node.left;
            if (node.right != null && node.right.data < node.left.data) {
                smallerChild = node.right;
            }

            if (node.data <= smallerChild.data) break;

            swapData(node, smallerChild);
            node = smallerChild;
        }
    }

    private void swapData(HeapNode n1, HeapNode n2) {
        int temp = n1.data;
        n1.data = n2.data;
        n2.data = temp;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // 테스트를 위한 메인 메서드
    public static void main(String[] args) {
        HeapWithNodeByBinaryIndexSearch heap = new HeapWithNodeByBinaryIndexSearch();
        int[] testData = {20, 15, 30, 5, 10, 40, 2};
        
        System.out.println("Inserting data: 20, 15, 30, 5, 10, 40, 2");
        for (int val : testData) {
            heap.insert(val);
        }

        System.out.print("Extracted data (should be sorted): ");
        while (!heap.isEmpty()) {
            System.out.print(heap.extractMin() + " ");
        }
        System.out.println();

        // 랜덤 데이터 테스트
        System.out.println("\nRandom Data Test:");
        Random random = new Random();
        int count = 1000;
        for (int i = 0; i < count; i++) {
            heap.insert(random.nextInt(10000));
        }

        boolean correct = true;
        int prev = Integer.MIN_VALUE;
        for (int i = 0; i < count; i++) {
            int current = heap.extractMin();
            if (prev > current) {
                correct = false;
                break;
            }
            prev = current;
        }
        System.out.println("Heap Sort correctness: " + correct);
    }
}
