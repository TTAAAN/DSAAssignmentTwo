package aupp.algorithms.array;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DelimiterChecker Unit Tests")
public class DelimiterCheckerTest {

    @Nested
    @DisplayName("1. Valid and Balanced Delimiters")
    class ValidBalancedDelimiters {

        @Test
        @DisplayName("Returns true for simple matching pairs: (), [], and {}")
        void shouldAcceptSimplePairs() {
            assertAll("Simple valid pairs",
                    () -> assertTrue(DelimiterChecker.check("()"), "Parentheses () should be valid"),
                    () -> assertTrue(DelimiterChecker.check("[]"), "Square brackets [] should be valid"),
                    () -> assertTrue(DelimiterChecker.check("{}"), "Curly braces {} should be valid")
            );
        }

        @Test
        @DisplayName("Returns true for sequential balanced delimiters")
        void shouldAcceptSequentialPairs() {
            assertAll("Sequential balanced delimiters",
                    () -> assertTrue(DelimiterChecker.check("()[]{}")),
                    () -> assertTrue(DelimiterChecker.check("()()()")),
                    () -> assertTrue(DelimiterChecker.check("{}{}{}[]()"))
            );
        }

        @Test
        @DisplayName("Returns true for nested balanced delimiters")
        void shouldAcceptNestedDelimiters() {
            assertAll("Nested balanced delimiters",
                    () -> assertTrue(DelimiterChecker.check("{[()]}")),
                    () -> assertTrue(DelimiterChecker.check("((()))")),
                    () -> assertTrue(DelimiterChecker.check("[{()}]")),
                    () -> assertTrue(DelimiterChecker.check("{[()][{}]}"))
            );
        }
    }

    @Nested
    @DisplayName("2. Invalid Delimiter Combinations")
    class InvalidDelimiters {

        @Test
        @DisplayName("Returns false for mismatched closing delimiters")
        void shouldRejectMismatchedPairs() {
            assertAll("Mismatched opening and closing delimiters",
                    () -> assertFalse(DelimiterChecker.check("(]"), "(] mismatch"),
                    () -> assertFalse(DelimiterChecker.check("{)"), "{) mismatch"),
                    () -> assertFalse(DelimiterChecker.check("[}"), "[} mismatch")
            );
        }

        @Test
        @DisplayName("Returns false for cross-overlapping delimiters")
        void shouldRejectOverlappingDelimiters() {
            assertAll("Interleaved/overlapping delimiters",
                    () -> assertFalse(DelimiterChecker.check("([)]"), "([)] is overlapping"),
                    () -> assertFalse(DelimiterChecker.check("{[(])}"), "{[(])} is overlapping"),
                    () -> assertFalse(DelimiterChecker.check("[(])"), "[(]) is overlapping")
            );
        }

        @Test
        @DisplayName("Returns false when opening delimiters are never closed (stack non-empty at end)")
        void shouldRejectUnclosedOpeningDelimiters() {
            assertAll("Missing closing delimiters",
                    () -> assertFalse(DelimiterChecker.check("(")),
                    () -> assertFalse(DelimiterChecker.check("[")),
                    () -> assertFalse(DelimiterChecker.check("{")),
                    () -> assertFalse(DelimiterChecker.check("(()")),
                    () -> assertFalse(DelimiterChecker.check("{[()]}("))
            );
        }

        @Test
        @DisplayName("Returns false when closing delimiter appears without a matching open (empty stack)")
        void shouldRejectUnexpectedClosingDelimiters() {
            assertAll("Excess closing delimiters",
                    () -> assertFalse(DelimiterChecker.check(")")),
                    () -> assertFalse(DelimiterChecker.check("]")),
                    () -> assertFalse(DelimiterChecker.check("}")),
                    () -> assertFalse(DelimiterChecker.check("()}}")),
                    () -> assertFalse(DelimiterChecker.check("()][()"))
            );
        }
    }

    @Nested
    @DisplayName("3. Text and Code Interspersed with Delimiters")
    class InterspersedTextAndCode {

        @Test
        @DisplayName("Correctly ignores non-delimiter characters (letters, numbers, math symbols)")
        void shouldIgnoreNonDelimiterCharacters() {
            assertAll("Delimiters mixed with expressions",
                    () -> assertTrue(DelimiterChecker.check("a{b[c(d)e]f}g")),
                    () -> assertTrue(DelimiterChecker.check("int x = (3 + 4) * [array[0]];")),
                    () -> assertTrue(DelimiterChecker.check("public static void main(String[] args) { return; }")),
                    () -> assertFalse(DelimiterChecker.check("int x = (3 + 4 * [array[0]);"))
            );
        }

        @Test
        @DisplayName("Returns true for empty strings and strings without any delimiters")
        void shouldHandleStringsWithNoDelimiters() {
            assertAll("No delimiters present",
                    () -> assertTrue(DelimiterChecker.check(""), "Empty string has no delimiter errors"),
                    () -> assertTrue(DelimiterChecker.check("hello world"), "Plain text has no delimiter errors"),
                    () -> assertTrue(DelimiterChecker.check("12345 + 67890"), "Numbers and operators without delimiters")
            );
        }
    }

    @Nested
    @DisplayName("4. Deeply Nested / Long Strings (Stack Resizing)")
    class DeepNestingAndResizing {

        @Test
        @DisplayName("Underlying Stack successfully resizes during deeply nested delimiters")
        void shouldHandleDeeplyNestedDelimitersRequiringStackResize() {
            int depth = 50;
            StringBuilder sb = new StringBuilder();

            // Build: {{{ ... ((( ... ))) ... }}}
            for (int i = 0; i < depth; i++) {
                sb.append('{');
            }
            for (int i = 0; i < depth; i++) {
                sb.append('}');
            }

            String deepValid = sb.toString();
            String deepInvalid = deepValid + "}"; // one extra closing bracket

            assertAll("Deep nesting checks triggering resize",
                    () -> assertTrue(DelimiterChecker.check(deepValid), "50-deep balanced nesting should be true"),
                    () -> assertFalse(DelimiterChecker.check(deepInvalid), "Unbalanced deep nesting should be false")
            );
        }
    }
}