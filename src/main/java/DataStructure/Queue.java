package DataStructure;

public class Queue {
    private Node front;
    private Node rear;
    private int size;

    public Queue() {
        front = rear = null;
    }

    public void enqueue(Object element) {
        Node newNode = new Node(element);
        if(rear == null) {
            front = rear = newNode;
        }else{
            rear.setNext(newNode);
            rear = newNode;
        }
        size++;
    }

    public Object dequeue() {
        if(isEmpty()){
            return null;
        }

        Object element = front.getElement();
        front = front.getNext();
        size--;

        if(isEmpty()){
            rear = null;
        }
        return element;

    }

    public Object peek() {
        if(isEmpty()){
            return null;
        }else{
            return front.getElement();
        }
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int getSize() {
        return size;
    }


}
