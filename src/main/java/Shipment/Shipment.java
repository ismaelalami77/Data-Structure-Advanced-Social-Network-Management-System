package Shipment;

import java.util.Date;

public class Shipment {
    private static int counter = 1;
    private String shipmentId;
    private String productId;
    private int quantity;
    private Date date;

    public Shipment(String productId, int quantity, Date date) {
        this.shipmentId = generateShipmentId();
        this.productId = productId;
        this.quantity = quantity;
        this.date = date;
    }

    private String generateShipmentId(){
        return String.format("S%03d", counter++);
    }

    public static String getCurrentShipmentId(){
        return String.format("S%03d", counter);
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "shipmentId='" + shipmentId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", date=" + date +
                '}';
    }
}
