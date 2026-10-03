package aupp.algorithms.array;

public class DelimiterChecker {
    public static boolean check(String input) {
        Stack<Character> stack = new Stack<>(input.length());

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == '{' || ch == '[' || ch == '(') {
                stack.push(ch);
            }

            if (ch == '}' || ch == ']' || ch == ')') {
                Character popped = stack.pop();
                if (popped == null) return false;
                if (ch == '}' && popped != '{') return false;
                if (ch == ']' && popped != '[') return false;
                if (ch == ')' && popped != '(') return false;
            }
        }

        return stack.peek() == null;
    }

    public static void main() {
        String input = "{}[](())a()[]";
        boolean isValid = check(input);
        System.out.println("Input: " + input);
        System.out.println("Is valid: " + isValid);
    }
}
