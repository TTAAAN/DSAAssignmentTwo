package aupp.algorithms.array;


import java.util.EmptyStackException;
import java.util.Objects;

// 1 2 3 4 5, top = 5, count = 5
// pop(),
public class Stack<T> {
    private int count;
    private T[] arr;

    @SuppressWarnings("unchecked")
    public Stack(int arraySize) {
        if (arraySize < 0) throw new IllegalArgumentException("Initial size must be non-negative: " + arraySize);


        this.count = 0;
        this.arr = (T[]) new Object[arraySize];
    }

    public void push(T newItem) {
        Objects.requireNonNull(newItem, "New item must not be null");

        if (arr.length <= count) resize(arr.length == 0 ? 1 : arr.length * 2);

        arr[count++] = newItem;
    }

    public T pop() {
        if (count <= 0) throw new EmptyStackException();

        T removed = arr[--count];
        arr[count] = null;

        if (arr.length > 1 && count <= arr.length / 4) resize(arr.length / 2);

        return removed;
    }

    public T peek() {
        if (count <= 0) throw new EmptyStackException();

        return arr[count - 1];
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stack[");

        for (int i = 0; i <= count - 1; i++) {
            if (i > 0) sb.append(", ");
            sb.append(arr[i]);
        }

        return sb.append("]").toString();
    }

    public void display() {
        System.out.println("Top");

        for (int i = count - 1; i >= 0; i--) {
            System.out.println("[" + arr[i] + "]");
        }
    }


    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        if (newSize < 0) throw new IllegalArgumentException("New size must be positive integer");

        T[] newArr = (T[]) new Object[newSize];

        for (int i = 0; i < count; i++) {
            newArr[i] = arr[i];
        }

        this.arr = newArr;

    }

    static void main() {
        Stack<Integer> stack = new Stack<>(0);
        stack.push(1);
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.toString());
        stack.display();
    }
}
