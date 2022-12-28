package deque;

import org.junit.Test;

import static org.junit.Assert.*;

/* Performs some basic array deque tests. */
public class ArrayDequeTest {

    /** You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * ArrayDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. */

    public static Deque<Integer> ad = new ArrayDeque<Integer>();

    @Test
    public void testAddFirst() {
        ad = new ArrayDeque<Integer>();
        ad.addFirst(10);
        int initial = ad.get(0);
        assertEquals(10, initial);
    }

    @Test
    public void testAddFirst2() {
        ad = new ArrayDeque<Integer>();
        for (int i = 0; i < 50; i ++) {
            ad.addFirst(100);
            int initial = ad.get(0);
            assertEquals(100, initial);
        }
    }

    @Test
    public void testAddLast() {
        ad = new ArrayDeque<Integer>();
        ad.addFirst(10);
        ad.addLast(15);
        int last = ad.get(1);
        assertEquals(15, last);
    }

    @Test
    public void testisEmpty() {
        ad = new ArrayDeque<Integer>();
        ad.addFirst(10);
        ad.removeFirst();
        assertTrue(ad.isEmpty());
    }
    @Test
    public void testisEmpty2() {
        ad = new ArrayDeque<Integer>();
        ad.addFirst(10);
        ad.addLast(20);
        ad.removeFirst();
        assertFalse(ad.isEmpty());
    }

    @Test
    public void size() {
        ad = new ArrayDeque<Integer>();
        for (int i = 0; i < 100; i ++) {
            ad.addFirst(10);
        }
        assertEquals(100, ad.size());
    }

    @Test
    public void testSize() {
        ad = new ArrayDeque<Integer>();
        ad.addFirst(10);
        ad.removeFirst();
        assertEquals(0, ad.size());
    }

    @Test
    public void testPrintDeque() {

    }

    @Test
    public void testRemoveFirst() {
        ad = new ArrayDeque<Integer>();
        ad.addFirst(10);
        for (int i = 0; i < 50; i ++) {
            ad.addLast(20);
        }
        ad.removeFirst();
        int first = ad.get(0);
        assertEquals(20, first);
    }

    @Test
    public void testRemoveLast() {
        ad = new ArrayDeque<Integer>();
        for (int i = 0; i < 50; i++) {
            ad.addFirst(50);
        }
        ad.addLast(100);
        ad.removeLast();
        int last = ad.get(49);
        assertEquals(50, last);
    }

    @Test
    public void testGet() {
        ad = new ArrayDeque<Integer>();
        ad.addFirst(20);
        ad.addLast(10);
        int last = ad.get(1);
        assertEquals(10, last);
    }

    @Test
    public void equals() {

    }
}
