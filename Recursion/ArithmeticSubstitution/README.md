# Arithmetic Substitution
[![Difficulty: Medium](https://img.shields.io/badge/difficulty-medium-yellow)]()
[![Skill: Recursion](https://img.shields.io/badge/skill-recursion-blue)]()

You're likely familiar with algebraic substitution, a process in which given an equation like
![x^2 + 4x + 4 = 0](https://render.githubusercontent.com/render/math?math=x%5E2%20%2B%204x%20%2B%204%20%3D%200),
you can find a value for ![x](https://render.githubusercontent.com/render/math?math=x) that, when substituted, satisfies
the equation.  This problem follows a somewhat similar concept, although here instead of finding substitutions for the
*values* in the equations, you'll be substituting the *operators* instead.  For example, given an equation like
4 &clubs; 7 &hearts; 2 &spades; 10 = 3, you could find that &clubs; = `+`, &hearts; = `+`, and &spades; = `-` given that
4 + 7 + 2 - 10 = 3.  Rules like operator precedence and grouping can often make these problems decently challenging to
solve by hand and fairly intensive for computers to solve; however, for the sake of time, we shall limit your options to
one precedence level, allowing expressions to be evaluated left-to-right.

Given an arithmetic substitution problem comprised of an array of `int`s and an `int` answer, your goal is to determine
which *additive* operators (`+` or `-`) can be substituted between each integer in order to find the answer.  These
operators are expressed in this problem using the `enum ArithmeticOperation`:
```java
public enum ArithmeticOperation {
    ADDITION, SUBTRACTION
}
```
If you don't remember how to work with `enum`s from the Evil Hangman assignment, review the Java tutorial
[here](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html); your solution must be returned as an
`ArithmeticOperation[]`.  As a sanity check, your solution array should be one element shorter than the integer array
you are given to solve.  Some example arithmetic substitution problems are as follows:

- 4 + 3 - 8 = -1  
`arithmeticSub([4, 3, 8], -1)` &rArr; `[ADDITION, SUBTRACTION]`
- 4 + 7 + 2 - 10 = 3  
`arithmeticSub([4, 7, 2, 10], 3)` &rArr; `[ADDITION, ADDITION, SUBTRACTION]`
- 100 - (-73) + (-22) = 151  
`arithmeticSub([100, -73, -22], 151)` &rArr; `[SUBTRACTION, ADDITION]`

To simplify the problem, you may assume that the given `int[]` may not be reordered.  If there is no combination of
operators that results in a valid equation, your method should return `null`.  Note that any solution that attempts to
evaluate an arithmetic expression encoded as a `String` will be awarded no credit.  **You may create any additional data
structures** that you see as necessary to solve the problem, but remember that excessive or inefficient data structures
may result in a grade deduction.
