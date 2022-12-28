package deque;

public class LinkedListDeque<T> implements Deque<T> {
// creating the private IntListNode class
    private class IntListNode {
        public T item;
        public IntListNode next;
        public IntListNode prev;
// Initializing the IntListNode class
        public IntListNode(IntListNode prev, T item, IntListNode next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private IntListNode sentinel;
    private int size;
// Initializing LinkedListDeque with a next and prev that essentially creates a circular Doubly linked list
    public LinkedListDeque() {
        sentinel = new IntListNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }
// We create a new node that is essentially the first node in the list
    // the original sentinel.next has a previous node that is the new node that is being added
    // the sentinel's next pointer is also assigned to the new node now
    @Override
    public void addFirst(T item) {
        IntListNode FirstNode = new IntListNode(sentinel, item, sentinel.next);
        sentinel.next.prev = FirstNode;
        sentinel.next = FirstNode;
        size += 1;
    }
// We assign the next of the current last node (in the list) to be the newly added node
    // sentinel's previous pointer is now assigned to the newly added node
    @Override
    public void addLast(T item) {
        IntListNode LastNode = new IntListNode(sentinel.prev, item, sentinel);
        sentinel.prev.next = LastNode;
        sentinel.prev = LastNode;
        size += 1;
    }
    @Override
    public int size() {
        return size;
    }
// In order to maintain the correct spacing we start the string with the current item and a space
    // after that as each item in the list is added we add a space between the current string and the newly added item
    // the while loop shuts when we hit the end of the list which is also the sentinel
    @Override
    public void printDeque() {
        IntListNode Current = sentinel.next;
        String s = Current.item + " ";
        while (Current.next != sentinel) {
            Current = Current.next;
            s = s + " " + Current.item;
        }
        System.out.print(s);
        System.out.println();
    }
// to make the second item the new first node, we assign it as sentinel's next item
    // the second item's previous is reassigned to the sentinel thus making it to the new head node
    // the size of the list is reduced
    // the head (removed Node) is also returned
    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        } else {
            IntListNode head = sentinel.next;
            sentinel.next = head.next;
            head.next.prev = sentinel;
            size -= 1;
            return head.item;
        }
    }
// the previous node of the sentinel is assigned to the previous node of the current last node
    // to do this the second last item's next node is assigned to be sentinel
    // the size is then reduced by 1
    // the method returns the last Node of the list that was removed from the list
    @Override
    public T removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        } else {
            IntListNode LastNode = sentinel.prev;
            sentinel.prev = LastNode.prev;
            LastNode.prev.next = sentinel;
            size -= 1;
            return LastNode.item;
        }
    }
// we introduce a variable called initial that keeps checking whether it is equal to index or not
    // the node is switched to the next one if it is not until we reach the end of the list in which case we return null
    @Override
    public T get(int index) {
        if (index > size) {
            return null;
        }
        int initial = 0;
        IntListNode current = sentinel.next;
        while (current != sentinel) {
            if (initial == index) {
                return current.item;
            }
            initial++;
            current = current.next;
        }
        return null;
    }
    // this recursive helper method has been created for the getRecursive method
    // the method recursively keeps calling the next node with an increase in the index
    // when the currentIndex and targetIndex are equal, we return the item of the current node
    private T helper(int currentIndex, IntListNode node, int targetIndex) {
        if (currentIndex == targetIndex) {
            return node.item;
        } else {
            return helper((currentIndex + 1), node.next, targetIndex);
        }
    }
// the helper method has been created to simplify this recursion
    public T getRecursive(int index) {
        if (index > size) {
            return null;
        } else {
            return helper(0, sentinel.next, index);
        }
    }
// in order to check whether the Object is of the same class, we check if the class and package name are the same or not
    // we further check that if the sizes are unequal, then it should return false
    // we create a for loop to sequentially check if items of both the lists are the same or not
    // we return true if the object has the same size, is of the type Deque and has the elements in the same order
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        else if (!getClass().equals(o.getClass())) {
            return false; }
        Deque other = (Deque) o;
        if (size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size(); i ++) {
            if (!get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }
}
