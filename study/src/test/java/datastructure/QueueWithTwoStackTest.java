package datastructure;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.EmptyStackException;

class QueueWithTwoStackTest {

    @Test
    void testAddAndRemove() {
        QueueWithTwoStack<Integer> queue = new QueueWithTwoStack<>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        
        assertEquals(1, queue.remove());
        assertEquals(2, queue.remove());
        assertEquals(3, queue.remove());
    }

    @Test
    void testOfferAndPoll() {
        QueueWithTwoStack<Integer> queue = new QueueWithTwoStack<>();
        assertTrue(queue.offer(10));
        assertTrue(queue.offer(20));
        assertTrue(queue.offer(30));
        
        assertEquals(10, queue.poll());
        assertEquals(20, queue.poll());
        assertEquals(30, queue.poll());
        assertNull(queue.poll());
    }

    @Test
    void testElementAndPeek() {
        QueueWithTwoStack<String> queue = new QueueWithTwoStack<>();
        queue.add("first");
        queue.add("second");
        
        assertEquals("first", queue.element());
        assertEquals("first", queue.peek());
        assertEquals("first", queue.remove());
        
        assertEquals("second", queue.element());
        assertEquals("second", queue.peek());
    }

    @Test
    void testPeekOnEmptyQueue() {
        QueueWithTwoStack<Integer> queue = new QueueWithTwoStack<>();
        assertNull(queue.peek());
    }

    @Test
    void testPollOnEmptyQueue() {
        QueueWithTwoStack<Integer> queue = new QueueWithTwoStack<>();
        assertNull(queue.poll());
    }

    @Test
    void testMixedOperations() {
        QueueWithTwoStack<Integer> queue = new QueueWithTwoStack<>();
        queue.add(1);
        queue.offer(2);
        assertEquals(1, queue.peek());
        assertEquals(1, queue.remove());
        queue.add(3);
        assertEquals(2, queue.poll());
        assertEquals(3, queue.peek());
        assertEquals(3, queue.poll());
        assertNull(queue.poll());
    }

    @Test
    void testFIFOBehavior() {
        QueueWithTwoStack<Integer> queue = new QueueWithTwoStack<>();
        for (int i = 1; i <= 100; i++) {
            queue.add(i);
        }

        for (int i = 1; i <= 100; i++) {
            assertEquals(i, queue.poll());
        }
        assertNull(queue.poll());
    }

    @Test
    void testRemoveOnEmptyQueueThrowsException() {
        QueueWithTwoStack<Integer> queue = new QueueWithTwoStack<>();
        assertThrows(EmptyStackException.class, queue::remove);
    }

    @Test
    void testElementOnEmptyQueueThrowsException() {
        QueueWithTwoStack<String> queue = new QueueWithTwoStack<>();
        assertThrows(EmptyStackException.class, queue::element);
    }

    @Test
    void testRemoveAfterAllElementsRemovedThrowsException() {
        QueueWithTwoStack<Integer> queue = new QueueWithTwoStack<>();
        queue.add(1);
        queue.add(2);

        assertEquals(1, queue.remove());
        assertEquals(2, queue.remove());

        assertThrows(EmptyStackException.class, queue::remove);
    }

    @Test
    void testElementAfterAllElementsRemovedThrowsException() {
        QueueWithTwoStack<Integer> queue = new QueueWithTwoStack<>();
        queue.add(10);

        assertEquals(10, queue.remove());

        assertThrows(EmptyStackException.class, queue::element);
    }
}
