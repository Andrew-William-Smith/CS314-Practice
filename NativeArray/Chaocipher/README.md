# Chaocipher
[![Difficulty: Medium](https://img.shields.io/badge/difficulty-medium-yellow)]()
[![Skill: Native Array](https://img.shields.io/badge/skill-native%20array-blue)]()

*Note:* I know this problem looks frightening, but understanding the algorithm is actually the hardest part.  Writing
the actual code is fairly straightforward and involves some interesting array manipulation problems.  Have fun!

The [Chaocipher](https://en.wikipedia.org/wiki/Chaocipher) is a cryptographic cipher invented by John Francis Byrne in
1918 but uncracked until 2010.  The Chaocipher algorithm is overall fairly simple, as described by Israeli cryptanalyst
Moshe Rubin in [a 2010 paper](http://www.chaocipher.com/ActualChaocipher/Chaocipher-Revealed-Algorithm.pdf); however,
its implementation involves some fairly complicated array operations, such as rotations and shifts.  A simplified
description of the Chaocipher algorithm is given below, lifted largely from the linked paper.  Note that any array
indices mentioned are zero-indexed, as in Java code.

To begin encoding a message, you must first create *left* and *right* alphabets.  These alphabets are permutations of
the standard alphabet of the language of the message to encode, and you do not need to generate them in order to solve
this problem.  The *right* alphabet is used to find characters in the unencoded message (or *plaintext*), and the *left*
alphabet is used to find translations into the encoded version (or *ciphertext*).  An example left and right alphabet
are given below:
```
                            +            *
Left alphabet (ciphertext): YKTANVHQOZJBEIDGLWCMXFPURS
Right alphabet (plaintext): CXKSTZEWLPNBRFJDIVQGOUMHYA
```
Two indices into this alphabet are given special names: the *zenith* (`+`), at index 0; and the *nadir* (`*`), at index
(alphabet length) &div; 2, which for the standard English alphabet is index 13.

For each letter in the plaintext, begin by locating that letter in the right alphabet and appending the letter at the
corresponding index in the left alphabet to the ciphertext.  Let's decode the message `HELLOWORLD`.  `H` is at index 23
in the right alphabet, which corresponds to `U` in the left alphabet:
```
                            +            *         |
Left alphabet (ciphertext): YKTANVHQOZJBEIDGLWCMXFPURS
Right alphabet (plaintext): CXKSTZEWLPNBRFJDIVQGOUMHYA
```
Now, we must *permute* both alphabets.  We shall permute the left alphabet by *rotating* it until the target cipher
letter is at the zenith; for this letter, that means that we want to rotate the alphabet such that `U` is in index 0.
Next, extract the letter at index 1 and shift the letters in indices [2, nadir] leftward one position.  Finally, insert
the extracted letter back into the alphabet at the nadir.  For our sample alphabet, this process would work as follows:
```
          +            *
Original: YKTANVHQOZJBEIDGLWCMXFPURS
Rotate:   URSYKTANVHQOZJBEIDGLWCMXFP
Extract:  U.SYKTANVHQOZJBEIDGLWCMXFP  (R)
Shift:    USYKTANVHQOZJ.BEIDGLWCMXFP  (R)
Reinsert: USYKTANVHQOZJRBEIDGLWCMXFP
```
The right alphabet follows a similar, albeit slightly different, process.  Begin by rotating the right alphabet so that
the target plaintext letter is at the end of the alphabet; for this letter, that means that we want to rotate the
alphabet so that `H` is in index 25.  Next, extract the letter at index **2** (not 1!) and shift the letters in indices
[3, nadir] leftward one position.  Finally, insert the extracted letter back into the alphabet at the nadir.  For the
sample alphabet:
```
          +            *
Original: CXKSTZEWLPNBRFJDIVQGOUMHYA
Rotate:   YACXKSTZEWLPNBRFJDIVQGOUMH
Extract:  YA.XKSTZEWLPNBRFJDIVQGOUMH  (C)
Shift:    YAXKSTZEWLPNB.RFJDIVQGOUMH  (C)
Reinsert: YAXKSTZEWLPNBCRFJDIVQGOUMH
```
Now that you've permuted both alphabets, repeat this procedure with the next letter in the plaintext and the new left
and right alphabets.  For the entire sample message, the process looks as follows:
```
LEFT (ciphertext)             RIGHT (plaintext)           |  CT  PT
----------------------------------------------------------+--------
YKTANVHQOZJBEIDGLWCMXFPURS    CXKSTZEWLPNBRFJDIVQGOUMHYA  |  U   H
USYKTANVHQOZJRBEIDGLWCMXFP    YAXKSTZEWLPNBCRFJDIVQGOUMH  |  V   E
VQOZJRBEIDGLWHCMXFPUSYKTAN    WLNBCRFJDIVQGPOUMHYAXKSTZE  |  Q   L
QZJRBEIDGLWHCOMXFPUSYKTANV    NBRFJDIVQGPOUCMHYAXKSTZEWL  |  V   L
VZJRBEIDGLWHCQOMXFPUSYKTAN    NBFJDIVQGPOUCRMHYAXKSTZEWL  |  W   O
WCQOMXFPUSYKTHANVZJRBEIDGL    UCMHYAXKSTZEWRLNBFJDIVQGPO  |  T   W
TANVZJRBEIDGLHWCQOMXFPUSYK    RLBFJDIVQGPOUNCMHYAXKSTZEW  |  G   O
GHWCQOMXFPUSYLKTANVZJRBEID    UNMHYAXKSTZEWCRLBFJDIVQGPO  |  K   R
KANVZJRBEIDGHTWCQOMXFPUSYL    LBJDIVQGPOUNMFHYAXKSTZEWCR  |  K   L
KNVZJRBEIDGHTAWCQOMXFPUSYL    BJIVQGPOUNMFHDYAXKSTZEWCRL  |  A   D

Plaintext:  HELLOWORLD
Ciphertext: UVQVWTGKKA
```
To decode an encoded message, follow the exact same process, but looking for each character in the ciphertext in the
left alphabet instead of the plaintext characters in the right alphabet.

Your task is to implement Chaocipher encoding of a `String` plaintext message given left and right alphabets encoded in
`char` arrays.  Your solution must work for the *general* case: that is, the user should be able to pass in alphabets in
any script, including non-Latin alphabets, and your solution should correctly encode the message in those scripts.
**Your solution must not create any additional data structures save for `char` arrays, `String`s, and
`StringBuilder`s**, although you may write whatever helper methods you see fit.  You may **not** use any additional
classes from the Java standard library.
