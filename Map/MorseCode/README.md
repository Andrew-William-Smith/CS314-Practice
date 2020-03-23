# Morse Code
[![Difficulty: Easy](https://img.shields.io/badge/difficulty-easy-brightgreen)]()
[![Skill: Map](https://img.shields.io/badge/skill-map-blue)]()

Prior to the advent of the telephone, the primary way that Americans communicated over long distances was via a device
called the *telegraph*.  The telegraph was an extremely simple device capable of transmitting only two signals: silence
and a constant tone.  In order to allow people to enter messages into a telegraph that could be easily understood by a
trained recipient, a special encoding known as [*Morse code*](https://en.wikipedia.org/wiki/Morse_code) was developed
and adopted worldwide for telegraph communications.  Morse code allowed the telegraph signal to be separated into one of
two structures based on the duration of a constant tone, a short pattern known as a *dit* (•) and a longer pattern known
as a *dah* (&ndash;), each followed by a brief silent pause to separate the patterns.  Letters or numbers could be
expressed through unique combinations of dits and dahs; for example, the letter `S` is represented by the pattern
*dit*-*dit*-*dit* (•&nbsp;•&nbsp;•), whereas `Q` is *dah*-*dah*-*dit*-*dah* (&ndash;&nbsp;&ndash;&nbsp;•&nbsp;&ndash;).

Your job as a telegraph operator is to decode messages from Morse code into standard English using the Latin alphabet.
You will be given a transcript of a Morse code message as a `String` in which a *dit* is represented by `.` (period), a
*dah* is represented by `-` (hyphen), and "letters" of Morse code are separated by single spaces.  To help you with the
translation, you are given a `MorseMap`, an immutable `Map<String, Character>` with keys representing Morse letters with
their corresponding English letters, numbers, and punctuation marks as values.  As you translate, you should replace any
valid Morse patterns you find with their respective English letters; however, if you encounter a pattern that does *not*
correspond to an English letter in the `MorseMap`, you should leave it in the message.  Some example translations are
given below:

- `... --- ...` &rArr; `SOS`
- `..- - -.-. ...` &rArr; `UTCS`
- `... ..-.-. ...` &rArr; `S..-.-.S` (•&nbsp;•&nbsp;&ndash;&nbsp;•&nbsp;&ndash;&nbsp;• is not a valid Morse pattern)
- `.... . .-.. .-.- ---` &rArr; `HEL.-.-O` (•&nbsp;&ndash;&nbsp;•&nbsp;&ndash; is not a valid Morse pattern)
- `.... . L .-.. O` &rArr; `HELLO` (`L` and `O` are not valid Morse signals)

In solving this problem, you may use any methods from the `Map<K, V>` interface that you like, provided that they do not
modify the map&mdash;recall that `MorseMap` is immutable, so attempting to modify it will result in an
`UnsupportedOperationException`.  **You may instantiate no additional data structures other than `String`s and
`StringBuilder`s**, on which you may also call any methods that you think would be useful in solving this problem.  Do
not use any other Java classes.  You may write helper methods if you feel that they are necessary.