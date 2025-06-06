package DataStructure;

import Product.Product;


public class NodeDouble {
    private static int counter = 1;
    private String categoryID;
    private String categoryName;
    private String categoryDescription;

    private Queue shipmentQueue;

    private Cursor inventoryStockList;
    private int inventoryStockListHead;
    private Cursor cancelledShipmentsList;
    private int cancelledShipmentsListHead;

    private Stack undoStack;
    private Stack redoStack;

    private LinkedList products;

    private NodeDouble next, previous;

    public NodeDouble(String categoryName, String categoryDescription) {
        this.categoryID = generateCategoryID();
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;

        this.shipmentQueue = new Queue();
        this.inventoryStockList = new Cursor();
        this.inventoryStockListHead = inventoryStockList.createList();
        this.undoStack = new Stack();
        this.redoStack = new Stack();
        this.cancelledShipmentsList = new Cursor();
        this.cancelledShipmentsListHead = inventoryStockList.createList();
        this.products = new LinkedList();
        this.next = null;
        this.previous = null;
    }

    private String generateCategoryID() {
        return String.format("C%03d", counter++);
    }

    public static String getCurrentCategoryID() {
        return String.format("C%03d", counter);
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public NodeDouble getNext() {
        return next;
    }

    public void setNext(NodeDouble next) {
        this.next = next;
    }

    public NodeDouble getPrevious() {
        return previous;
    }

    public void setPrevious(NodeDouble previous) {
        this.previous = previous;
    }

    public Queue getShipmentQueue() {
        return shipmentQueue;
    }

    public void setShipmentQueue(Queue shipmentQueue) {
        this.shipmentQueue = shipmentQueue;
    }

    public Stack getUndoStack() {
        return undoStack;
    }

    public void setUndoStack(Stack undoStack) {
        this.undoStack = undoStack;
    }

    public Stack getRedoStack() {
        return redoStack;
    }

    public void setRedoStack(Stack redoStack) {
        this.redoStack = redoStack;
    }

    public LinkedList getProducts() {
        return products;
    }

    public void setProducts(LinkedList products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.addLast(product);
    }

    public int getInventoryStockListHead() {
        return inventoryStockListHead;
    }

    public void setInventoryStockListHead(int inventoryStockListHead) {
        this.inventoryStockListHead = inventoryStockListHead;
    }

    public int getCancelledShipmentsListHead() {
        return cancelledShipmentsListHead;
    }

    public void setCancelledShipmentsListHead(int cancelledShipmentsListHead) {
        this.cancelledShipmentsListHead = cancelledShipmentsListHead;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        NodeDouble.counter = counter;
    }

    public Cursor getInventoryStockList() {
        return inventoryStockList;
    }

    public void setInventoryStockList(Cursor inventoryStockList) {
        this.inventoryStockList = inventoryStockList;
    }

    public Cursor getCancelledShipmentsList() {
        return cancelledShipmentsList;
    }

    public void setCancelledShipmentsList(Cursor cancelledShipmentsList) {
        this.cancelledShipmentsList = cancelledShipmentsList;
    }


    @Override
    public String toString() {
        return "NodeDouble{" +
                "shipmentQueue=" + shipmentQueue +
                ", undoStack=" + undoStack +
                ", redoStack=" + redoStack +
                ", products=" + products +
                ", next=" + next +
                ", previous=" + previous +
                '}';
    }
}
