package Operations;

import Shipment.Shipment;

import java.util.Date;

public class ShipmentLog {
    private static StringBuilder stringBuilder = new StringBuilder();

    public static void log(String action, Shipment shipment, int quantity) {
        Date date = shipment.getDate();
        String dateString = date.toString();
        String shipmentId = shipment.getShipmentId();
        String productId = shipment.getProductId();
        String sign;
        if (action.equalsIgnoreCase("Add Shipment")) {
            sign = "+";
        } else {
            sign = "-";
        }

        String string = dateString + " | " + action + " | " +
                shipmentId + " | " + productId + " | " + sign + quantity;

        stringBuilder.append(string+"\n");
    }

    public static StringBuilder getStringBuilder() {
        return stringBuilder;
    }
}
