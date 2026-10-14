package aupp.algorithms.array;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PriorityQueue<T> Unit Tests")
public class PriorityQueueTest {

    @Nested
    @DisplayName("1. When PriorityQueue is Newly Created (Empty)")
    class WhenEmpty {

        private PriorityQueue<String> pq;

        @BeforeEach
        void setUp() {
            pq = new PriorityQueue<>(5);
        }

        @Test
        @DisplayName("remove(), peekFront(), and peekRear() must return null when empty")
        void shouldReturnNullWhenEmpty() {
            assertAll("Empty PriorityQueue boundary checks",
                    () -> assertNull(pq.remove(), "remove() should return null on an empty priority queue"),
                    () -> assertNull(pq.peekFront(), "peekFront() should return null on an empty priority queue"),
                    () -> assertNull(pq.peekRear(), "peekRear() should return null on an empty priority queue")
            );
        }

        @Test
        @DisplayName("toString() and display() should handle empty state gracefully")
        void shouldHandleDisplayAndToStringGracefully() {
            assertAll("String and Display handling on empty queue",
                    () -> assertNotNull(pq.toString(), "toString() should not return null"),
                    () -> assertDoesNotThrow(() -> pq.display(), "display() should not throw when empty")
            );
        }
    }

    @Nested
    @DisplayName("2. Insert and Peek Operations (peekFront / peekRear)")
    class InsertAndPeekOperations {

        private PriorityQueue<String> pq;

        @BeforeEach
        void setUp() {
            pq = new PriorityQueue<>(5);
        }

        @Test
        @DisplayName("Single element makes both peekFront() and peekRear() return that element")
        void shouldReturnSingleElementForBothPeeks() {
            pq.insert("Emergency", 1);

            assertAll("Single element peek checks",
                    () -> assertEquals("Emergency", pq.peekFront(), "peekFront() should return the single item"),
                    () -> assertEquals("Emergency", pq.peekRear(), "peekRear() should return the single item"),
                    // Ensure peeking is non-destructive
                    () -> assertEquals("Emergency", pq.peekFront(), "peekFront() must not remove the item"),
                    () -> assertEquals("Emergency", pq.peekRear(), "peekRear() must not remove the item")
            );
        }

        @Test
        @DisplayName("peekFront() returns lowest priorityValue (highest priority), peekRear() returns highest priorityValue")
        void shouldIdentifyFrontAndRearCorrectly() {
            // Insert in random order of priorities
            pq.insert("Medium", 5);
            pq.insert("Highest", 1);
            pq.insert("Lowest", 10);

            assertAll("Front and rear priority alignment",
                    () -> assertEquals("Highest", pq.peekFront(), "peekFront() should be the item with smallest priorityValue (1)"),
                    () -> assertEquals("Lowest", pq.peekRear(), "peekRear() should be the item with largest priorityValue (10)")
            );
        }
    }

    @Nested
    @DisplayName("3. Priority Removal Order (Smaller value = Higher priority)")
    class PriorityOrderingTests {

        private PriorityQueue<String> pq;

        @BeforeEach
        void setUp() {
            pq = new PriorityQueue<>(6);
        }

        @Test
        @DisplayName("Items are removed strictly from smallest priorityValue to largest")
        void shouldRemoveInCorrectPriorityOrder() {
            pq.insert("Task C", 3);
            pq.insert("Task A", 1);
            pq.insert("Task D", 4);
            pq.insert("Task B", 2);

            assertAll("Removal order verification",
                    () -> assertEquals("Task A", pq.remove(), "Priority 1 should be removed first"),
                    () -> assertEquals("Task B", pq.remove(), "Priority 2 should be removed second"),
                    () -> assertEquals("Task C", pq.remove(), "Priority 3 should be removed third"),
                    () -> assertEquals("Task D", pq.remove(), "Priority 4 should be removed fourth"),
                    () -> assertNull(pq.remove(), "Queue should be empty after all items removed")
            );
        }

        @Test
        @DisplayName("Handles negative and zero priority values correctly")
        void shouldSupportNegativeAndZeroPriorities() {
            // -10 is smaller than -5, 0, and 5, so -10 has highest priority
            pq.insert("Neutral", 0);
            pq.insert("Super High", -10);
            pq.insert("High", -5);
            pq.insert("Low", 5);

            assertAll("Negative and zero priority order",
                    () -> assertEquals("Super High", pq.peekFront()),
                    () -> assertEquals("Low", pq.peekRear()),
                    () -> assertEquals("Super High", pq.remove()),
                    () -> assertEquals("High", pq.remove()),
                    () -> assertEquals("Neutral", pq.remove()),
                    () -> assertEquals("Low", pq.remove())
            );
        }

        @Test
        @DisplayName("Interleaved inserts and removes update peekFront and peekRear dynamically")
        void shouldHandleInterleavedInsertsAndRemoves() {
            pq.insert("Job 10", 10);
            pq.insert("Job 20", 20);

            assertEquals("Job 10", pq.remove()); // removes 10

            // Current state: [Job 20]
            pq.insert("Job 5", 5);   // New highest priority
            pq.insert("Job 30", 30); // New lowest priority

            assertAll("Dynamic state check after interleaving",
                    () -> assertEquals("Job 5", pq.peekFront()),
                    () -> assertEquals("Job 30", pq.peekRear()),
                    () -> assertEquals("Job 5", pq.remove()),
                    () -> assertEquals("Job 20", pq.remove()),
                    () -> assertEquals("Job 30", pq.remove()),
                    () -> assertNull(pq.remove(), "Queue should now be empty")
            );
        }

        @Test
        @DisplayName("Returns null once queue is exhausted after removals")
        void shouldReturnNullAfterCompleteExhaustion() {
            pq.insert("Only Task", 1);
            pq.remove();

            assertAll("Exhaustion state check",
                    () -> assertNull(pq.remove(), "remove() should return null"),
                    () -> assertNull(pq.peekFront(), "peekFront() should return null"),
                    () -> assertNull(pq.peekRear(), "peekRear() should return null")
            );
        }
    }

    @Nested
    @DisplayName("4. Dynamic Array Resizing")
    class ResizingOperations {

        @Test
        @DisplayName("Resizes when items exceed initial capacity and preserves priority order")
        void shouldResizeWhenExceedingInitialCapacity() {
            // Capacity of 2
            PriorityQueue<String> smallPQ = new PriorityQueue<>(2);

            smallPQ.insert("P3", 30);
            smallPQ.insert("P1", 10);

            // These inserts must trigger array resizing
            assertDoesNotThrow(() -> smallPQ.insert("P4", 40), "Resize should occur without error");
            assertDoesNotThrow(() -> smallPQ.insert("P2", 20), "Further insert should succeed");
            assertDoesNotThrow(() -> smallPQ.insert("P0", 5), "Insert with highest priority should succeed");

            assertAll("Verify elements and order after resize",
                    () -> assertEquals("P0", smallPQ.peekFront()),
                    () -> assertEquals("P4", smallPQ.peekRear()),
                    () -> assertEquals("P0", smallPQ.remove()),
                    () -> assertEquals("P1", smallPQ.remove()),
                    () -> assertEquals("P2", smallPQ.remove()),
                    () -> assertEquals("P3", smallPQ.remove()),
                    () -> assertEquals("P4", smallPQ.remove()),
                    () -> assertNull(smallPQ.remove(), "Queue should be empty")
            );
        }

        @Test
        @DisplayName("Handles multiple consecutive resizes starting from capacity 1")
        void shouldHandleMultipleConsecutiveResizes() {
            PriorityQueue<Integer> pq = new PriorityQueue<>(1);
            int count = 50;

            // Insert numbers in reverse order so priorities are 49, 48, ... 0
            for (int i = count - 1; i >= 0; i--) {
                final int val = i;
                assertDoesNotThrow(() -> pq.insert(val, val), "Insert failed at value " + val);
            }

            // Smallest priorityValue is 0, highest is 49
            assertAll("Peeks after large volume insertion",
                    () -> assertEquals(0, pq.peekFront()),
                    () -> assertEquals(49, pq.peekRear())
            );

            // Remove all elements; they should come out from 0 to 49
            for (int i = 0; i < count; i++) {
                assertEquals(i, pq.remove(), "Mismatch at priority " + i);
            }

            assertNull(pq.remove(), "PQ should be empty after removing all elements");
        }
    }

    @Nested
    @DisplayName("5. Generic Types Compatibility")
    class GenericTypeSupport {

        @Test
        @DisplayName("PriorityQueue works correctly with complex custom objects")
        void shouldSupportCustomObjects() {
            record Patient(String name, String condition) {}

            PriorityQueue<Patient> triageQueue = new PriorityQueue<>(3);
            Patient p1 = new Patient("John Doe", "Mild Cough");
            Patient p2 = new Patient("Jane Smith", "Severe Trauma");
            Patient p3 = new Patient("Bob Dylan", "Fever");

            // Jane has highest urgency (1), Bob has medium (2), John has low (3)
            triageQueue.insert(p1, 3);
            triageQueue.insert(p2, 1);
            triageQueue.insert(p3, 2);

            assertAll("Custom objects priority verification",
                    () -> assertEquals(p2, triageQueue.peekFront(), "Jane Smith should be at front"),
                    () -> assertEquals(p1, triageQueue.peekRear(), "John Doe should be at rear"),
                    () -> assertEquals(p2, triageQueue.remove()),
                    () -> assertEquals(p3, triageQueue.remove()),
                    () -> assertEquals(p1, triageQueue.remove()),
                    () -> assertNull(triageQueue.remove())
            );
        }
    }

    @Nested
    @DisplayName("6. String Representation and Display")
    class ToStringAndDisplayTests {

        @Test
        @DisplayName("toString() contains item representations and display() executes cleanly")
        void shouldVerifyToStringAndDisplay() {
            PriorityQueue<String> pq = new PriorityQueue<>(3);
            pq.insert("CriticalBug", 1);
            pq.insert("MinorFix", 10);

            String str = pq.toString();

            assertAll("toString format inspection and display execution",
                    () -> assertNotNull(str, "toString() cannot be null"),
                    () -> assertTrue(str.contains("CriticalBug"), "toString() should contain 'CriticalBug'"),
                    () -> assertTrue(str.contains("MinorFix"), "toString() should contain 'MinorFix'"),
                    () -> assertDoesNotThrow(pq::display, "display() must not throw any exception")
            );
        }
    }
}