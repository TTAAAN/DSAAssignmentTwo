package aupp.algorithms.array;


import java.util.Objects;

public class PriorityQueue<T> {

    private record Item<T>(T item, int priorityValue) {
    }

    private Item<T>[] arr;
    private int count;

    // Time Complexity: O(N) where N is arraySize for array allocation
    @SuppressWarnings("unchecked")
    public PriorityQueue(int arraySize) {
        if (arraySize < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.arr = new Item[arraySize];
        this.count = 0;
    }

    // Time Complexity: O(log N) for binary search
    private int findInsertionIndex(Item<T> target) {
        Objects.requireNonNull(target, "Target item must not be null");

        int left = 0;
        int right = count - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid].priorityValue() > target.priorityValue()) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    // Time Complexity: O(N) for shifting elements and O(log N) for finding the insertion index, resulting in O(N) overall
    public void insert(T newItem, int priorityValue) {
        Objects.requireNonNull(newItem, "New item must not be null");

        Item<T> x = new Item<>(newItem, priorityValue);

        if (count >= arr.length) {
            resize(arr.length == 0 ? 1 : arr.length * 2);
        }
        int index = findInsertionIndex(x);
        for (int i = count; i > index; i--) {
            arr[i] = arr[i - 1];
        }
        arr[index] = x;
        count++;
    }

    // Time Complexity: O(1) amortized, O(N) worst-case when shrinking
    public T remove() {
        if (count <= 0) return null;

        Item<T> removedItem = arr[--count];
        arr[count] = null;

        if (count > 0 && count <= arr.length / 4) {
            resize(arr.length / 2);
        }

        return removedItem.item();
    }

    // Time Complexity: O(1)
    public T peekFront() {
        // Front (Head) = Highest priority item (next to be popped from the end)
        if (count <= 0) return null;

        return arr[count - 1].item();
    }

    // Time Complexity: O(1)
    public T peekRear() {
        // Rear (Tail) = Lowest priority item (sitting at index 0)
        if (count <= 0) return null;

        return arr[0].item();
    }

    // Time Complexity: O(N) where N is the number of elements in the queue
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PriorityQueue[");

        // Iterate backwards from Front (highest priority at count - 1) to Rear (lowest priority at 0)
        for (int i = count - 1; i >= 0; i--) {
            if (i < count - 1) sb.append(", ");
            sb.append(arr[i].item()).append("(priority: ").append(arr[i].priorityValue()).append(")");
        }
        return sb.append("]").toString();
    }

    // Time Complexity: O(N) where N is the number of elements in the queue
    public void display() {
        System.out.println("Front (Highest Priority)");
        for (int i = count - 1; i >= 0; i--) {
            System.out.println("[" + arr[i].item() + " (priority: " + arr[i].priorityValue() + ")]");
        }
        System.out.println("Rear (Lowest Priority)");
    }


    // Time Complexity: O(N) where N is the number of copied elements
    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        if (newSize < 0) throw new IllegalArgumentException("New size must be positive integer");

        Item<T>[] newArr = new Item[newSize];

        for (int i = 0; i < count; i++) {
            newArr[i] = arr[i];
        }

        this.arr = newArr;
    }
}