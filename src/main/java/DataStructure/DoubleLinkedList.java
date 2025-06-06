package DataStructure;

public class DoubleLinkedList {
    private NodeDouble front, back;
    private int size;

    public DoubleLinkedList() {
        front = back = null;
        size = 0;
    }

    public void addFirst(NodeDouble newNode) {
        if (size == 0) {
            front = back = newNode;
        } else {
            newNode.setNext(front);
            front.setPrevious(newNode);
            front = newNode;
        }
        size++;
    }

    public NodeDouble getFirst() {
        return front;
    }

    public void addLast(NodeDouble newNode) {
        if (size == 0) {
            front = back = newNode;
        } else {
            back.setNext(newNode);
            newNode.setPrevious(back);
            back = newNode;
        }
        size++;
    }

    public NodeDouble getLast() {
        return back;
    }

    private void printList(NodeDouble current) {
        if (current != null) {
            System.out.println(current.getCategoryID() + " " + current.getCategoryName() + " " + current.getCategoryDescription());

            printList(current.getNext());
        }
    }

    public void printList() {
        printList(front);
    }

    public boolean remove(Object element) {
        if (size == 0) return false;

        NodeDouble current = front;
        while (current != null) {
            if (current == element || current.equals(element)) {
                if (current.getPrevious() != null) {
                    current.getPrevious().setNext(current.getNext());
                } else {
                    front = current.getNext();
                }

                if (current.getNext() != null) {
                    current.getNext().setPrevious(current.getPrevious());
                } else {
                    back = current.getPrevious();
                }

                size--;
                return true;
            }
            current = current.getNext();
        }
        return false;
    }
}
