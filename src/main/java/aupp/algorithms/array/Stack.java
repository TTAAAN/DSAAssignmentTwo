package aupp.algorithms.array;


import java.util.Objects;

public class Stack<T> {
    private int count;
    private T[] arr;

    // Time Complexity: O(n) for initialization
    @SuppressWarnings("unchecked")
    public Stack(int arraySize) {
        if (arraySize < 0) throw new IllegalArgumentException("Initial size must be non-negative: " + arraySize);


        this.count = 0;
        this.arr = (T[]) new Object[arraySize];
    }

    // Time Complexity: O(1) amortized, O(n) worst case when resizing
    public void push(T newItem) {
        Objects.requireNonNull(newItem, "New item must not be null");

        if (arr.length <= count) resize(arr.length == 0 ? 1 : arr.length * 2);

        arr[count++] = newItem;
    }

    // Time Complexity: O(1) amortized, O(n) worst case when shrinking
    public T pop() {
        if (count <= 0) return null;

        T removed = arr[--count];
        arr[count] = null;

        if (arr.length > 1 && count <= arr.length / 4) resize(arr.length / 2);

        return removed;
    }

    // Time Complexity: O(1), accessing the last element directly
    public T peek() {
        if (count <= 0) return null;

        return arr[count - 1];
    }


    // Time Complexity: O(n), looping through the stack to build a string representation
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stack[");

        for (int i = 0; i <= count - 1; i++) {
            if (i > 0) sb.append(", ");
            sb.append(arr[i]);
        }

        return sb.append("]").toString();
    }

    // Time Complexity: O(n), looping through the stack to display elements
    public void display() {
        System.out.println("Top");

        for (int i = count - 1; i >= 0; i--) {
            System.out.println("[" + arr[i] + "]");
        }
    }


    // Time Complexity: O(n), creating a new array and copying elements
    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        if (newSize < 0) throw new IllegalArgumentException("New size must be positive integer");

        T[] newArr = (T[]) new Object[newSize];

        for (int i = 0; i < count; i++) {
            newArr[i] = arr[i];
        }

        this.arr = newArr;

    }
}
