package Statistics;

import DataStructure.*;
import Product.Product;
import Project3.MainView;
import Project3.UIHelper;
import Shipment.Shipment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Statistics {
    private BorderPane root;

    private Text totalProductsText, totalIncomingShipmentsText, totalApprovedShipmentsText, totalCancelledShipmentsText;
    private Text shipmentWithMaxQuantityText, activeProductsText, inActiveProductsText;
    private TextArea cancelRatePerCategoryTextArea;

    public Statistics() {
        root = new BorderPane();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("========== WAREHOUSE REPORT ==========");

        HBox titleBox = new HBox(titleText);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 40, 0));

        totalProductsText = UIHelper.createInfoText("Total Products: ");
        totalIncomingShipmentsText = UIHelper.createInfoText("Incoming Shipments: ");
        totalApprovedShipmentsText = UIHelper.createInfoText("Approved Shipments: ");
        totalCancelledShipmentsText = UIHelper.createInfoText("Cancelled Shipments: ");

        Text maxQuantityText = UIHelper.createInfoText("Shipment With Max Quantity: ");
        shipmentWithMaxQuantityText = UIHelper.createInfoText("");

        Text statusText = UIHelper.createInfoText("Status Summary: ");
        activeProductsText = UIHelper.createInfoText("");
        inActiveProductsText = UIHelper.createInfoText("");

        Text cancelRatePerCategoryText = UIHelper.createInfoText("Cancel Rate Per Category: ");
        cancelRatePerCategoryTextArea = new TextArea();
        cancelRatePerCategoryTextArea.setEditable(false);


        gridPane.add(totalProductsText, 0, 0);
        gridPane.add(totalIncomingShipmentsText, 0, 1);
        gridPane.add(totalApprovedShipmentsText, 0, 2);
        gridPane.add(totalCancelledShipmentsText, 0, 3);

        gridPane.add(maxQuantityText, 0, 4);
        gridPane.add(shipmentWithMaxQuantityText, 0, 5);

        gridPane.add(statusText, 0, 6);
        gridPane.add(activeProductsText, 0, 7);
        gridPane.add(inActiveProductsText, 0, 8);

        gridPane.add(cancelRatePerCategoryText, 0, 9);
        gridPane.add(cancelRatePerCategoryTextArea, 0, 10);


        VBox vBox = new VBox(20, titleBox, gridPane);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(40));
        vBox.setStyle("-fx-background-color: white");

        root.setCenter(vBox);
    }

    public BorderPane getRoot() {
        return root;
    }

    public void updateStatistics() {
        totalProductsText.setText("Total products: " + totalProducts());
        totalIncomingShipmentsText.setText("Total Incoming Shipments: " + totalIncomingShipments());
        totalApprovedShipmentsText.setText("Total Approved Shipments: " + countApprovedShipments());
        totalCancelledShipmentsText.setText("Total Cancelled Shipments: " + countCancelledShipments());
        shipmentWithMaxQuantity();
        activeProductsText.setText("-Active Products: " + numberOfActiveProducts());
        inActiveProductsText.setText("-Inactive Products: " + numberOfInActiveProducts());
        cancelRatePerCategory();
    }

    private int totalProducts() {
        int total = 0;

        NodeDouble categoryNode = MainView.categoriesList.getFirst();
        while (categoryNode != null) {
            LinkedList products = categoryNode.getProducts();
            Node productNode = products.getFirst();

            while (productNode != null) {
                total++;
                productNode = productNode.getNext();
            }

            categoryNode = categoryNode.getNext();
        }

        return total;
    }

    private int numberOfActiveProducts() {
        int total = 0;
        DataStructure.NodeDouble categoryNode = MainView.categoriesList.getFirst();
        while (categoryNode != null) {
            LinkedList products = categoryNode.getProducts();
            Node productNode = products.getFirst();
            while (productNode != null) {
                Product product = (Product) productNode.getElement();
                if (product.getStatus().equalsIgnoreCase("Active")) {
                    total++;
                }
                productNode = productNode.getNext();
            }
            categoryNode = categoryNode.getNext();
        }

        return total;
    }

    private int numberOfInActiveProducts() {
        int total = 0;
        DataStructure.NodeDouble categoryNode = MainView.categoriesList.getFirst();
        while (categoryNode != null) {
            LinkedList products = categoryNode.getProducts();
            Node productNode = products.getFirst();
            while (productNode != null) {
                Product product = (Product) productNode.getElement();
                if (product.getStatus().equalsIgnoreCase("Inactive")) {
                    total++;
                }
                productNode = productNode.getNext();
            }
            categoryNode = categoryNode.getNext();
        }

        return total;
    }

    private String shipmentWithMaxQuantity() {
        int maximumQuantity = 0;
        Shipment maxQuantityShipment = null;
        String text = "";

        NodeDouble categoryNode = MainView.categoriesList.getFirst();

        while (categoryNode != null) {
            Queue shipmentQueue = categoryNode.getShipmentQueue();
            Queue tempQueue = new Queue();

            while (!shipmentQueue.isEmpty()) {
                Object element = shipmentQueue.dequeue();
                if (element instanceof Shipment) {
                    Shipment shipment = (Shipment) element;
                    if (shipment.getQuantity() > maximumQuantity) {
                        maximumQuantity = shipment.getQuantity();
                        maxQuantityShipment = shipment;
                    }
                    tempQueue.enqueue(shipment);
                }
            }

            while (!tempQueue.isEmpty()) {
                shipmentQueue.enqueue(tempQueue.dequeue());
            }
            categoryNode = categoryNode.getNext();
        }
        if (maxQuantityShipment != null) {
            text = "-" + maxQuantityShipment.getShipmentId() + " -> " + maxQuantityShipment.getQuantity() + " Units";
            shipmentWithMaxQuantityText.setText(text);
        }
        return text;
    }

    private int countCancelledShipments() {
        int total = 0;
        NodeDouble categoryNode = MainView.categoriesList.getFirst();
        while (categoryNode != null) {
            total += categoryNode.getCancelledShipmentsList().getCounter();
            categoryNode = categoryNode.getNext();
        }
        return total;
    }

    private int countApprovedShipments() {
        int total = 0;
        NodeDouble categoryNode = MainView.categoriesList.getFirst();
        while (categoryNode != null) {
            total += categoryNode.getInventoryStockList().getCounter();
            categoryNode = categoryNode.getNext();
        }
        return total;
    }

    private int totalIncomingShipments() {
        int total = 0;
        NodeDouble categoryNode = MainView.categoriesList.getFirst();
        while (categoryNode != null) {
            total += categoryNode.getShipmentQueue().getSize();
            categoryNode = categoryNode.getNext();
        }
        return total;
    }

    private void cancelRatePerCategory() {
        StringBuilder cancelRate = new StringBuilder();
        NodeDouble categoryNode = MainView.categoriesList.getFirst();
        while (categoryNode != null) {
            String categoryName = categoryNode.getCategoryName();
            int cancelled = categoryNode.getCancelledShipmentsList().getCounter();
            int approved = categoryNode.getInventoryStockList().getCounter();
            int total = cancelled + approved;
            int percentage;
            if (total == 0) {
                percentage = 0;
            } else {
                percentage = (int) Math.round(cancelled * 100.0 / total);
            }

            String text = "-" + categoryName + ": " + cancelled + "/" + total + "(" + percentage + "%)";
            cancelRate.append(text + "\n");
            categoryNode = categoryNode.getNext();
        }
        cancelRatePerCategoryTextArea.setText(cancelRate.toString().toString());
    }

    public void printStatistics() {
        try(PrintWriter writer = new PrintWriter(new FileWriter("Report.txt", false))) {
            writer.println("========== WAREHOUSE REPORT ==========");

            writer.println();

            writer.println("Total products: " + totalProducts());
            writer.println("Total Incoming Shipments: " + totalIncomingShipments());
            writer.println("Total Approved Shipments: " + countApprovedShipments());
            writer.println("Total Cancelled Shipments: " + countCancelledShipments());

            writer.println();

            writer.println("Shipment with max quantity: ");
            writer.println(shipmentWithMaxQuantity());

            writer.println();

            writer.println("Cancel Rate Per Category: ");
            writer.println(cancelRatePerCategoryTextArea.getText());

            writer.println();

            writer.println("Status Summary: ");
            writer.println("-Active products: " + numberOfActiveProducts());
            writer.println("-Inactive products: " + numberOfInActiveProducts());

        }catch (IOException e){
            e.printStackTrace();
        }

        UIHelper.showAlert(Alert.AlertType.INFORMATION, "Statistics printed successfully");
    }

}
