/* CS 314 PRACTICE PROBLEM --- CHAOCIPHER (SOLUTION)
 * Skills tested: Native array
 * Author: Andrew Smith
 * Written: 23 March 2020
 *
 * Due to the length of the problem description, it has not been copied here; refer to the README for a full description
 * of how the Chaocipher algorithm works.
 *
 *
 * LICENSE:
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package NativeArray.Chaocipher.solution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"Duplicates", "ManualArrayCopy"})
public class Chaocipher {
    /**
     * Encode the specified plaintext message using the specified left and right alphabets according to the Chaocipher
     * algorithm described in the problem description.  Note that as this problem is asking you to convert from
     * plaintext into ciphertext, you should be searching for matching characters in the <i>right</i> alphabet.
     *
     * @param plaintext The message to encode using the Chaocipher.  Will not be <code>null</code>.
     * @param leftAlphabet The left (ciphertext) alphabet used to encode the message.  Will not be <code>null</code>
     *                     and will be &ge; 3 characters in length.  You may not modify this array.
     * @param rightAlphabet The right (plaintext) alphabet used to encode the message.  Will not be <code>null</code>
     *                      and will be the same length as <code>leftAlphabet</code>.  You may not modify this array.
     * @return The encoded ciphertext for the specified message.
     * @throws IllegalArgumentException If a character in the plaintext cannot be properly encoded.
     */
    public static String encode(String plaintext, char[] leftAlphabet, char[] rightAlphabet) {
        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            // Determine where the current character is in the right alphabet
            char currentPlain = plaintext.charAt(i);
            int plainIndex = indexOf(currentPlain, rightAlphabet);
            if (plainIndex == -1) {
                throw new IllegalArgumentException(String.format("Character '%c' could not be encoded", currentPlain));
            }

            // Add the translation to the ciphertext
            ciphertext.append(leftAlphabet[plainIndex]);
            // Print the current alphabets
            System.out.printf("%s    %s    %c  %c\n", new String(leftAlphabet), new String(rightAlphabet),
                    leftAlphabet[plainIndex], currentPlain);

            // Permute the alphabet
            leftAlphabet = rotateLeft(leftAlphabet, plainIndex);
            shiftFirstHalf(leftAlphabet, 1);
            // Permute right alphabet (rotating one index past the plaintext index places it at the end)
            rightAlphabet = rotateLeft(rightAlphabet, plainIndex + 1);
            shiftFirstHalf(rightAlphabet, 2);
        }

        return ciphertext.toString();
    }

    /**
     * Helper method for <code>encode()</code>.  Find the index of the specified character (<i>needle</i>) in the
     * specified array (<i>haystack</i>).
     *
     * @param needle The character to find in the array.
     * @param haystack The array through which to search for the character.
     * @return The index of the needle in the haystack if it was found, -1 otherwise.
     */
    private static int indexOf(char needle, char[] haystack) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] == needle) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Helper method for <code>encode()</code>.  Rotate the specified array leftward <code>places</code> places.
     * @param source The array to be rotated.
     * @param places The number of indices by which to rotate <code>source</code>.
     * @return A version of <code>source</code> rotated leftward <code>places</code> places.
     */
    private static char[] rotateLeft(char[] source, int places) {
        // Ensure that we do not attempt to rotate off the end of the array
        int realPlaces = places % source.length;
        char[] rotated = new char[source.length];

        // Copy shifted portion of array
        int destIndex = 0;
        for (int i = realPlaces; i < source.length; i++, destIndex++) {
            rotated[destIndex] = source[i];
        }
        // Copy rotated portion
        for (int i = 0; i < realPlaces; i++, destIndex++) {
            rotated[destIndex] = source[i];
        }

        return rotated;
    }

    /**
     * Helper method for <code>encode()</code>.  Shift the array from index <code>startIndex</code> through the nadir
     * leftward by one index, rotating the value at <code>startIndex</code> to the nadir.
     *
     * @param source The array of characters to be shifted.
     * @param startIndex The index to be extracted/rotated into the nadir.
     */
    private static void shiftFirstHalf(char[] source, int startIndex) {
        final int nadir = source.length / 2;
        char extracted = source[startIndex];

        // Shift characters leftward and replace extracted character
        for (int i = startIndex + 1; i <= nadir; i++) {
            source[i - 1] = source[i];
        }
        source[nadir] = extracted;
    }


    @DisplayName("Chaocipher solution tests")
    @Tag("ChaocipherSolution")
    public static class ChaocipherTest {
        private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";

        /** Assert that the student solution returns the same value as the target solution. */
        private String assertSameEncoding(String plaintext, String leftAlphabet, String rightAlphabet) {
            char[] leftChars = leftAlphabet.toCharArray();
            char[] rightChars = rightAlphabet.toCharArray();
            String expectedEncoding = NativeArray.Chaocipher.solution.Chaocipher.encode(plaintext, leftChars,
                    rightChars);
            String actualEncoding = encode(plaintext, leftChars, rightChars);

            System.out.println("Expected encoding: " + expectedEncoding);
            System.out.println("Actual encoding:   " + actualEncoding);
            assertEquals(expectedEncoding, actualEncoding);
            return actualEncoding;
        }

        /** Perform the Fisher-Yates shuffle on the specified array. */
        private char[] shuffle(char[] source) {
            Random rand = new Random();
            for (int i = source.length - 1; i > 0; i--) {
                int swapIndex = rand.nextInt(source.length);
                char temp = source[swapIndex];
                source[swapIndex] = source[i];
                source[i] = temp;
            }
            return source;
        }

        @Test
        @DisplayName("Problem description example")
        void descriptionExample() {
            assertSameEncoding("HELLOWORLD", "YKTANVHQOZJBEIDGLWCMXFPURS", "CXKSTZEWLPNBRFJDIVQGOUMHYA");
        }

        @Test
        @DisplayName("[Rubin 2010] paper example")
        void paperExample() {
            assertSameEncoding("WELLDONEISBETTERTHANWELLSAID", "HXUCZVAMDSLKPEFJRIGTWOBNYQ",
                    "PTLNBQDEOYSFAVZKGJRIHWXUMC");
        }

        @Test
        @DisplayName("Small alphabets")
        void smallAlphabets() {
            assertSameEncoding("ABCDDCBA", "ZYXW", "ABCD");
        }

        @Test
        @DisplayName("Same alphabets")
        void sameAlphabets() {
            assertSameEncoding("RECTIFY", "RECTIFY", "RECTIFY");
        }

        @Test
        @DisplayName("Alphabets in different scripts")
        void differentScripts() {
            assertSameEncoding("THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG", "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩϜϚ",
                    "WXTKGSLIYAFOUPRJQZBCEHDVMN");
        }

        @RepeatedTest(value = 10, name = "Random alphabets ({currentRepetition} of {totalRepetitions})")
        void randomAlphabets() {
            // Source: "Chaocipher" on Wikipedia
            String plaintext = "THECHAOCIPHER1ISACIPHERMETHODINVENTEDBYJOHNFRANCISBYRNEIN1918ANDDESCRIBEDINHIS1953AUTOB"
                    + "IOGRAPHICALSILENTYEARS2HEBELIEVEDCHAOCIPHERWASSIMPLEYETUNBREAKABLEBYRNESTATEDTHATTHEMACHINEHEUSE"
                    + "DTOENCIPHERHISMESSAGESCOULDBEFITTEDINTOACIGARBOXHEOFFEREDCASHREWARDSFORANYONEWHOCOULDSOLVEITINMA"
                    + "Y2010BYRNESDAUGHTERINLAWPATRICIABYRNEDONATEDALLCHAOCIPHERRELATEDPAPERSANDARTIFACTS3TOTHENATIONAL"
                    + "CRYPTOLOGICMUSEUMINFTMEADEMARYLANDUSATHISLEDTOTHEDISCLOSUREOFTHECHAOCIPHERALGORITHM4";

            // Generate random alphabets
            char[] leftAlphabet = shuffle(ALPHABET.toCharArray());
            char[] rightAlphabet = shuffle(ALPHABET.toCharArray());
            assertSameEncoding(plaintext, new String(leftAlphabet), new String(rightAlphabet));
        }

        @Test
        @DisplayName("Multi-round encoding")
        void decodeAfterEncode() {
            String leftAlphabet = "WPBUFKDSCMGJONQYARVEZHILTX";
            String rightAlphabet = "DZISGXYOETQVRMAKHNFPBUCLWJ";
            String plaintext = "HELLOWORLD";

            for (int i = 0; i < 100; i++) {
                plaintext = assertSameEncoding(plaintext, leftAlphabet, rightAlphabet);
            }
        }
    }
}
