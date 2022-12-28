package deque;

public class ArrayDeque<T> implements Deque<T> {
    private int size, nextLast, nextFirst, arraysize;
    private T[] a;

    public ArrayDeque() {
        size = 0;
        nextLast = 0;
        nextFirst = 0;
        arraysize=8;
        a = (T[]) new Object[arraysize];
    }
    private int subtract(int n) {
        n = (n - 1);
        if (n == -1) {
            n = (a.length - 1);
        }
        return n;
    }

    private boolean mod25(int s, int l) //returns true if the ratio of filled elements is less than 25%
    {
        if ((s / (double) l) < 0.25) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index)
    {
        //int size = size();
        if (index < 0 || index >= size)
        {
            return null;
        }
        int store;
        store = (index+(nextFirst));
        return a[store%(a.length)];
    }

    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        int s = size();
        int last = s + 1;
        int i = 0;
        /*while(s>0) {
            s--;
            temp[i++] = a[nextFirst];
            if (nextFirst == last) {
                nextFirst = 0;
            }*/
        for (int l = 0; l<size;l++)
        {
            temp[l] = a[(l+nextFirst)%(a.length)];
        }
        a=temp;
        nextFirst = 0;
        nextLast = size-1;
    }
    @Override
    public void addLast(T item) {
        //int s = size();
        if (size == 0)
        {
            nextFirst=0;
            nextLast=0;
            a[nextLast]=item;
            size++;
            return;
        }
        else {
            if (a.length == size) {
                resize(a.length * 2);
                /*nextLast = size - 1;
                a[nextLast] = item;
                size++;
                nextLast = nextLast + 1; //assigning the value to the end of all current values
                nextFirst = 0; //pushing nextFirst to the opposite end, to allow space for values to be stored in between and prevent overriding */
            }
            if (nextLast == (a.length - 1)) {
                nextLast = 0;
                a[nextLast] = item;
                size++;
            } else {
                size++;
                nextLast = (nextLast + 1);
                a[nextLast] = item;
            /*if (size()==1)
            {
                nextFirst = subtract(nextFirst);
            }*/
            }

        }
    }
    @Override
    public void addFirst(T item) {
        //int s = size();
        if (size == 0) {
            nextFirst = 0;
            nextLast = 0;
            a[nextLast]=item;
            size++;
            return;
        } else {
            if (a.length == size) {
                resize(a.length * 2);
                //nextFirst = 0;
                /*a[subtract(nextFirst)] = item;
                size++;
                //nextLast = size - 1; //nextLast comes to the next spot, after all stored variables
                nextFirst = subtract(nextFirst); //value of nextFirst becomes one prior to the end of the array*/
            }
            if (nextFirst==0) //same as addlast, just opposite
            {
                size++;
                nextFirst = a.length - 1; //circular, push to opposite end
                a[nextFirst]=item;
            }
            else {
                size++;
                nextFirst = subtract(nextFirst);
                a[nextFirst] = item;
            /*if (size==1)
            {
                nextLast = (nextLast+1)%(a.length);
            }*/
            }
        }
    }


    @Override
    public T removeFirst() {
        //int s = size();
        if (size == 0) {
            return null;
        }
        //nextFirst = ((nextFirst + 1) % (a.length)); //reverting to previous position with value
        T value = a[nextFirst];
        a[nextFirst] = null;
        size--;
        if (size == 0) {
            nextFirst = 0;
            nextLast = 0;
        }else{
            nextFirst = ((nextFirst + 1) % (a.length));
        }
        if (a.length >= 16 && (mod25(size, a.length))) {
            resize((a.length) / 2);
                /*nextFirst = 0;
                nextLast = size - 1;*/
        }
        return value;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        //nextLast = subtract(nextLast); //reverting to previous position with value
        T value = a[nextLast];
        a[nextLast] = null;
        size--;
        if (size == 0) {
            nextFirst = 0;
            nextLast = 0;
        }
        else {
            nextLast = subtract(nextLast);
        }
        if (size >= 16 && (mod25(size, a.length))) {
            resize((a.length) / 2);
            /*nextFirst = 0;
            nextLast = size-1;*/
        }
        return value;
    }
    @Override
    public void printDeque() {
        //int size = size();
        if (nextFirst==0) {
            for (int i = nextFirst+1; i <= size; i++) {
                System.out.println(a[i]);
            }
        }
        else if (nextFirst == a.length-1)
        {
            for (int i = 0;i<=size-1;i++)
            {
                System.out.println(a[i]);
            }
        }
        else if (nextLast == 0)
        {
            for (int i = nextFirst+1;i<a.length;i++)
            {
                System.out.println(a[i]);
            }
        }
        else if (nextFirst>=nextLast)
        {
            for (int i = 0;i<nextLast;i++)
            {
                System.out.println(a[i]);
            }
            for (int j = nextFirst+1; j<a.length;j++)
            {
                System.out.println(a[j]);
            }
        }
        else {
            for (int i = nextFirst+1;i<nextLast;i++)
            {
                System.out.println(a[i]);
            }
        }
        // considered 1 2 3 4 - - -?
    }
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
