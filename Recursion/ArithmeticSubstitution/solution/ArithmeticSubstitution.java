/* CS 314 PRACTICE PROBLEM --- ARITHMETIC SUBSTITUTION (SOLUTION)
 * Skills tested: Recursion
 * Author: Andrew Smith
 * Written: 4 November 2019
 *
 * PROBLEM DESCRIPTION:
 * You're likely familiar with algebraic substitution, a process in which given an equation like x^2 + 4x + 4 = 0, you
 * can find a value for x that, when substituted, satisfies the equation.  This problem follows a somewhat similar
 * concept, although here instead of finding substitutions for values in the equations, you'll be substituting the
 * OPERATORS instead.  For example, given an equation like 4 ♣ 7 ♥ 2 ♠ 10 = 3, you could find that ♣ = +, ♥ = +, and
 * ♠ = - given that 4 + 7 + 2 - 10 = 3.  Rules like operator precedence and grouping can often make these problems
 * decently challenging to solve by hand and fairly intensive for computers to solve; however, for the sake of time, we
 * shall limit your options to one precedence level, allowing expressions to be evaluated left-to-right.
 *
 * EXAMPLES:
 * - 4 + 3 - 8 = -1
 *   arithmeticSub([4, 3, 8], -1) => [ADDITION, SUBTRACTION]
 * - 4 + 7 + 2 - 10 = 3
 *   arithmeticSub([4, 7, 2, 10], 3) => [ADDITION, ADDITION, SUBTRACTION]
 * - 100 - (-73) + (-22) = 151
 *   arithmeticSub([100, -73, -22], 151) => [SUBTRACTION, ADDITION]
 *
 * TASK:
 * Given an arithmetic substitution problem comprised of an array of ints and an int answer, your goal is to determine
 * which additive operators (+ or -) can be substituted between each integer in order to find the answer.  These
 * operators are expressed in this problem using the enum ArithmeticOperation; if you don't remember how to work with
 * enums from the Evil Hangman assignment, review the Java tutorial.  Your solution must be returned as an
 * ArithmeticOperation[], and as a sanity check, this array should be one element shorter than the integer array you are
 * given to solve.  To simplify the problem, you may assume that the given int[] may not be reordered.  If there is no
 * combination of operators that results in a valid equation, your method should return null.  Note that any solution
 * that attempts to evaluate an arithmetic expression encoded as a String will be awarded no credit.  You may create any
 * additional data structures that you see as necessary to solve the problem, but remember that excessive or inefficient
 * data structures may result in a grade deduction.
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

package Recursion.ArithmeticSubstitution.solution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("Duplicates")
public class ArithmeticSubstitution {
    /**
     * Determine the combination of arithmetic operators necessary to satisfy the given equation, as per the problem
     * description.
     *
     * @param terms The integer terms comprising the equation to solve.  May not be null or empty.  Your solution may
     *              not modify this array.
     * @param answer The desired solution to the equation containing the specified terms.
     * @return An array of operators that satisfy the given equation if a solution can be found, <code>null</code>
     *         otherwise.
     */
    public static ArithmeticOperation[] arithmeticSub(int[] terms, int answer) {
        ArithmeticOperation[] operators = new ArithmeticOperation[terms.length - 1];
        boolean success = doArithmeticSub(terms, answer, operators, 1, terms[0]);
        return success ? operators : null;
    }

    /**
     * Helper method to perform arithmetic substitution for an equation passed to <code>arithmeticSub()</code>.
     * @param terms The integer terms comprising the equation to solve.
     * @param answer The desired solution to the equation.
     * @param operators The operators substituted between the <code>terms</code> in the equation.  If a valid solution
     *                  can be found, this array will contain the solution to the equation.
     * @param currentTerm The index of the current term being solved for.
     * @param currentSum The value of the left-hand side of the equation through term <code>currentTerm</code>.
     * @return Whether a valid solution can be found for the specified equation.
     */
    private static boolean doArithmeticSub(int[] terms, int answer, ArithmeticOperation[] operators, int currentTerm,
                                           int currentSum) {
        // Base case: reached the last term, determine if our answer works
        if (currentTerm == terms.length) {
            return currentSum == answer;
        }

        // Try addition first
        operators[currentTerm - 1] = ArithmeticOperation.ADDITION;
        if (doArithmeticSub(terms, answer, operators, currentTerm + 1, currentSum + terms[currentTerm])) {
            return true;
        }

        // That didn't work, let's try subtraction
        operators[currentTerm - 1] = ArithmeticOperation.SUBTRACTION;
        return doArithmeticSub(terms, answer, operators, currentTerm + 1, currentSum - terms[currentTerm]);
    }

    /**
     * A representation of a single arithmetic operator.  Intended for use in the <code>arithmeticSub</code> method.
     * @see ArithmeticSubstitution#arithmeticSub(int[], int)
     */
    public enum ArithmeticOperation {
        /** The standard arithmetic addition operator. */
        ADDITION,
        /** The standard arithmetic subtraction operator. */
        SUBTRACTION
    }


    @DisplayName("ArithmeticSubstitution solution tests")
    @Tag("ArithmeticSubstitution")
    public static class ArithmeticSubstitutionTest {
        /**
         * Evaluate the arithmetic expression with the specified terms joined by the specified operators.
         * @param terms The numeric terms of the expression.
         * @param operators The operators placed between terms of the expression.
         * @return The value of the specified expression.
         */
        private int evalExpression(int[] terms, ArithmeticOperation[] operators) {
            if (terms.length == 0) {
                throw new IllegalArgumentException("Cannot evaluate equation with no numeric terms");
            }
            if (operators.length != terms.length - 1) {
                throw new IllegalArgumentException("Number of operators must be one less than number of terms");
            }

            int sum = terms[0];
            for (int i = 0; i < operators.length; i++) {
                if (operators[i] == ArithmeticOperation.ADDITION) {
                    sum += terms[i + 1];
                } else {
                    sum -= terms[i + 1];
                }
            }
            return sum;
        }

        @Test
        @DisplayName("Single term, solvable")
        void singleTermSolvable() {
            assertArrayEquals(new ArithmeticOperation[] {}, arithmeticSub(new int[] {10}, 10));
        }

        @Test
        @DisplayName("Single term, unsolvable")
        void singleTermUnsolvable() {
            assertArrayEquals(null, arithmeticSub(new int[] {11}, 10));
        }

        @Test
        @DisplayName("Two terms, solvable, addition")
        void twoTermsSolvableAddition() {
            assertArrayEquals(new ArithmeticOperation[] {ArithmeticOperation.ADDITION},
                    arithmeticSub(new int[] {7, 9}, 16));
        }

        @Test
        @DisplayName("Two terms, solvable, subtraction")
        void twoTermsSolvableSubtraction() {
            assertArrayEquals(new ArithmeticOperation[] {ArithmeticOperation.SUBTRACTION},
                    arithmeticSub(new int[] {7, 9}, -2));
        }

        @Test
        @DisplayName("Two terms, unsolvable")
        void twoTermsUnsolvable() {
            assertArrayEquals(null, arithmeticSub(new int[] {7, 9}, 42));
        }

        @Test
        @DisplayName("Minimum and maximum, no overflow")
        void minimumMaximumNoOverflow() {
            assertArrayEquals(new ArithmeticOperation[] {ArithmeticOperation.ADDITION},
                    arithmeticSub(new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE}, -1));
        }

        @Test
        @DisplayName("Minimum and maximum, overflow")
        void minimumMaximumOverflow() {
            assertArrayEquals(new ArithmeticOperation[] {ArithmeticOperation.SUBTRACTION},
                    arithmeticSub(new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE}, 1));
        }

        @Test
        @DisplayName("Three terms, + +")
        void threeTermsPlusPlus() {
            assertArrayEquals(new ArithmeticOperation[] {ArithmeticOperation.ADDITION, ArithmeticOperation.ADDITION},
                    arithmeticSub(new int[] {7, 9, 11}, 27));
        }

        @Test
        @DisplayName("Three terms, + -")
        void threeTermsPlusMinus() {
            assertArrayEquals(new ArithmeticOperation[] {ArithmeticOperation.ADDITION, ArithmeticOperation.SUBTRACTION},
                    arithmeticSub(new int[] {7, 9, 11}, 5));
        }

        @Test
        @DisplayName("Three terms, - +")
        void threeTermsMinusPlus() {
            assertArrayEquals(new ArithmeticOperation[] {ArithmeticOperation.SUBTRACTION, ArithmeticOperation.ADDITION},
                    arithmeticSub(new int[] {7, 9, 11}, 9));
        }

        @Test
        @DisplayName("Three terms, - -")
        void threeTermsMinusMinus() {
            assertArrayEquals(new ArithmeticOperation[] {ArithmeticOperation.SUBTRACTION,
                    ArithmeticOperation.SUBTRACTION}, arithmeticSub(new int[] {7, 9, 11}, -13));
        }

        @Test
        @DisplayName("Problem description example 1")
        void descriptionExample1() {
            assertArrayEquals(new ArithmeticOperation[] {ArithmeticOperation.ADDITION, ArithmeticOperation.SUBTRACTION},
                    arithmeticSub(new int[] {4, 3, 8}, -1));
        }

        @Test
        @DisplayName("Problem description example 2")
        void descriptionExample2() {
            assertArrayEquals(new ArithmeticOperation[] {ArithmeticOperation.ADDITION, ArithmeticOperation.ADDITION,
                    ArithmeticOperation.SUBTRACTION}, arithmeticSub(new int[] {4, 7, 2, 10}, 3));
        }

        @Test
        @DisplayName("Problem description example 3")
        void descriptionExample3() {
            assertArrayEquals(new ArithmeticOperation[] {ArithmeticOperation.SUBTRACTION, ArithmeticOperation.ADDITION},
                    arithmeticSub(new int[] {100, -73, -22}, 151));
        }

        @RepeatedTest(value = 10, name = "Random expression ({currentRepetition} of {totalRepetitions})")
        void randomExpression() {
            // Populate terms array with random integers
            Random rand = new Random();
            int[] terms = new int[25];
            for (int i = 0; i < terms.length; i++) {
                terms[i] = rand.nextInt();
            }

            // Generate random operations
            ArithmeticOperation[] correctOps = new ArithmeticOperation[terms.length - 1];
            for (int i = 0; i < correctOps.length; i++) {
                correctOps[i] = rand.nextBoolean() ? ArithmeticOperation.ADDITION : ArithmeticOperation.SUBTRACTION;
            }

            // Compute expected answer and ensure that solution method returns valid solution
            int expected = evalExpression(terms, correctOps);
            assertEquals(expected, evalExpression(terms, arithmeticSub(terms, expected)));
        }
    }
}
