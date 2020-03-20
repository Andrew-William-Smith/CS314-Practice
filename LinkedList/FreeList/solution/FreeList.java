/* CS 314 PRACTICE PROBLEM --- FREE LISTS (SOLUTION)
 * Skills tested: Linked list
 * Author: Andrew Smith
 * Written: 5 November 2019
 *
 * PROBLEM DESCRIPTION:
 * The garbage collector is one of the most important aspects of the Java runtime, as it allows programmers to
 * instantiate millions of nested objects without running out of memory or having to manually free memory.  The primary
 * Java garbage collector uses a version of an algorithm known as MARK-SWEEP, in which objects are marked as garbage
 * once they have no referents from instance variables or global data.  To facilitate this garbage collection strategy,
 * the JVM maintains a data structure known as a FREE LIST, which is a type of linked list that stores a region of free
 * memory in each node.  When memory is required to allocate an object, a free block is either occupied in full or split
 * in order to accommodate the new object; this method allows small objects to be allocated quickly, but may lead to
 * slight redundancies in the list when objects are deallocated, leading to memory waste --- or INTERNAL FRAGMENTATION
 * --- over time.
 *
 * In order to save memory, most free list systems implement a process called COMPACTION, in which list nodes
 * representing contiguous blocks of memory are merged into a single node.  Blocks are said to be CONTIGUOUS if the
 * starting address and length of the first add up to the starting address of the second.  If blocks are not contiguous
 * --- that is, they are DISCRETE --- they should not be compacted.  Some examples of free list compaction are shown
 * below.  Free lists are comprised of FLNode instances, each of which is shown as a 2-tuple of the form (startAddress,
 * length).
 *
 * EXAMPLES:
 * - [(240, 20), (270, 15), (285, 15), (350, 8), (360, 10), (370, 16)] => [(240, 20), (270, 30), (350, 8), (360, 26)]
 * - [(0, 84), (84, 16), (100, 20), (120, 80), (220, 16)] => [(0, 200), (220, 16)]
 * - [(0, 100), (100, 100), (200, 100), (300, 100)] => [(0, 400)]
 *
 * TASK:
 * Your task is to implement free list compaction in the method FreeList::compact().  Once your solution works
 * correctly, all tests in the fixture FreeListCompactionTest should pass.  Pay particular attention to the structure of
 * the inner class FLNode, and don't be afraid to use the FreeList::toString() method to help debug your compaction.
 * Don't worry about the methods FreeList::allocate() and FreeList::free(); they're just there to allow testing of the
 * list and to give some more context as to what a free list is and how it works.  You don't need to modify them to
 * successfully complete this problem.  Note that you may not instantiate any additional data structures to solve this
 * problem, and your solution should be as efficient as possible in terms of both time and space.
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

package LinkedList.FreeList.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("Duplicates")
public class FreeList {
    /** The first free block in this list. */
    private FLNode first;
    /** The number of free blocks in this list. */
    private int size;
    /** The number of bytes controlled by this free list. */
    private int sizeBytes;

    /**
     * FreeList constructor.  Initialises the list with one free block occupying the entire designated space.
     *
     * @param numBytes The number of bytes of memory described by this list.
     * @throws IllegalArgumentException If <code>numBytes</code> &le; 0.
     */
    public FreeList(int numBytes) {
        if (numBytes <= 0) {
            throw new IllegalArgumentException("FreeList must control at least one byte of memory");
        }
        this.first = new FLNode(0, numBytes, null);
        this.size = 1;
        this.sizeBytes = numBytes;
    }

    /**
     * Allocate the specified number of bytes of memory, removing them from the free list.  Allocation is performed
     * according to the "first-fit" strategy, in which memory is allocated from the first free block that spans at least
     * the requested amount of space.
     *
     * @param numBytes The number of bytes to allocate.
     * @return The starting address of the allocated block.
     * @throws IllegalArgumentException If <code>numBytes</code> &le; 0.
     * @throws NoSuchElementException   If there is not enough free space to allocate the requested memory.
     */
    public int allocate(int numBytes) {
        // Must allocate at least one byte
        if (numBytes <= 0) {
            throw new IllegalArgumentException("Must allocate at least one byte of memory");
        }

        // Traverse through the list until a free block of the proper size is found
        FLNode prevNode = null;
        FLNode freeNode = this.first;
        while (freeNode != null && freeNode.length < numBytes) {
            prevNode = freeNode;
            freeNode = freeNode.next;
        }
        // Ensure that a block was found
        if (freeNode == null) {
            throw new NoSuchElementException("Out of memory: The requested amount of memory could not be allocated");
        }

        // If this node perfectly fits the free block, delete it from the free list
        int start = freeNode.startAddress;
        if (freeNode.length == numBytes) {
            if (prevNode != null) {
                prevNode.next = freeNode.next;
            }
            this.size--;

            // If that was the last free block, there should be no first node
            if (this.size == 0) {
                first = null;
            }
            return start;
        }
        // Otherwise, shrink the block
        freeNode.startAddress += numBytes;
        freeNode.length -= numBytes;
        return start;
    }

    /**
     * Free the specified number of bytes of memory beginning at the specified starting address, adding them back into
     * the free list.  Already free regions of memory may not be freed.
     *
     * @param startAddress The address at which to begin freeing memory.  Must be within the address space controlled by
     *                     this free list.
     * @param numBytes     The number of bytes after the starting address to free.
     * @throws IllegalArgumentException If <code>numBytes</code> &le; 0, the range encapsulated by
     *                                  <code>startAddress + numBytes</code> exceeds the free list's address space, or
     *                                  the memory requested to be freed is already free.
     */
    public void free(int startAddress, int numBytes) {
        // Precondition checks
        if (numBytes <= 0) {
            throw new IllegalArgumentException("Must free a positive number of bytes");
        }
        if (startAddress < 0 || startAddress + numBytes > this.sizeBytes) {
            throw new IllegalArgumentException("Cannot free memory outside this list's address space");
        }

        // Find the proper place to insert the new node
        FLNode prevNode = null;
        FLNode nextNode = this.first;
        while (nextNode != null && nextNode.startAddress < startAddress) {
            prevNode = nextNode;
            nextNode = nextNode.next;
        }

        if (nextNode != null && startAddress + numBytes > nextNode.startAddress) {
            // The freed block overlaps with already-free memory
            throw new IllegalArgumentException("Cannot free memory that is already free");
        } else if (prevNode == null) {
            // This block is the new first block in the list
            this.first = new FLNode(startAddress, numBytes, nextNode);
        } else {
            // This block is elsewhere in the list
            prevNode.next = new FLNode(startAddress, numBytes, nextNode);
        }

        this.size++;
    }

    /**
     * Compact contiguous free nodes in this list, as per the problem description.  This method has no preconditions.
     */
    private void compact() {
        // If there are no free blocks, do nothing
        if (this.first == null) {
            return;
        }

        FLNode blockStart = this.first;
        for (FLNode curNode = this.first.next; curNode != null; curNode = curNode.next) {
            if (blockStart.startAddress + blockStart.length == curNode.startAddress) {
                // Boundaries align, merge nodes
                blockStart.length += curNode.length;
                this.size--;
            } else {
                // Discrete boundaries, unlink contiguous block
                blockStart.next = curNode;
                blockStart = curNode;
            }
        }

        // Relink last starting block
        blockStart.next = null;
    }

    /**
     * @return A string representation of the free blocks in this list.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("Size: " + this.size + "; Blocks: ");
        if (this.size == 0) {
            sb.append("none");
        } else {
            sb.append(this.first);
            for (FLNode node = this.first.next; node != null; node = node.next) {
                sb.append(", ");
                sb.append(node);
            }
        }
        return sb.toString();
    }

    private static class FLNode {
        /** Starting address of this free block. */
        public int startAddress;
        /** Length of this free block in bytes. */
        public int length;
        /** Next free block in the list. */
        public FLNode next;

        /**
         * FreeList free block constructor.
         *
         * @param address     The address at which this free block should begin.
         * @param blockLength The length of this free block in bytes.
         * @param nextNode    The free block following this one.  May be null to signify the end of the list.
         */
        public FLNode(int address, int blockLength, FLNode nextNode) {
            this.startAddress = address;
            this.length = blockLength;
            this.next = nextNode;
        }

        /**
         * @return A representation of this free block as a 2-tuple: (startAddress, length).
         */
        public String toString() {
            return String.format("(%d, %d)", this.startAddress, this.length);
        }
    }


    @DisplayName("FreeList harness tests (should all pass)")
    @Tag("FreeListSolution")
    public static class FreeListHarnessTest {
        private FreeList list;
        /** Each free list under test will control 1024 bytes in total. */
        private static final int MEMORY_SIZE = 1024;

        @BeforeEach
        void setUp() {
            list = new FreeList(MEMORY_SIZE);
        }

        @Test
        @DisplayName("Constructor properly initialises list")
        void constructor() {
            assertEquals(1, list.size);
            assertEquals("Size: 1; Blocks: (0, 1024)", list.toString());
        }

        @Test
        @DisplayName("Constructor precondition: numBytes > 0")
        void constructorPrecondition() {
            assertThrows(IllegalArgumentException.class, () -> new FreeList(0));
        }

        @Test
        @DisplayName("Allocate all free space at once")
        void allFreeSpaceOnce() {
            assertEquals(0, list.allocate(MEMORY_SIZE));
            assertEquals(0, list.size);
            assertEquals("Size: 0; Blocks: none", list.toString());
        }

        @Test
        @DisplayName("Cannot allocate once out of space")
        void noSpace() {
            list.allocate(MEMORY_SIZE);
            assertThrows(NoSuchElementException.class, () -> list.allocate(1));
        }

        @Test
        @DisplayName("Allocate all free space separate times")
        void allFreeSpaceMany() {
            assertEquals(0, list.allocate(512));
            assertEquals(1, list.size);
            assertEquals("Size: 1; Blocks: (512, 512)", list.toString());

            assertEquals(512, list.allocate(256));
            assertEquals(1, list.size);
            assertEquals("Size: 1; Blocks: (768, 256)", list.toString());

            assertEquals(768, list.allocate(256));
            assertEquals(0, list.size);
            assertEquals("Size: 0; Blocks: none", list.toString());
        }

        @Test
        @DisplayName("Cannot allocate non-positive sizes")
        void allocateNonPositive() {
            assertThrows(IllegalArgumentException.class, () -> list.allocate(-1));
        }

        @Test
        @DisplayName("Cannot free non-positive sizes")
        void freeNonPositiveSize() {
            int start = list.allocate(100);
            assertThrows(IllegalArgumentException.class, () -> list.free(start, 0));
        }

        @Test
        @DisplayName("Cannot free negative addresses")
        void freeNegativeAddress() {
            list.allocate(100);
            assertThrows(IllegalArgumentException.class, () -> list.free(-1, 100));
        }

        @Test
        @DisplayName("Cannot free address outside range")
        void freeAddressTooLarge() {
            list.allocate(MEMORY_SIZE);
            assertThrows(IllegalArgumentException.class, () -> list.free(MEMORY_SIZE + 1, MEMORY_SIZE));
        }

        @Test
        @DisplayName("Cannot free outside address space")
        void freeOutsideAddressSpace() {
            int start = list.allocate(MEMORY_SIZE);
            assertThrows(IllegalArgumentException.class, () -> list.free(start, MEMORY_SIZE + 1));
        }

        @Test
        @DisplayName("Cannot free already free memory")
        void freeAlreadyFreed() {
            assertThrows(IllegalArgumentException.class, () -> list.free(0, 100));
        }

        @Test
        @DisplayName("Cannot free already free memory 2")
        void freeAlreadyFreed2() {
            int start = list.allocate(100);
            assertThrows(IllegalArgumentException.class, () -> list.free(start, 101));
        }

        @Test
        @DisplayName("Free all memory")
        void freeAllMemory() {
            int start = list.allocate(MEMORY_SIZE);
            list.free(start, MEMORY_SIZE);
            assertEquals(1, list.size);
            assertEquals("Size: 1; Blocks: (0, 1024)", list.toString());
        }

        @Test
        @DisplayName("Free block in middle of list")
        void freeMiddle() {
            list.allocate(MEMORY_SIZE);
            list.free(512, 256);
            assertEquals(1, list.size);
            assertEquals("Size: 1; Blocks: (512, 256)", list.toString());
            assertThrows(IllegalArgumentException.class, () -> list.free(512, 256));
        }

        @Test
        @DisplayName("Free multiple blocks")
        void freeMultiple() {
            list.allocate(MEMORY_SIZE);
            list.free(0, 256);
            list.free(768, 256);
            assertEquals(2, list.size);
            assertEquals("Size: 2; Blocks: (0, 256), (768, 256)", list.toString());
        }

        @Test
        @DisplayName("Free small blocks in loop")
        void freeSmallLoop() {
            list.allocate(MEMORY_SIZE);
            for (int i = 0; i < MEMORY_SIZE; i++) {
                list.free(i, 1);
            }
            assertEquals(MEMORY_SIZE, list.size);
        }
    }

    @DisplayName("FreeList compaction tests")
    @Tag("FreeListSolution")
    public static class FreeListCompactionTest {
        private FreeList list;
        /**
         * Each free list under test will control 1024 bytes in total.
         */
        private static final int MEMORY_SIZE = 1024;

        @BeforeEach
        void setUp() {
            list = new FreeList(MEMORY_SIZE);
            list.allocate(MEMORY_SIZE);
        }

        @Test
        @DisplayName("No free blocks in list")
        void noFreeBlocks() {
            list.compact();
            assertEquals(0, list.size);
            assertEquals("Size: 0; Blocks: none", list.toString());
        }

        @Test
        @DisplayName("One free block")
        void oneFreeBlock() {
            list.free(0, MEMORY_SIZE);
            list.compact();
            assertEquals(1, list.size);
            assertEquals("Size: 1; Blocks: (0, 1024)", list.toString());
        }

        @Test
        @DisplayName("Two non-contiguous free blocks")
        void twoNonContiguousBlocks() {
            list.free(0, 256);
            list.free(768, 256);
            list.compact();
            assertEquals(2, list.size);
            assertEquals("Size: 2; Blocks: (0, 256), (768, 256)", list.toString());
        }

        @Test
        @DisplayName("Merge two contiguous free blocks")
        void twoContiguousBlocks() {
            list.free(0, 512);
            list.free(512, 512);
            list.compact();
            assertEquals(1, list.size);
            assertEquals("Size: 1; Blocks: (0, 1024)", list.toString());
        }

        @Test
        @DisplayName("Merge two non-contiguous ranges")
        void nonContiguousRanges() {
            list.free(0, 128);
            list.free(128, 256);
            list.free(512, 128);
            list.free(640, 384);
            list.compact();
            assertEquals(2, list.size);
            assertEquals("Size: 2; Blocks: (0, 384), (512, 512)", list.toString());
        }

        @Test
        @DisplayName("Problem description example 1")
        void descriptionExample1() {
            list.free(240, 20);
            list.free(270, 15);
            list.free(285, 15);
            list.free(350, 8);
            list.free(360, 10);
            list.free(370, 16);
            list.compact();
            assertEquals(4, list.size);
            assertEquals("Size: 4; Blocks: (240, 20), (270, 30), (350, 8), (360, 26)", list.toString());
        }

        @Test
        @DisplayName("Problem description example 2")
        void descriptionExample2() {
            list.free(0, 84);
            list.free(84, 16);
            list.free(100, 20);
            list.free(120, 80);
            list.free(220, 16);
            list.compact();
            assertEquals(2, list.size);
            assertEquals("Size: 2; Blocks: (0, 200), (220, 16)", list.toString());
        }

        @Test
        @DisplayName("Problem description example 3")
        void descriptionExample3() {
            list.free(0, 100);
            list.free(100, 100);
            list.free(200, 100);
            list.free(300, 100);
            list.compact();
            assertEquals(1, list.size);
            assertEquals("Size: 1; Blocks: (0, 400)", list.toString());
        }

        @Test
        @DisplayName("Compact single bytes")
        void compactSingleBytes() {
            for (int i = 0; i < MEMORY_SIZE; i++) {
                list.free(i, 1);
            }
            list.compact();
            assertEquals(1, list.size);
            assertEquals("Size: 1; Blocks: (0, 1024)", list.toString());
        }
    }
}
