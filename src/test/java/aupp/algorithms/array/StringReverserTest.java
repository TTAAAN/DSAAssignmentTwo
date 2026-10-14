package aupp.algorithms.array;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StringReverser Unit Tests")
public class StringReverserTest {

    @Nested
    @DisplayName("1. Basic String Reversal")
    class BasicReversalTests {

        @Test
        @DisplayName("Reverses standard words and single characters")
        void shouldReverseStandardWords() {
            assertAll("Standard word reversal checks",
                    () -> assertEquals("olleh", StringReverser.reverse("hello")),
                    () -> assertEquals("avaJ", StringReverser.reverse("Java")),
                    () -> assertEquals("smhtirogla", StringReverser.reverse("algorithms")),
                    () -> assertEquals("Z", StringReverser.reverse("Z"), "Single char should remain itself")
            );
        }

        @Test
        @DisplayName("Reversal of palindromes returns identical strings")
        void shouldHandlePalindromes() {
            assertAll("Palindrome checks",
                    () -> assertEquals("racecar", StringReverser.reverse("racecar")),
                    () -> assertEquals("madam", StringReverser.reverse("madam")),
                    () -> assertEquals("step on no pets", StringReverser.reverse("step on no pets"))
            );
        }
    }

    @Nested
    @DisplayName("2. Special Characters, Whitespace, and Numbers")
    class SpecialCharactersAndWhitespace {

        @Test
        @DisplayName("Reverses strings containing spaces and whitespace correctly")
        void shouldPreserveWhitespacePositionsWhenReversed() {
            assertAll("Whitespace handling",
                    () -> assertEquals("dlrow olleh", StringReverser.reverse("hello world")),
                    () -> assertEquals("   cba", StringReverser.reverse("abc   "), "Trailing spaces become leading"),
                    () -> assertEquals("   ", StringReverser.reverse("   "), "Whitespace-only string")
            );
        }

        @Test
        @DisplayName("Reverses punctuation, numeric characters, and symbols")
        void shouldReversePunctuationAndNumbers() {
            assertAll("Punctuation and digit checks",
                    () -> assertEquals("987654321", StringReverser.reverse("123456789")),
                    () -> assertEquals("!dlroW ,olleH", StringReverser.reverse("Hello, World!")),
                    () -> assertEquals(")(*&^%$#@!", StringReverser.reverse("!@#$%^&*()"))
            );
        }
    }

    @Nested
    @DisplayName("3. Boundary and Edge Cases")
    class BoundaryCases {

        @Test
        @DisplayName("Empty string returns an empty string")
        void shouldReturnEmptyStringForEmptyInput() {
            assertEquals("", StringReverser.reverse(""), "Empty string reversal should produce empty string");
        }
    }

    @Nested
    @DisplayName("4. Stack Resizing on Long Strings")
    class StackResizingTests {

        @Test
        @DisplayName("Underlying Stack resizes successfully when reversing strings larger than initial stack capacity")
        void shouldResizeUnderlyingStackForLongStrings() {
            // Build a 100-character string to trigger dynamic array resizing in Stack
            String original = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz0123456789";
            String expected = new StringBuilder(original).reverse().toString();

            assertAll("Long string reversal with stack resizing",
                    () -> assertDoesNotThrow(() -> StringReverser.reverse(original)),
                    () -> assertEquals(expected, StringReverser.reverse(original))
            );
        }
    }
}