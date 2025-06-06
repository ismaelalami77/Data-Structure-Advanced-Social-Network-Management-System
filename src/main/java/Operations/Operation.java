package Operations;

import DataStructure.NodeDouble;
import Product.Product;
import Shipment.Shipment;

public class Operation {
    private String operation;
    private Shipment shipment;
    private NodeDouble category;
    public final static String CANCEL = "Cancel";
    public final static String Approve = "Approve";



    public Operation(String operation, Shipment shipment, NodeDouble category) {
        this.operation = operation;
        this.shipment = shipment;
        this.category = category;
    }

    public String getOperation() {
        return operation;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public NodeDouble getCategory() {
        return category;
    }
}


