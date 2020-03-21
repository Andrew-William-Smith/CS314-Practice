/* CS 314 PRACTICE PROBLEM --- WORD WRAP
 * Skills tested: Recursion
 * Author: Andrew Smith
 * Written: 3 November 2019
 *
 * PROBLEM DESCRIPTION:
 * Nearly every modern computer system requires some version of WORD WRAP: the ability to take a single-line block of
 * text and insert newlines so that it fits within a specified horizontal space.  This document is an example of word
 * wrap in action: parts of each sentence were typed on a single line in the source file, then your web browser
 * converted them into the wrapped paragraph form you're looking at now.  Other prominent programs that make use of
 * word wrap are text editors and word processors --- without this functionality, you'd have to manually split lines in
 * your essays, a task which would likely be mildly tedious at best.
 *
 * EXAMPLES:
 * - Input string: the quick brown fox jumps over the lazy dog
 *   Line length: 24
 *   Wrapped text:
 *       the quick brown fox
 *       jumps over the lazy dog
 *   Lines necessary to fit: 2
 *
 * - Input string: They don't think it be like it is, but it do.
 *   Line length: 16
 *   Wrapped text:
 *       They don't
 *       think it be
 *       like it is, but
 *       it do.
 *   Lines necessary to fit: 4
 *
 * - Input string: it's supercalifragilisticexpialidocious even though the sound of it
 *   Line length: 16
 *   Wrapped text:
 *       it's
 *       supercalifragilisticexpialidocious
 *       even though the
 *       sound of it
 *   Lines necessary to fit: 4
 *   NOTE that this example shows the desired behaviour if a word does not fit within the character limit: it should be
 *   printed on its own line with no other words.
 *
 * TASK:
 * Your task is to implement a simple form of word wrap for monospace characters.  Given a Scanner containing a
 * paragraph's worth of text and an integer maximum line length, compute how many lines are necessary to fit the entire
 * paragraph within the specified width.  In this problem, you may assume that a "word" is delineated from surrounding
 * words by spaces: you don't need to worry about breaking on hyphens or other punctuation.  You must solve this problem
 * recursively, but the exact form the recursion takes is entirely up to you; that is, you may write helper methods as
 * necessary.  You may not instantiate any additional data structures save for Strings.
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

package Recursion.WordWrap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("Duplicates")
public class WordWrap {
    /**
     * Determine how many lines are necessary to print the paragraph in <code>input</code> with each line wrapped at
     * <code>lineLength</code> characters.  You do not need to check preconditions.
     *
     * @param input Contains the paragraph to wrap.  Will not be <code>null</code>.
     * @param lineLength The maximum number of characters in each line.  Will be positive.
     * @return The number of lines necessary to print the wrapped paragraph.
     */
    public static int wrap(Scanner input, int lineLength) {
        // TODO: Implement this method
        return -1;
    }


    @DisplayName("WordWrap solution tests")
    @Tag("WordWrap")
    public static class WordWrapTest {
        @Test
        @DisplayName("Paragraph is empty")
        void emptyParagraph() {
            assertEquals(0, wrap(new Scanner(""), 10));
        }

        @Test
        @DisplayName("Single word, shorter than limit")
        void singleWordShorter() {
            assertEquals(1, wrap(new Scanner("word"), 10));
        }

        @Test
        @DisplayName("Single word, equal to limit")
        void singleWordEqual() {
            assertEquals(1, wrap(new Scanner("pontifical"), 10));
        }

        @Test
        @DisplayName("Single word, longer than limit")
        void singleWordLonger() {
            assertEquals(1, wrap(new Scanner("unfortunately"), 10));
        }

        @Test
        @DisplayName("Two words, no wrapping")
        void twoWordsNoWrap() {
            assertEquals(1, wrap(new Scanner("One line!"), 10));
        }

        @Test
        @DisplayName("Two words, wrapping required")
        void twoWordsWrap() {
            assertEquals(2, wrap(new Scanner("Multiple lines!"), 10));
        }

        @Test
        @DisplayName("Two words, first exceeds limit")
        void twoWordsFirstLonger() {
            assertEquals(2, wrap(new Scanner("internationalise this"), 10));
        }

        @Test
        @DisplayName("Two words, second exceeds limit")
        void twoWordsSecondLonger() {
            assertEquals(2, wrap(new Scanner("for internationalisation"), 10));
        }

        @Test
        @DisplayName("Problem description example 1")
        void descriptionExample1() {
            assertEquals(2, wrap(new Scanner("the quick brown fox jumps over the lazy dog"), 24));
        }

        @Test
        @DisplayName("Problem description example 2")
        void descriptionExample2() {
            assertEquals(4, wrap(new Scanner("They don't think it be like it is, but it do."), 16));
        }

        @Test
        @DisplayName("Problem description example 3")
        void descriptionExample3() {
            assertEquals(4, wrap(new Scanner("it's supercalifragilisticexpialidocious even though the sound of it"),
                    16));
        }

        @Test
        @DisplayName("Long paragraph")
        void longParagraph() {
            assertEquals(20, wrap(new Scanner(
                  "According to all known laws of aviation, there is no way a bee should be able to fly. Its wings are too "
                + "small to get its fat little body off the ground. The bee, of course, flies anyway because bees don't care "
                + "what humans think is impossible. Yellow, black. Yellow, black. Yellow, black. Yellow, black. Ooh, black and "
                + "yellow! Let's shake it up a little. Barry! Breakfast is ready! Coming! Hang on a second. Hello? Barry? "
                + "Adam? Can you believe this is happening? I can't. I'll pick you up. Looking sharp. Use the stairs. Your "
                + "father paid good money for those. Sorry. I'm excited. Here's the graduate. We're very proud of you, son. A "
                + "perfect report card, all B's. Very proud. Ma! I got a thing going here. You got lint on your fuzz. Ow! "
                + "That's me! Wave to us! We'll be in row 118,000. Bye! Barry, I told you, stop flying in the house! Hey, "
                + "Adam. Hey, Barry. Is that fuzz gel? A little. Special day, graduation. Never thought I'd make it. Three "
                + "days grade school, three days high school. Those were awkward. Three days college. I'm glad I took a day "
                + "and hitchhiked around the hive. You did come back different. Hi, Barry. Artie, growing a mustache? Looks "
                + "good. Hear about Frankie? Yeah. You going to the funeral? No, I'm not going. Everybody knows, sting "
                + "someone, you die. Don't waste it on a squirrel. Such a hothead. I guess he could have just gotten out of "
                + "the way. I love this incorporating an amusement park into our day. That's why we don't need vacations. Boy, "
                + "quite a bit of pomp under the circumstances."
            ), 80));
        }
    }
}
