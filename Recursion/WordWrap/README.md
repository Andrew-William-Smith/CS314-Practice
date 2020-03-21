# Word Wrap
[![Difficulty: Easy](https://img.shields.io/badge/difficulty-easy-brightgreen)]()
[![Skill: Recursion](https://img.shields.io/badge/skill-recursion-blue)]()

Nearly every modern computer system requires some version of *word wrap*: the ability to take a single-line block of
text and insert newlines so that it fits within a specified horizontal space.  This document is an example of word wrap
in action: parts of each sentence were typed on a single line in the source file, then your web browser converted them
into the wrapped paragraph form you're looking at now.  Other prominent programs that make use of word wrap are text
editors and word processors&mdash;without this functionality, you'd have to manually split lines in your essays, a task
which would likely be mildly tedious at best.

To save us from having to revert to this onerous behaviour, your task is to implement a simple form of word wrap for
monospace characters.  Given a `Scanner` containing a paragraph's worth of text and an `int` maximum line length,
compute how many lines are necessary to fit the entire paragraph within the specified width.  Some examples of how word
wrap should work are as follows:

- Input string: `the quick brown fox jumps over the lazy dog`  
Line length:  24  
Wrapped text:  
`the quick brown fox`  
`jumps over the lazy dog`  
Lines necessary to fit: **2**

- Input string: `They don't think it be like it is, but it do.`  
Line length: 16  
Wrapped text:  
`They don't`  
`think it be`  
`like it is, but`  
`it do.`  
Lines necessary to fit: **4**

- Input string: `it's supercalifragilisticexpialidocious even though the sound of it`  
Line length: 16  
Wrapped text:  
`it's`  
`supercalifragilisticexpialidocious`  
`even though the`  
`sound of it`  
Lines necessary to fit: **4**

Note that the last example shows the desired behaviour if a word does not fit within the character limit: it should be
printed on its own line with no other words.  In this problem, you may assume that a "word" is delineated from
surrounding words by spaces&mdash;you don't need to worry about breaking on hyphens or other punctuation.  You must
solve this problem recursively, but the exact form the recursion takes is up to you; that is, you may add your own
helper methods as needed.  **You may not instantiate any additional data structures save for `String`s.**
