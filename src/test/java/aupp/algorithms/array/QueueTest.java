package aupp.algorithms.array;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Queue<T> (Circular Queue) Unit Tests")
public class QueueTest {

    @Nested
    @DisplayName("1. When Queue is Newly Created (Empty)")
    class WhenEmpty {

        private Queue<String> queue;

        @BeforeEach
        void setUp() {
            queue = new Queue<>(5);
        }

        @Test
        @DisplayName("peekFront(), peekRear(), and remove() must return null when queue is empty")
        void shouldReturnNullWhenEmpty() {
            // Group testing for empty boundaries
            assertAll("Empty queue boundary checks",
                    () -> assertNull(queue.remove(), "remove() should return null on empty queue"),
                    () -> assertNull(queue.peekFront(), "peekFront() should return null on empty queue"),
                    () -> assertNull(queue.peekRear(), "peekRear() should return null on empty queue")
            );
        }

        @Test
        @DisplayName("toString() and display() should execute gracefully when queue is empty")
        void shouldHandleDisplayAndToStringWhenEmpty() {
            assertAll("Empty queue display operations",
                    () -> assertNotNull(queue.toString(), "toString() should not return null"),
                    () -> assertDoesNotThrow(() -> queue.display(), "display() should not throw on empty queue")
            );
        }
    }

    @Nested
    @DisplayName("2. Insert and Peek Operations (peekFront / peekRear)")
    class InsertAndPeekOperations {

        private Queue<Integer> queue;

        @BeforeEach
        void setUp() {
            queue = new Queue<>(5);
        }

        @Test
        @DisplayName("Single item insert makes peekFront() and peekRear() return the same item")
        void shouldPointBothPeeksToSingleElement() {
            queue.insert(100);

            assertAll("Single item front and rear checks",
                    () -> assertEquals(100, queue.peekFront(), "peekFront() should return the single item"),
                    () -> assertEquals(100, queue.peekRear(), "peekRear() should return the single item"),
                    // Verify peeking is non-destructive
                    () -> assertEquals(100, queue.peekFront(), "peekFront() must remain non-destructive"),
                    () -> assertEquals(100, queue.peekRear(), "peekRear() must remain non-destructive")
            );
        }

        @Test
        @DisplayName("Multiple inserts maintain correct front (oldest) and rear (newest)")
        void shouldReflectFrontAndRearAccurately() {
            queue.insert(10);
            queue.insert(20);
            queue.insert(30);

            assertAll("Multiple item peeks",
                    () -> assertEquals(10, queue.peekFront(), "peekFront() should be the first item inserted (10)"),
                    () -> assertEquals(30, queue.peekRear(), "peekRear() should be the latest item inserted (30)")
            );
        }
    }

    @Nested
    @DisplayName("3. Remove Operations and FIFO Order")
    class RemoveAndFifoOperations {

        private Queue<String> queue;

        @BeforeEach
        void setUp() {
            queue = new Queue<>(4);
            queue.insert("First");
            queue.insert("Second");
            queue.insert("Third");
        }

        @Test
        @DisplayName("remove() retrieves items in strict First-In, First-Out (FIFO) order")
        void shouldRemoveInFifoOrder() {
            assertAll("FIFO removal sequence",
                    () -> assertEquals("First", queue.remove(), "First remove should be 'First'"),
                    () -> assertEquals("Second", queue.remove(), "Second remove should be 'Second'"),
                    () -> assertEquals("Third", queue.remove(), "Third remove should be 'Third'")
            );
        }

        @Test
        @DisplayName("peekFront() updates as items are removed, while peekRear() remains constant")
        void shouldUpdateFrontAsElementsAreRemoved() {
            assertEquals("First", queue.peekFront());
            assertEquals("Third", queue.peekRear());

            queue.remove(); // removes "First"

            assertAll("Peeks after first removal",
                    () -> assertEquals("Second", queue.peekFront(), "peekFront() should now be 'Second'"),
                    () -> assertEquals("Third", queue.peekRear(), "peekRear() should still be 'Third'")
            );
        }

        @Test
        @DisplayName("All operations return null once all elements are removed")
        void shouldReturnNullAfterCompleteExhaustion() {
            queue.remove(); // First
            queue.remove(); // Second
            queue.remove(); // Third

            assertAll("Post-exhaustion boundary returns",
                    () -> assertNull(queue.remove(), "remove() should return null after exhausting items"),
                    () -> assertNull(queue.peekFront(), "peekFront() should return null after exhausting items"),
                    () -> assertNull(queue.peekRear(), "peekRear() should return null after exhausting items")
            );
        }
    }

    @Nested
    @DisplayName("4. Circular Array Wrap-Around Behavior")
    class CircularWrapAroundTests {

        @Test
        @DisplayName("Indices wrap around the end of the array without resizing when capacity allows")
        void shouldWrapAroundCircularly() {
            // Initial capacity: 3
            Queue<String> circularQueue = new Queue<>(3);

            // Fill slots: [A, B, C]
            circularQueue.insert("A");
            circularQueue.insert("B");
            circularQueue.insert("C");

            // Remove 2 items: frees up indices 0 and 1
            assertEquals("A", circularQueue.remove());
            assertEquals("B", circularQueue.remove());

            // Insert 2 new items: rear wraps around to index 0 and 1
            assertDoesNotThrow(() -> circularQueue.insert("D"));
            assertDoesNotThrow(() -> circularQueue.insert("E"));

            // Logical order of items should now be: "C", "D", "E"
            assertAll("Wrap-around peek verification",
                    () -> assertEquals("C", circularQueue.peekFront(), "Front element should be 'C'"),
                    () -> assertEquals("E", circularQueue.peekRear(), "Rear element should be 'E'")
            );

            // Verify FIFO retrieval after wrap-around
            assertAll("Wrap-around removal verification",
                    () -> assertEquals("C", circularQueue.remove()),
                    () -> assertEquals("D", circularQueue.remove()),
                    () -> assertEquals("E", circularQueue.remove()),
                    () -> assertNull(circularQueue.remove(), "Queue should be empty")
            );
        }
    }

    @Nested
    @DisplayName("5. Dynamic Resizing (Linear and Wrapped)")
    class ResizingOperations {

        @Test
        @DisplayName("Queue resizes properly when filled linearly beyond initial capacity")
        void shouldResizeWhenExceedingCapacityLinearly() {
            Queue<Integer> smallQueue = new Queue<>(2);

            smallQueue.insert(10);
            smallQueue.insert(20);

            // Pushing past capacity triggers dynamic resize
            assertDoesNotThrow(() -> smallQueue.insert(30), "Resizing should occur without exception");
            assertDoesNotThrow(() -> smallQueue.insert(40), "Further inserts should succeed");

            assertAll("FIFO order preserved after linear resize",
                    () -> assertEquals(10, smallQueue.peekFront()),
                    () -> assertEquals(40, smallQueue.peekRear()),
                    () -> assertEquals(10, smallQueue.remove()),
                    () -> assertEquals(20, smallQueue.remove()),
                    () -> assertEquals(30, smallQueue.remove()),
                    () -> assertEquals(40, smallQueue.remove()),
                    () -> assertNull(smallQueue.remove(), "Queue should be empty")
            );
        }

        @Test
        @DisplayName("CRITICAL: Queue unwraps and preserves order when resizing while wrapped around")
        void shouldResizeCorrectlyWhenWrappedAround() {
            // Initial capacity: 3
            Queue<String> wrappedQueue = new Queue<>(3);

            wrappedQueue.insert("Item-1");
            wrappedQueue.insert("Item-2");
            wrappedQueue.insert("Item-3");

            // Dequeue two elements so front moves forward
            assertEquals("Item-1", wrappedQueue.remove());
            assertEquals("Item-2", wrappedQueue.remove());

            // Enqueue items to cause circular wrap: front is now after rear
            wrappedQueue.insert("Item-4"); // wrapped
            wrappedQueue.insert("Item-5"); // wrapped

            // Queue currently holds: [Item-3, Item-4, Item-5] (full capacity of 3)
            // Adding one more forces a resize on a wrapped buffer
            assertDoesNotThrow(() -> wrappedQueue.insert("Item-6"), "Resize on wrapped queue must not crash");

            assertAll("Preservation of FIFO order after wrapped resize",
                    () -> assertEquals("Item-3", wrappedQueue.peekFront()),
                    () -> assertEquals("Item-6", wrappedQueue.peekRear()),
                    () -> assertEquals("Item-3", wrappedQueue.remove()),
                    () -> assertEquals("Item-4", wrappedQueue.remove()),
                    () -> assertEquals("Item-5", wrappedQueue.remove()),
                    () -> assertEquals("Item-6", wrappedQueue.remove()),
                    () -> assertNull(wrappedQueue.remove(), "Queue should now be empty")
            );
        }

        @Test
        @DisplayName("Queue handles multiple consecutive resizes starting from capacity 1")
        void shouldHandleMultipleConsecutiveResizes() {
            Queue<Integer> queue = new Queue<>(1);
            int count = 60;

            for (int i = 0; i < count; i++) {
                final int val = i;
                assertDoesNotThrow(() -> queue.insert(val), "Insert failed at value " + val);
            }

            assertAll("Front and rear check after large volume insert",
                    () -> assertEquals(0, queue.peekFront()),
                    () -> assertEquals(count - 1, queue.peekRear())
            );

            // Verify FIFO retrieval of all 60 items
            for (int i = 0; i < count; i++) {
                assertEquals(i, queue.remove(), "Mismatch at removed index " + i);
            }

            assertNull(queue.remove(), "Queue should be empty after removing all items");
        }
    }

    @Nested
    @DisplayName("6. Generic Types Compatibility")
    class GenericTypeSupport {

        @Test
        @DisplayName("Queue properly supports custom record/object types")
        void shouldWorkWithCustomObjects() {
            record Task(int priority, String description) {}

            Queue<Task> taskQueue = new Queue<>(2);
            Task t1 = new Task(1, "Fix bug");
            Task t2 = new Task(2, "Write tests");
            Task t3 = new Task(3, "Deploy");

            taskQueue.insert(t1);
            taskQueue.insert(t2);
            taskQueue.insert(t3); // triggers resize

            assertAll("Custom objects FIFO verification",
                    () -> assertEquals(t1, taskQueue.peekFront()),
                    () -> assertEquals(t3, taskQueue.peekRear()),
                    () -> assertEquals(t1, taskQueue.remove()),
                    () -> assertEquals(t2, taskQueue.remove()),
                    () -> assertEquals(t3, taskQueue.remove()),
                    () -> assertNull(taskQueue.remove())
            );
        }
    }

    @Nested
    @DisplayName("7. String Representation and Display")
    class ToStringAndDisplayTests {

        @Test
        @DisplayName("toString() contains queued items and display() executes without exceptions")
        void shouldVerifyToStringAndDisplay() {
            Queue<String> queue = new Queue<>(3);
            queue.insert("Apple");
            queue.insert("Banana");

            String str = queue.toString();

            assertAll("toString format inspection and display execution",
                    () -> assertNotNull(str, "toString() should not be null"),
                    () -> assertTrue(str.contains("Apple"), "toString() must contain 'Apple'"),
                    () -> assertTrue(str.contains("Banana"), "toString() must contain 'Banana'"),
                    () -> assertDoesNotThrow(queue::display, "display() should execute without throwing")
            );
        }
    }
}