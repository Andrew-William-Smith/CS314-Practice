/* CS 314 PRACTICE PROBLEM --- CHAOCIPHER
 * Skills tested: Native array
 * Author: Andrew Smith
 * Written: 23 March 2020
 *
 * Due to the length of the problem description, it has not been copied here; refer to the README for a full description
 * of how the Chaocipher algorithm works.  Note that the solution to this problem prints out the alphabets at each stage
 * of encryption as in the problem description; you can use this information, printed during every test, to help debug
 * your code.
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
package NativeArray.Chaocipher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"Duplicates"})
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
        // TODO: Implement this method
        return null;
    }


    @DisplayName("Chaocipher solution tests")
    @Tag("Chaocipher")
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
