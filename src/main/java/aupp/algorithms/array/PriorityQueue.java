package aupp.algorithms.array;


import java.util.Objects;

public class PriorityQueue<T> {

    private record Item<T>(T item, int priorityValue) {}

    private Item<T>[] arr;
    private int count;

    @SuppressWarnings("unchecked")
    public PriorityQueue(int arraySize) {
        if (arraySize < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.arr = new Item[arraySize];
        this.count = 0;
    }

    private int findInsertionIndex(Item<T> target) {
        Objects.requireNonNull(target, "Target item must not be null");

        int left = 0;
        int right = count - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid].priorityValue() < target.priorityValue()) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

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

    public T remove() {
        if (count <= 0) return null;

        Item<T> removedItem = arr[--count];
        arr[count] = null;

        if (count > 0 && count <= arr.length / 4) {
            resize(arr.length / 2);
        }

        return removedItem.item();
    }

    public T peekFront() {
        if (count <= 0) return null;

        return arr[count - 1].item();
    }

    public T peekRear() {
        if (count <= 0) return null;

        return arr[0].item();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PriorityQueue[");

        // Build from Front (highest priority) to Rear (lowest priority)
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(", ");
            sb.append(arr[i].item()).append("(priority: ").append(arr[i].priorityValue()).append(")");
        }
        return sb.append("]").toString();
    }

    public void display() {
        System.out.println("Front");
        for (int i = 0; i < count; i++) {
            System.out.println("[" + arr[i].item() + " (priority: " + arr[i].priorityValue() + ")]");
        }
        System.out.println("Rear");
    }


    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        if (newSize < 0) throw new IllegalArgumentException("New size must be positive integer");

        Item<T>[] newArr = new Item[newSize];

        for (int i = 0; i < count; i++) {
            newArr[i] = arr[i];
        }

        this.arr = newArr;
    }

    static void main() {
        PriorityQueue<String> pq = new PriorityQueue<>(5);
        pq.insert("Task 1", 3);
        pq.insert("Task 2", 1);
        pq.insert("Task 3", 2);

        System.out.println(pq); // Should print tasks in order of priority


    }
}
