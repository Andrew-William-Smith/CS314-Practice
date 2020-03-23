/* MORSE CODE MAP
 * Author: Andrew Smith
 * Written: 23 March 2020
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

package common;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An immutable map of Morse code patterns to letters, numbers, and punctuation.  Internally, this "map" is implemented
 * as a tree, with a <i>dit</i> indicating a leftward traversal through the tree and a <i>dah</i> indicating a rightward
 * traversal.  The tree itself is flattened to an array representing its level-order traversal.
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class MorseMap extends AbstractMap<String, Character> {
    // Characters used to represent dits and dahs in Morse keys
    private char dit;
    private char dah;

    /** Set of entries for Morse characters, generated only once upon first request. */
    private Set<Entry<String, Character>> cachedEntries;

    /** Maximum length of a decodable Morse pattern. */
    private static final int MAX_MORSE_LENGTH = 6;
    /** Representation of an invalid Morse code pattern.  Signifies a translation error. */
    private static final char INV = 0;

    /**
     * Level-order traversal of the Morse code translation tree described in the class documentation.  Invalid patterns
     * are represented by an <code>INV</code> value.
     */
    private static final char[] MORSE_TREE = {
        INV,  '5',  INV,  'H',  INV,  '4',  INV,  'S',  INV,  INV,  INV,  'V',  INV,  '3',  INV,  'I',
        INV,  INV,  INV,  'F',  INV,  INV,  INV,  'U',  '?',  INV,  '_',  INV,  INV,  '2',  INV,  'E',
        INV,  '&',  INV,  'L',  '"',  INV,  INV,  'R',  INV,  '+',  '.',  INV,  INV,  INV,  INV,  'A',
        INV,  INV,  INV,  'P',  '@',  INV,  INV,  'W',  INV,  INV,  INV,  'J', '\'',  '1',  INV,  INV,
        INV,  '6',  '-',  'B',  INV,  '=',  INV,  'D',  INV,  '/',  INV,  'X',  INV,  INV,  INV,  'N',
        INV,  INV,  INV,  'C',  ';',  INV,  '!',  'K',  INV,  '(',  ')',  'Y',  INV,  INV,  INV,  'T',
        INV,  '7',  INV,  'Z',  INV,  INV,  ',',  'G',  INV,  INV,  INV,  'Q',  INV,  INV,  INV,  'M',
        ':',  '8',  INV,  INV,  INV,  INV,  INV,  'O',  INV,  '9',  INV,  INV,  INV,  '0',  INV
    };

    /**
     * Translate the Morse code pattern in the specified String to a single character.
     * @param morsePattern The Morse pattern to decode.
     * @return The Latin alphabet character corresponding to the specified Morse pattern, or <code>INV</code> if the
     *         pattern cannot be translated.
     */
    private char translateMorse(String morsePattern) {
        // If the length is outside the bounds of normal Morse code, this is not a Morse pattern
        if (morsePattern.length() == 0 || morsePattern.length() > MAX_MORSE_LENGTH) {
            return INV;
        }

        // Traverse through the flattened tree
        int treeIndex = MORSE_TREE.length / 2;
        int adjustment = (treeIndex + 1) / 2;
        for (int i = 0; i < morsePattern.length(); i++) {
            char blip = morsePattern.charAt(i);
            if (blip == this.dit) {
                // Move left on dits
                treeIndex -= adjustment;
            } else if (blip == this.dah) {
                // Move right on dahs
                treeIndex += adjustment;
            } else {
                // Neither a dit nor a dah
                return INV;
            }

            // Halve adjustment to move to next subtree
            adjustment /= 2;
        }

        return MORSE_TREE[treeIndex];
    }

    /**
     * Morse code translation map constructor.
     * @param dit The character used to represent a <i>dit</i>, or a short blip in the telegraph signal.
     * @param dah The character used to represent a <i>dah</i>, or a long beep in the telegraph signal.  May not be the
     *            same as <i>dit</i>.
     * @throws IllegalArgumentException If <code>dit</code> and <code>dah</code> are the same character.
     */
    public MorseMap(char dit, char dah) {
        if (dit == dah) {
            throw new IllegalArgumentException("Dit and dah characters must be distinct");
        }

        this.dit = dit;
        this.dah = dah;
    }

    /**
     * Morse code translation map constructor.  By default, <i>dits</i> are represented by <code>.</code> and
     * <i>dahs</i> are represented by <code>-</code>.
     */
    public MorseMap() {
        this('.', '-');
    }

    @Override
    public Set<Entry<String, Character>> entrySet() {
        // Only generate entry set if it has not already been created
        if (this.cachedEntries == null) {
            HashSet<Entry<String, Character>> entries = new HashSet<>();
            getEntries(entries, "", MORSE_TREE.length / 2, (MORSE_TREE.length + 1) / 4);
            this.cachedEntries = Collections.unmodifiableSet(entries);
        }
        return this.cachedEntries;
    }

    /** Helper method for <code>entrySet()</code>. */
    private void getEntries(Set<Entry<String, Character>> entries, String morse, int treeIndex, int adjustment) {
        // Add pattern to set if it has a valid translation
        char translation = MORSE_TREE[treeIndex];
        if (translation != INV) {
            entries.add(new SimpleImmutableEntry<>(morse, translation));
        }

        // Traverse in both directions if possible
        if (adjustment != 0) {
            getEntries(entries, morse + this.dit, treeIndex - adjustment, adjustment / 2);
            getEntries(entries, morse + this.dah, treeIndex + adjustment, adjustment / 2);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        // Only String keys are permitted
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("Only String keys are supported for Morse translation");
        }
        return translateMorse((String) key) != INV;
    }

    @Override
    public Character get(Object key) {
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("Only String keys are supported for Morse translation");
        }

        // Must return null if the translation is invalid
        char translation = translateMorse((String) key);
        return translation == INV ? null : translation;
    }
}
