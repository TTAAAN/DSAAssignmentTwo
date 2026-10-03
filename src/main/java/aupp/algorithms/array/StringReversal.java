package aupp.algorithms.array;

import java.util.Objects;

public class StringReversal {
    public static String reverse(String input) {
        Objects.requireNonNull(input, "Input string must not be null");

        Stack<Character> stack = new Stack<>(input.length());

        for (int i = 0; i < input.length(); i++) {
            stack.push(input.charAt(i));
        }

        StringBuilder sb = new StringBuilder();
        while (stack.peek() != null) {
            sb.append(stack.pop());
        }
        return sb.toString();
    }

    public static void main() {
        String input = "Hello, World!";
        String reversed = reverse(input);
        System.out.println("Original: " + input);
        System.out.println("Reversed: " + reversed);
    }
}
