package Shipment;

import DataStructure.NodeDouble;
import DataStructure.Queue;
import Operations.Operation;
import Operations.ShipmentLog;
import Project3.Main;
import Project3.MainView;
import Project3.MenuBarPane;
import Project3.UIHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Date;

public class ViewShipmentsScene {
    public static TableView shipmentsTableView;
    private BorderPane root;
    private VBox vBox;
    private Button approveButton, cancelButton;

    private ObservableList<Shipment> shipmentsObservableList = FXCollections.observableArrayList();

    private Operation lastOperation = null;
    private Operation redoOperation = null;


    public ViewShipmentsScene() {
        root = new BorderPane();

        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(50, 50, 50, 50));
        vBox.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("View Shipments");

        shipmentsTableView = new TableView();
        TableColumn<Shipment, String> shipmentIDCol = new TableColumn<>("Shipment ID");
        shipmentIDCol.setCellValueFactory(new PropertyValueFactory<>("shipmentId"));
        TableColumn<Shipment, String> productIdCol = new TableColumn<>("Product ID");
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        TableColumn<Shipment, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<Shipment, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        shipmentsTableView.getColumns().addAll(shipmentIDCol, productIdCol, quantityCol, dateCol);
        shipmentsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        shipmentsTableView.setItems(shipmentsObservableList);


        approveButton = UIHelper.createStyledButton("Approve");
        cancelButton = UIHelper.createStyledButton("Cancel");

        HBox buttonBox = new HBox(approveButton, cancelButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);


        vBox.getChildren().addAll(titleText, shipmentsTableView, buttonBox);
        root.setCenter(vBox);

        //actions
        approveButton.setOnAction(e -> approveAction());
        cancelButton.setOnAction(e -> cancelAction());
    }

    public BorderPane getRoot() {
        return root;
    }


    public void fillShipments() {
        shipmentsObservableList.clear();

        NodeDouble categoryNode = MainView.categoriesList.getFirst();

        while (categoryNode != null) {
            Queue shipmentQueue = categoryNode.getShipmentQueue();

            if (shipmentQueue != null && !shipmentQueue.isEmpty()) {
                Queue tempQueue = new Queue();

                while (!shipmentQueue.isEmpty()) {
                    Object element = shipmentQueue.dequeue();

                    if (element instanceof Shipment) {
                        Shipment shipment = (Shipment) element;
                        shipmentsObservableList.add(shipment);
                    }
                    tempQueue.enqueue(element);
                }
                while (!tempQueue.isEmpty()) {
                    shipmentQueue.enqueue(tempQueue.dequeue());
                }
            }
            categoryNode = categoryNode.getNext();
        }
        shipmentsObservableList.sort((s1, s2) -> s1.getShipmentId().compareTo(s2.getShipmentId()));
        shipmentsTableView.setItems(shipmentsObservableList);

    }

    private void approveAction() {
        NodeDouble node = MainView.categoriesList.getFirst();
        while (node != null && (node.getShipmentQueue() == null || node.getShipmentQueue().isEmpty())) {
            node = node.getNext();
        }
        if (node == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "No shipments found");
            return;
        }

        Shipment shipment = (Shipment) node.getShipmentQueue().peek();
        String shipmentId = shipment.getShipmentId();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to approve shipment " + shipmentId + "?", ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText(null);
        confirmation.showAndWait();
        if (confirmation.getResult() == ButtonType.YES) {
            node.getShipmentQueue().dequeue();
            node.getInventoryStockList().insertAtHead(shipment, node.getInventoryStockListHead());
            shipmentsObservableList.remove(shipment);
            shipmentsTableView.refresh();
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "Shipment " + shipmentId + " approved");

            lastOperation = new Operation(Operation.Approve, shipment, node);
            redoOperation = null;
            ShipmentLog.log("Add Shipment", shipment, shipment.getQuantity());
        }

    }

    private void cancelAction() {
        NodeDouble node = MainView.categoriesList.getFirst();
        while (node != null && (node.getShipmentQueue() == null || node.getShipmentQueue().isEmpty())) {
            node = node.getNext();
        }
        if (node == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "No shipments found");
            return;
        }

        Shipment shipment = (Shipment) node.getShipmentQueue().peek();
        String shipmentId = shipment.getShipmentId();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to cancel shipment " + shipmentId + "?", ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText(null);
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {
            node.getShipmentQueue().dequeue();
            node.getCancelledShipmentsList().insertAtHead(shipment, node.getCancelledShipmentsListHead());
            shipmentsObservableList.remove(shipment);
            shipmentsTableView.refresh();
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "Shipment " + shipmentId + "cancelled");

            lastOperation = new Operation(Operation.CANCEL, shipment, node);
            redoOperation = null;
            ShipmentLog.log("Cancel Shipment", shipment, shipment.getQuantity());
        }

    }

    public void undoAction() {
        if (lastOperation == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "Nothing to undo");
            return;
        }

        Shipment shipment = lastOperation.getShipment();
        NodeDouble node = lastOperation.getCategory();

        if (lastOperation.getOperation().equals(Operation.Approve)) {
            node.getInventoryStockList().remove(shipment, node.getInventoryStockListHead());
        } else {
            node.getCancelledShipmentsList().remove(shipment, node.getCancelledShipmentsListHead());
        }


        Queue shipmetnQueue = node.getShipmentQueue();
        Queue tempQueue = new Queue();

        while (!shipmetnQueue.isEmpty()) {
            tempQueue.enqueue(shipmetnQueue.dequeue());
        }

        shipmetnQueue.enqueue(shipment);

        while (!tempQueue.isEmpty()) {
            shipmetnQueue.enqueue(tempQueue.dequeue());
        }

        UIHelper.showAlert(Alert.AlertType.INFORMATION, "Shipment " + shipment.getShipmentId() + " undone");

        redoOperation = lastOperation;
        lastOperation = null;
        ShipmentLog.log("Undo Shipment", shipment, shipment.getQuantity());
        fillShipments();
    }

    public void redoAction() {
        if (redoOperation == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "Nothing to redo");
            return;
        }

        Shipment shipment = redoOperation.getShipment();
        NodeDouble node = redoOperation.getCategory();

        Queue shipmentQueue = node.getShipmentQueue();
        Shipment first = (Shipment) shipmentQueue.dequeue();

        if (redoOperation.getOperation().equals(Operation.Approve)) {
            node.getInventoryStockList().insertAtHead(shipment, node.getInventoryStockListHead());
        } else {
            node.getCancelledShipmentsList().insertAtHead(shipment, node.getCancelledShipmentsListHead());
        }

        shipmentsObservableList.remove(first);
        shipmentsTableView.refresh();

        lastOperation = redoOperation;
        redoOperation = null;

        ShipmentLog.log("Redo Shipment", shipment, shipment.getQuantity());
        UIHelper.showAlert(Alert.AlertType.INFORMATION, "Shipment " + shipment.getShipmentId() + " redone");
    }

}
