package aupp.algorithms.stackqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Stack<T> Unit Tests")
public class StackTest {

    @Nested
    @DisplayName("1. When Stack is Newly Created (Empty)")
    class WhenEmpty {

        private Stack<String> stack;

        @BeforeEach
        void setUp() {
            stack = new Stack<>(5);
        }

        @Test
        @DisplayName("peek() and pop() must return null when stack is empty")
        void shouldReturnNullWhenEmpty() {
            // Group testing using assertAll
            assertAll("Empty stack boundary checks",
                    () -> assertNull(stack.peek(), "peek() should return null on an empty stack"),
                    () -> assertNull(stack.pop(), "pop() should return null on an empty stack")
            );
        }

        @Test
        @DisplayName("toString() and display() should not crash when stack is empty")
        void shouldHandleDisplayAndToStringGracefully() {
            assertAll("String and Display handling on empty stack",
                    () -> assertNotNull(stack.toString(), "toString() should not return null"),
                    () -> assertDoesNotThrow(() -> stack.display(), "display() should not throw an exception on empty stack")
            );
        }
    }

    @Nested
    @DisplayName("2. Push and Peek Operations")
    class PushAndPeekOperations {

        private Stack<Integer> stack;

        @BeforeEach
        void setUp() {
            stack = new Stack<>(5);
        }

        @Test
        @DisplayName("push() followed by peek() returns top element without removing it")
        void shouldPeekTopElementWithoutRemoving() {
            stack.push(42);

            // Group testing: verifying element is visible and persistent
            assertAll("Push and peek consistency",
                    () -> assertEquals(42, stack.peek(), "First peek should return the pushed item"),
                    () -> assertEquals(42, stack.peek(), "Second peek should still return the item (idempotent)")
            );
        }

        @Test
        @DisplayName("peek() always returns the most recently pushed item")
        void shouldPeekMostRecentItem() {
            stack.push(10);
            stack.push(20);
            stack.push(30);

            assertAll("Most recent item visibility",
                    () -> assertEquals(30, stack.peek(), "peek() should show 30"),
                    () -> assertEquals(30, stack.peek(), "Consecutive peek() calls should remain 30")
            );
        }
    }

    @Nested
    @DisplayName("3. Pop Operations and LIFO Order")
    class PopOperations {

        private Stack<String> stack;

        @BeforeEach
        void setUp() {
            stack = new Stack<>(5);
            stack.push("First");
            stack.push("Second");
            stack.push("Third");
        }

        @Test
        @DisplayName("Elements are popped in Last-In, First-Out (LIFO) order")
        void shouldPopInLifoOrder() {
            // Group testing all pop outputs in sequence
            assertAll("LIFO popping sequence",
                    () -> assertEquals("Third", stack.pop(), "First pop should return last pushed item"),
                    () -> assertEquals("Second", stack.pop(), "Second pop should return middle item"),
                    () -> assertEquals("First", stack.pop(), "Third pop should return first pushed item")
            );
        }

        @Test
        @DisplayName("After popping all elements, subsequent pop() and peek() calls return null")
        void shouldReturnNullAfterExhaustion() {
            stack.pop(); // Third
            stack.pop(); // Second
            stack.pop(); // First

            assertAll("State after complete exhaustion",
                    () -> assertNull(stack.peek(), "peek() should return null once stack is empty"),
                    () -> assertNull(stack.pop(), "pop() should return null once stack is empty")
            );
        }

        @Test
        @DisplayName("Interleaved push and pop operations maintain correct state")
        void shouldSupportInterleavedPushAndPop() {
            Stack<Integer> numberStack = new Stack<>(4);
            numberStack.push(1);
            numberStack.push(2);

            int firstRemoved = numberStack.pop(); // 2
            numberStack.push(3);
            int secondRemoved = numberStack.pop(); // 3
            int thirdRemoved = numberStack.pop(); // 1

            assertAll("Interleaved push and pop results",
                    () -> assertEquals(2, firstRemoved),
                    () -> assertEquals(3, secondRemoved),
                    () -> assertEquals(1, thirdRemoved),
                    () -> assertNull(numberStack.pop(), "Stack should be empty")
            );
        }
    }

    @Nested
    @DisplayName("4. Dynamic Array Resizing")
    class ResizingOperations {

        @Test
        @DisplayName("Stack automatically doubles/expands capacity when initial size is exceeded")
        void shouldResizeWhenExceedingInitialCapacity() {
            // Start with a small capacity of 2
            Stack<Integer> smallStack = new Stack<>(2);

            smallStack.push(100);
            smallStack.push(200);

            // This push must trigger array resizing without throwing ArrayIndexOutOfBoundsException
            assertDoesNotThrow(() -> smallStack.push(300), "Pushing beyond initial capacity should not fail");
            assertDoesNotThrow(() -> smallStack.push(400), "Further pushes after resize should succeed");

            assertAll("Verify items after dynamic resizing",
                    () -> assertEquals(400, smallStack.pop()),
                    () -> assertEquals(300, smallStack.pop()),
                    () -> assertEquals(200, smallStack.pop()),
                    () -> assertEquals(100, smallStack.pop()),
                    () -> assertNull(smallStack.pop(), "Stack should now be empty")
            );
        }

        @Test
        @DisplayName("Stack handles many sequential resizes from initial capacity 1")
        void shouldHandleMultipleConsecutiveResizes() {
            Stack<Integer> tinyStack = new Stack<>(1);

            int numberOfItems = 50;
            for (int i = 0; i < numberOfItems; i++) {
                final int val = i;
                assertDoesNotThrow(() -> tinyStack.push(val), "Push at index " + val + " failed");
            }

            // Verify top is 49
            assertEquals(49, tinyStack.peek());

            // Pop all and ensure LIFO order holds across multiple resizes
            for (int i = numberOfItems - 1; i >= 0; i--) {
                assertEquals(i, tinyStack.pop(), "Mismatch at pop value: " + i);
            }

            assertNull(tinyStack.pop(), "Stack should be empty after popping all resized elements");
        }
    }

    @Nested
    @DisplayName("5. Generic Types Compatibility")
    class GenericTypeSupport {

        @Test
        @DisplayName("Stack handles complex custom objects correctly")
        void shouldWorkWithCustomObjects() {
            // Local custom class to verify custom type support
            record Student(int id, String name) {}

            Stack<Student> studentStack = new Stack<>(2);
            Student s1 = new Student(101, "Alice");
            Student s2 = new Student(102, "Bob");

            studentStack.push(s1);
            studentStack.push(s2);

            assertAll("Custom Object LIFO verification",
                    () -> assertEquals(s2, studentStack.peek()),
                    () -> assertEquals(s2, studentStack.pop()),
                    () -> assertEquals(s1, studentStack.pop()),
                    () -> assertNull(studentStack.pop())
            );
        }
    }

    @Nested
    @DisplayName("6. String Representation and Display")
    class ToStringAndDisplayTests {

        @Test
        @DisplayName("toString() contains pushed elements and display() prints cleanly")
        void shouldVerifyToStringAndDisplay() {
            Stack<String> stack = new Stack<>(3);
            stack.push("Alpha");
            stack.push("Beta");

            String output = stack.toString();

            assertAll("toString format inspection and display execution",
                    () -> assertNotNull(output, "toString() cannot be null"),
                    () -> assertTrue(output.contains("Alpha"), "toString() output should contain 'Alpha'"),
                    () -> assertTrue(output.contains("Beta"), "toString() output should contain 'Beta'"),
                    () -> assertDoesNotThrow(stack::display, "display() must not throw any exception")
            );
        }
    }
}