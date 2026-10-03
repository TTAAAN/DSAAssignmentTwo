package aupp.algorithms.array;

import java.util.Objects;

public class DelimiterChecker {
    // Time Complexity: O(n) where n is the length of the input string
    public static boolean check(String input) {
        Objects.requireNonNull(input, "Input string must not be null");

        Stack<Character> stack = new Stack<>(input.length());

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            // Push opening delimiters onto the stack
            if (ch == '{' || ch == '[' || ch == '(') {
                stack.push(ch);
            }

            // Pop from the stack when encountering closing delimiters and check for balance
            if (ch == '}' || ch == ']' || ch == ')') {
                Character popped = stack.pop();

                // if the stack is empty and if we encounter a closing delimiter, it means the delimiters are not balanced
                if (popped == null) return false;
                if (ch == '}' && popped != '{') return false;
                if (ch == ']' && popped != '[') return false;
                if (ch == ')' && popped != '(') return false;
            }
        }

        return stack.peek() == null;
    }
}