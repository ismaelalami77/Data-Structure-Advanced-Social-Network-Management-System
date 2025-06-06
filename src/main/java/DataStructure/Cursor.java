package DataStructure;

public class Cursor {
    private final static int MAX_SIZE = 10000;
    private CursorNode[] cursorArray;
    private int counter = 0;

    public Cursor() {
        initialization();
    }

    public void initialization() {
        cursorArray = new CursorNode[MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++) {
            cursorArray[i] = new CursorNode(null, i + 1);
        }
        cursorArray[MAX_SIZE - 1].setNext(0);
    }

    public int cursorAlloc() {
        int p = cursorArray[0].getNext();
        cursorArray[0].setNext(cursorArray[p].getNext());
        return p;
    }

    public int createList() {
        int l = cursorAlloc();
        if (l == 0) {
            System.out.println("Error out of space");
        } else {
            cursorArray[l] = new CursorNode("-", 0);
        }
        return l;
    }

    private void cursorFree(int p) {
        cursorArray[p].setElement(null);
        cursorArray[p].setNext(cursorArray[0].getNext());
        cursorArray[0].setNext(p);
    }

    public boolean isNull(int l) {
        return cursorArray[l] == null;
    }

    public boolean isEmpty(int l) {
        return cursorArray[l].getNext() == 0;
    }

    public boolean isLast(int p) {
        return cursorArray[p].getNext() == 0;
    }


    public void insertAtHead(Object data, int l) {
        if (isNull(l)) {
            return;
        }
        int p = cursorAlloc();
        if (p != 0) {
            cursorArray[p] = new CursorNode(data, cursorArray[l].getNext());
            cursorArray[l].setNext(p);
            counter++;
        } else {
            System.out.println("out of space");
        }
    }

    public int find(Object data, int l) {
        int p = cursorArray[l].getNext();

        while ((p != 0) && !cursorArray[p].getElement().equals(data)) {
            p = cursorArray[p].getNext();
        }
        return p;
    }

    public void insert(Object data, int l, int p) {
        if (isNull(l)) {
            return;
        }
        int temp = cursorAlloc();
        if (temp != 0) {
            cursorArray[temp] = new CursorNode(data, cursorArray[p].getNext());
            cursorArray[p].setNext(temp);
        } else {
            System.out.println("out of space");
        }
    }

    public void traverseList(int l) {
        if (isNull(l)) {
            return;
        }
        int p = cursorArray[l].getNext();
        while (p != 0) {
            System.out.print(cursorArray[p].getElement() + " -> ");
            p = cursorArray[p].getNext();
        }
        System.out.println("null");
    }

    public int findPrevious(Object data, int l) {
        int p = l;
        while (cursorArray[p].getNext() != 0 &&
                !cursorArray[cursorArray[p].getNext()].getElement().equals(data)) {
            p = cursorArray[p].getNext();
        }
        return p;
    }

    public void remove(Object data, int l) {
        int pos = findPrevious(data, l);
        if (cursorArray[pos].getNext() != 0) {
            int temp = cursorArray[pos].getNext();
            cursorArray[pos].setNext(cursorArray[temp].getNext());
            cursorFree(temp);
            counter--;
        }
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
