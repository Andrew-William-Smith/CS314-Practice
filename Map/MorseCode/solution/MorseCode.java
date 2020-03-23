/* CS 314 PRACTICE PROBLEM --- MORSE CODE (SOLUTION)
 * Skills tested: Recursion
 * Author: Andrew Smith
 * Written: 11 November 2019
 *
 * PROBLEM DESCRIPTION:
 * Prior to the advent of the telephone, the primary way that Americans communicated over long distances was via a
 * device called the telegraph.  The telegraph was an extremely simple device capable of transmitting only two signals:
 * silence and a constant tone.  In order to allow people to enter messages into a telegraph that could be easily
 * understood by a trained recipient, a special encoding known as MORSE CODE was developed and adopted worldwide for
 * telegraph communications.  Morse code allowed the telegraph signal to be separated into one of two structures based
 * on the duration of a constant tone, a short pattern known as a DIT (.) and a longer pattern known as a DAH (-), each
 * followed by a brief silent pause to separate the patterns.  Letters or numbers could be expressed through unique
 * combinations of dits and dahs; for example, the letter "S" is represented by the pattern dit-dit-dit (...), whereas
 * "Q" is dah-dah-dit-dah (--.-).
 *
 *
 * EXAMPLES:
 * - "... --- ..." => "SOS"
 * - "..- - -.-. ..." => "UTCS"
 * - "... ..-.-. ..." => "S..-.-.S" (..-.-. is not a valid Morse pattern)
 * - ".... . .-.. .-.- ---" => "HEL.-.-O" (.-.- is not a valid Morse pattern)
 * - ".... . L .-.. O" => "HELLO" (L and O are not valid Morse characters)
 *
 * TASK:
 * Your job as a telegraph operator is to decode messages from Morse code into standard English using the Latin
 * alphabet.  You will be given a transcript of a Morse code message as a String in which a dit is represented by '.'
 * (period), a dah is represented by '-' (hyphen), and "letters" of Morse code are separated by single spaces.  To help
 * you with the translation, you are given a MorseMap, an immutable Map<String, Character> with keys representing Morse
 * letters with their corresponding English letters, numbers, and punctuation marks as values.  As you translate, you
 * should replace any valid Morse patterns you find with their respective English letters; however, if you encounter a
 * pattern that does not correspond to an English letter in the MorseMap, you should leave it in the message.
 *
 * In solving this problem, you may use any methods from the Map<K, V> interface that you like, provided that they do
 * not modify the map --- recall that MorseMap is immutable, so attempting to modify it will result in an
 * UnsupportedOperationException.  You may instantiate no additional data structures other than Strings and
 * StringBuilders, on which you may also call any methods that you think would be useful in solving this problem.  Do
 * not use any other Java classes.  You may write helper methods if you feel that they are necessary.
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

package Map.MorseCode.solution;

import common.MorseMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("Duplicates")
public class MorseCode {
    /**
     * Translate the specified Morse string into Latin characters according to the problem description.
     * @param morseMessage The Morse code transcript to translate.  Will not be <code>null</code>.
     * @param translator The <code>MorseMap</code> you will use to translate Morse patterns into Latin alphabet
     *                   characters.  Refer to the <code>common.MorseMap</code> class for more information.
     * @return A translated version of the specified Morse code transcript.
     * @see MorseMap
     */
    public static String translateMorse(String morseMessage, MorseMap translator) {
        final char SPACE = ' ';
        int characterStart = 0;
        StringBuilder englishMessage = new StringBuilder();

        int idx = 0;
        while (idx < morseMessage.length()) {
            char currentSymbol = morseMessage.charAt(idx);
            // Resolve Morse letter on space
            if (currentSymbol == SPACE) {
                String morseLetter = morseMessage.substring(characterStart, idx);
                appendLetter(morseLetter, englishMessage, translator);
                characterStart = idx + 1;
            }
            idx++;
        }

        // Flush character buffer at end of message
        String finalLetter = morseMessage.substring(characterStart, idx);
        appendLetter(finalLetter, englishMessage, translator);
        return englishMessage.toString();
    }

    /**
     * Append a translation of the specified Morse letter to the English message.
     * @param morseLetter The Morse letter to translate.
     * @param english The English message to which to append the translation.
     * @param translator The Morse translator used to translate Morse letters into Latin alphabet characters.
     */
    private static void appendLetter(String morseLetter, StringBuilder english, MorseMap translator) {
        if (translator.containsKey(morseLetter)) {
            // If the letter is in the map, substitute it
            english.append(translator.get(morseLetter));
        } else {
            // Otherwise, re-insert the string
            english.append(morseLetter);
        }
    }


    @DisplayName("MorseCode solution tests")
    @Tag("MorseCodeSolution")
    public static class MorseCodeTest {
        MorseMap translator = new MorseMap();

        @Test
        @DisplayName("Translate empty String")
        void emptyString() {
            assertEquals("", translateMorse("", translator));
        }

        @Test
        @DisplayName("Translate Morse letters, 1 character")
        void translateLetter1() {
            assertEquals("E", translateMorse(".", translator));
            assertEquals("T", translateMorse("-", translator));
            assertEquals("X", translateMorse("X", translator));
        }

        @Test
        @DisplayName("Translate Morse letters, 2 characters")
        void translateLetter2() {
            assertEquals("I", translateMorse("..", translator));
            assertEquals("A", translateMorse(".-", translator));
            assertEquals("N", translateMorse("-.", translator));
            assertEquals("M", translateMorse("--", translator));
            assertEquals("HI", translateMorse("HI", translator));
        }

        @Test
        @DisplayName("Translate Morse letters, 3 characters")
        void translateLetter3() {
            assertEquals("S", translateMorse("...", translator));
            assertEquals("O", translateMorse("---", translator));
            assertEquals("U", translateMorse("..-", translator));
            assertEquals("CAT", translateMorse("CAT", translator));
        }

        @Test
        @DisplayName("Problem description example 1")
        void descriptionExample1() {
            assertEquals("SOS", translateMorse("... --- ...", translator));
        }

        @Test
        @DisplayName("Problem description example 2")
        void descriptionExample2() {
            assertEquals("UTCS", translateMorse("..- - -.-. ...", translator));
        }

        @Test
        @DisplayName("Problem description example 3")
        void descriptionExample3() {
            assertEquals("S..-.-.S", translateMorse("... ..-.-. ...", translator));
        }

        @Test
        @DisplayName("Problem description example 4")
        void descriptionExample4() {
            assertEquals("HEL.-.-O", translateMorse(".... . .-.. .-.- ---", translator));
        }

        @Test
        @DisplayName("Problem description example 5")
        void descriptionExample5() {
            assertEquals("HELLO", translateMorse(".... . L .-.. O", translator));
        }

        @Test
        @DisplayName("No spaces")
        void noSpaces() {
            assertEquals("...---...", translateMorse("...---...", translator));
        }

        @Test
        @DisplayName("Alternative Morse characters")
        void alternativeCharacters() {
            assertEquals("UTCS", translateMorse("@@_ _ _@_@ @@@", new MorseMap('@', '_')));
        }

        @Test
        @DisplayName("Hello, world!")
        void helloWorld() {
            assertEquals("HELLO,WORLD!", translateMorse(".... . .-.. .-.. --- --..-- .-- --- .-. .-.. -.. -.-.--",
                    translator));
        }

        @Test
        @DisplayName("URL")
        void translateURL() {
            String url = ".... - - .--. ... ---... -..-. -..-. .-- .-- .-- .-.-.- -.-. ... .-.-.- ..- - . -..- .- ... "
                    + ".-.-.- . -.. ..- -..-. ~ ... -.-. --- - - -- -..-. -.-. ... ...-- .---- ....- -..-.";
            assertEquals("HTTPS://WWW.CS.UTEXAS.EDU/~SCOTTM/CS314/", translateMorse(url, translator));
        }

        @Test
        @DisplayName("Pangram")
        void pangram() {
            String pangram = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- "
                    + ".-- -..- -.-- --.. .---- ..--- ...-- ....- ..... -.... --... ---.. ----. ----- .-.-.- --..-- "
                    + "..--.. .----. -.-.-- -..-. -.--. -.--.- .-... ---... -.-.-. -...- .-.-. -....- ..--.- .-..-. "
                    + ".--.-.";
            assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.,?'!/()&:;=+-_\"@", translateMorse(pangram, translator));
        }
    }
}
