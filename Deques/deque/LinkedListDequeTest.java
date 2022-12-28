package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list deque tests. */
public class LinkedListDequeTest {

    /** You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * LinkedListDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. Please do not import java.util.Deque here!*/

    public static Deque<Integer> lld = new LinkedListDeque<Integer>();

    @Test
    /** Adds a few things to the list, checks that isEmpty() is correct.
     * This is one simple test to remind you how junit tests work. You
     * should write more tests of your own.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        lld = new LinkedListDeque<Integer>();
		assertTrue("A newly initialized LLDeque should be empty", lld.isEmpty());
		lld.addFirst(0);

        assertFalse("lld1 should now contain 1 item", lld.isEmpty());

        lld = new LinkedListDeque<Integer>(); //Assigns lld equal to a new, clean LinkedListDeque!
    }

    /** Adds an item, removes an item, and ensures that dll is empty afterwards. */
    @Test
    public void addRemoveTest() {
        lld = new LinkedListDeque<Integer>();
        lld.addFirst(0);
        lld.removeFirst();
        assertTrue(lld.isEmpty());

    }
    /** Make sure that removing from an empty LinkedListDeque does nothing */
    @Test
    public void removeEmptyTest() {
        lld = new LinkedListDeque<Integer>();
        lld.addFirst(10);
        lld.removeFirst();
        assertEquals(null, lld.removeFirst());
    }
    /** Make sure your LinkedListDeque also works on non-Integer types */
    @Test
    public void multipleParamsTest() {
        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();
        lld1.addFirst("Hi");
        lld1.removeFirst();
        assertEquals(0, lld1.size());
        }
        /** Make sure that removing from an empty LinkedListDeque returns null */
        @Test
        public void emptyNullReturn () {
            lld = new LinkedListDeque<Integer>();
            lld = new LinkedListDeque<Integer>();
            lld.addFirst(10);
            lld.removeFirst();
            assertEquals(null, lld.removeFirst());
        }
        /** TODO: Write tests to ensure that your implementation works for really large
         * numbers of elements, and test any other methods you haven't yet tested!
         */
        @Test
        public void testAddFirst () {
            lld = new LinkedListDeque<Integer>();
            lld.addFirst(10);
            int first = lld.get(0);
            assertEquals(10, first);
        }

        @Test
        public void testAddLast () {
            lld = new LinkedListDeque<Integer>();
            lld.addFirst(10);
            lld.addLast(15);
            int last = lld.get(1);
            assertEquals(15, last);
        }

        @Test
        public void testAddLast2 () {
            lld = new LinkedListDeque<Integer>();
            for (int initial = 0; initial < 50; initial++) {
                lld.addLast(10);
            }
            int last = lld.get(49);
            assertEquals(10, last);
            assertFalse(lld.isEmpty());
        }
        @Test
        public void testisEmpty () {
            lld = new LinkedListDeque<Integer>();
            lld.addFirst(10);
            lld.addLast(15);
            lld.removeFirst();
            assertFalse(lld.isEmpty());
        }

        @Test
        public void testisEmpty2 () {
            lld = new LinkedListDeque<Integer>();
            lld.addFirst(10);
            lld.addLast(15);
            lld.removeFirst();
            lld.removeLast();
            assertTrue(lld.isEmpty());
        }

        @Test
        public void testSize () {
            lld = new LinkedListDeque<Integer>();
            lld.addFirst(10);
            assertEquals(1, lld.size());
        }

        @Test
        public void testSize2 () {
            lld = new LinkedListDeque<Integer>();
            for (int initial = 0; initial < 50; initial++) {
                lld.addLast(10);
            }
            assertEquals(50, lld.size());
        }

        @Test
        public void printDeque () {
        }

        @Test
        public void testRemoveFirst () {
            lld = new LinkedListDeque<Integer>();
            lld.addFirst(10);
            lld.addLast(15);
            lld.addLast(20);
            lld.removeFirst();
            int first = lld.get(0);
            assertEquals(15, first);
        }

        @Test
        public void testRemoveLast () {
            lld = new LinkedListDeque<Integer>();
            lld.addFirst(10);
            lld.addLast(15);
            lld.addLast(20);
            lld.removeLast();
            int last = lld.get(1);
            assertEquals(15, last);
        }

        public void testRemoveLast2 () {
            lld = new LinkedListDeque<Integer>();
            lld.addFirst(10);
            lld.removeFirst();
            assertEquals(null, lld.removeFirst());
        }

        @Test
        public void testGet () {
            lld = new LinkedListDeque<Integer>();
            lld.addFirst(10);
            lld.addLast(15);
            int first = lld.get(1);
            assertEquals(15, first);
        }
        @Test
        public void equals () {
        }

}
