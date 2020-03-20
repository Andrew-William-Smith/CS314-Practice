# Free Lists
[![Difficulty: Medium](https://img.shields.io/badge/difficulty-medium-yellow)]()
[![Skill: Linked List](https://img.shields.io/badge/skill-linked%20list-blue)]()
[![Also in CS 429](https://img.shields.io/badge/also%20in-CS%20429-blueviolet)]()

The garbage collector is one of the most important aspects of the Java runtime, as it allows programmers to instantiate
millions of nested objects without running out of memory or having to manually free memory.  The primary Java garbage
collector uses a version of an algorithm known as *mark-sweep*, in which objects are marked as garbage once they have no
referents from instance variables or global data.  To facilitate this garbage collection strategy, the JVM maintains a
data structure known as a *free list*, which is a type of linked list that stores a region of free memory in each node.
When memory is required to allocate an object, a free block is either occupied in full or split in order to accommodate
the new object; this method allows small objects to be allocated quickly, but may lead to slight redundancies in the
list when objects are deallocated, leading to memory waste&mdash;or *internal fragmentation*&mdash;over time.

In order to save memory, most free list systems implement a process called *compaction*, in which list nodes
representing contiguous blocks of memory are merged into a single node.  Blocks are said to be *contiguous* if the
starting address and length of the first add up to the starting address of the second.  If blocks are not contiguous
&mdash;that is, they are *discrete*&mdash;they should not be compacted.  Some examples of free list compaction are shown
below.  Free lists are comprised of `FLNode` instances, each of which is shown as a 2-tuple of the form
`(startAddress, length)` in the problem examples.  Each `FLNode` has the following properties:

- `int startAddress` &mdash; The starting address of the memory block represented by the node.
- `int length` &mdash; The length of the block in bytes.
- `FLNode next` &mdash; The next node in the free list.

Some examples of list compaction are given below:
- `[(240, 20), (270, 15), (285, 15), (350, 8), (360, 10), (370, 16)]`
  &rArr; `[(240, 20), (270, 30), (350, 8), (360, 26)]`
- `[(0, 84), (84, 16), (100, 20), (120, 80), (220, 16)]`
  &rArr; `[(0, 200), (220, 16)]`
- `[(0, 100), (100, 100), (200, 100), (300, 100)]`
  &rArr; `[(0, 400)]`

Your task is to implement free list compaction in the method `FreeList::compact()`.  Once your solution works correctly,
all tests in the fixture `FreeListCompactionTest` should pass.  Pay particular attention to the structure of the inner
class `FLNode`, and don't be afraid to use the `FreeList::toString()` method to help debug your compaction. Don't worry
about the methods `FreeList::allocate()` and `FreeList::free()`; they're just there to allow testing of the list and to
give some more context as to what a free list is and how it works.  You don't need to modify them to successfully
complete this problem.  Note that **you may not instantiate any additional data structures** to solve this problem, and
your solution should be as efficient as possible in terms of both time and space.
