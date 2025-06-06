package DataStructure;

public class LinkedList {
    private Node front, back;
    private int size;

    public LinkedList() {
        front = back = null;
        size = 0;
    }

    public void addFirst(Object element) {
        Node newNode = new Node(element);
        if (size == 0) {
            front = back = newNode;
        } else {
            newNode.setNext(front);
            front = newNode;
        }
        size++;
    }

    public Node getFirst() {
        return front;
    }

    public void addLast(Object element) {
        Node newNode = new Node(element);
        if (size == 0) {
            front = back = newNode;
        }else{
            back.setNext(newNode);
            back = newNode;
        }
        size++;
    }

    public Node getLast() {
        return back;
    }

    public void add(int index, Object element) {
        if (index == 0) addFirst(element);
        else if(index == size - 1) addLast(element);
        else if(index > 0 && index < size - 1) {
            Node newNode = new Node(element);
            Node current = front;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.setNext(newNode);
            size++;
        }
    }

    public boolean removeFirst(){
        if (size == 0) return false;
        else if (size == 1) {
            front = back = null;
        }
        else{
            front = front.getNext();
        }
        size--;
        return true;
    }

    public boolean removeLast(){
        if (size == 0) return false;
        else if (size == 1) {
            front = back = null;
        }else{
            Node current = front;
            for (int i = 0; i < size - 2; i++) {
                current = current.getNext();
            }
            current.setNext(null);
            back = current;
        }
        size--;
        return true;
    }

    private void printList(Node current) {
        if(current != null) {
            System.out.println(current.getElement());
            printList(current.getNext());
        }
    }
    public boolean remove(Object element) {
        if (front == null) return false;

        if (front.getElement().equals(element)) {
            front = front.getNext();
            if (front == null) back = null;
            size--;
            return true;
        }

        Node current = front;
        while (current.getNext() != null) {
            if (current.getNext().getElement().equals(element)) {
                current.setNext(current.getNext().getNext());
                if (current.getNext() == null) back = current;
                size--;
                return true;
            }
            current = current.getNext();
        }

        return false;
    }


    public void printList(){
        printList(front);
    }
}
