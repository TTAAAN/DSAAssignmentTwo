package aupp.algorithms.array;

import java.util.Objects;

public class Queue<T> {
    private T[] arr;
    private int count;
    private int head;
    private int tail;

    // Time Complexity: O(1) [or O(N) where N is arraySize for array allocation]
    @SuppressWarnings("unchecked")
    public Queue(int size) {
        if (size < 0) throw new IllegalArgumentException("Initial size must be non-negative: " + size);

        this.arr = (T[]) new Object[size];

        this.count = 0;
        this.head = 0;
        this.tail = 0;
    }

    // Time Complexity: O(1) amortized, O(N) worst-case when resizing
    public void insert(T newItem) {
        Objects.requireNonNull(newItem, "New item must not be null");

        // Shrink the array if it's full. Accounted for the case when the array is empty (size 0) to avoid multiplying by 2.
        if (arr.length <= count) resize(arr.length == 0 ? 1 : arr.length * 2);

        arr[tail] = newItem;

        tail = (tail + 1) % arr.length;

        count++;
    }

    // Time Complexity: O(1) amortized, O(N) worst-case when shrinking
    public T remove() {
        if (count <= 0) return null;

        T removed = arr[head];
        arr[head] = null;
        head = (head + 1) % arr.length;
        count--;

        // Checking count > 0 to avoid shrinking the array when it's empty
        if (count > 0 && count <= arr.length / 4) resize(arr.length / 2);

        return removed;
    }

    // Time Complexity: O(1)
    public T peekFront() {
        if (count == 0) return null;

        return arr[head];
    }

    // Time Complexity: O(1)
    public T peekRear() {
        if (count == 0) return null;

        // If the tail is at index 0, we need to wrap back to the end of the array.
        return arr[(tail - 1 + arr.length) % arr.length];
    }

    // Time Complexity: O(N) where N is the number of elements in the queue
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Queue[");
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(", ");
            sb.append(arr[(head + i) % arr.length]);
        }
        return sb.append("]").toString();
    }

    // Helper method for dynamic resizing
    // Time Complexity: O(N) where N is the number of copied elements
    @SuppressWarnings("unchecked")
    private void resize(int newSize) {

        T[] newArr = (T[]) new Object[newSize];
        for (int i = 0; i < count; i++) {
            newArr[i] = arr[(head + i) % arr.length];
        }

        this.arr = newArr;
        this.head = 0;
        this.tail = count;
    }

    // Time Complexity: O(N) where N is the number of elements in the queue
    public void display() {
        System.out.println("Front");
        for (int i = 0; i < count; i++) {
            System.out.println("[" + arr[(head + i) % arr.length] + "]");
        }
        System.out.println("Rear");
    }

}
