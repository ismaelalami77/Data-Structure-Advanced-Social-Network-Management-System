package DataStructure;

public class Stack {
    private int size;
    private Node front;

    public Stack() {
        front = null;
        size = 0;
    }

    public void push(Object element) {
        Node newNode = new Node(element);

        newNode.setNext(front);
        front = newNode;
        size++;
    }

    public Object pop() {
        if(!isEmpty()){
            Node top = front;
            front = front.getNext();
            size--;
            return top.getElement();
        }else{
            return null;
        }
    }

    public Object peek() {
        if(!isEmpty()){
            return front.getElement();
        }else{
            return null;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (front == null);
    }

    public void clear() {
        while(!isEmpty()){
            pop();
        }
    }
}
