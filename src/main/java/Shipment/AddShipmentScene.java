package Shipment;

import DataStructure.Node;
import DataStructure.NodeDouble;
import Product.Product;
import Project3.MainView;
import Project3.UIHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Date;

public class AddShipmentScene {
    public TextField shipmentIdTextField, productIdTextField;
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private TextField quantityTextField;
    private DatePicker shipmentDatePicker;

    private Text shipmentIdText, productIdText, quantityText, shipmentDateText;
    private Button addShipmentButton, cancelButton;

    public AddShipmentScene() {
        root = new BorderPane();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("Add Shipment");

        HBox titleBox = new HBox(titleText);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 40, 0));

        shipmentIdText = UIHelper.createInfoText("Shipment ID: ");
        shipmentIdTextField = UIHelper.createStyledTextField();
        shipmentIdTextField.setEditable(false);

        productIdText = UIHelper.createInfoText("Product ID: ");
        productIdTextField = UIHelper.createStyledTextField();
        productIdTextField.setEditable(false);

        quantityText = UIHelper.createInfoText("Quantity: ");
        quantityTextField = UIHelper.createStyledTextField();

        shipmentDateText = UIHelper.createInfoText("Date: ");
        shipmentDatePicker = UIHelper.createStyledDatePicker();

        addShipmentButton = UIHelper.createStyledButton("Add");
        cancelButton = UIHelper.createStyledButton("Cancel");

        gridPane.add(shipmentIdText, 0, 0);
        gridPane.add(shipmentIdTextField, 1, 0);
        gridPane.add(productIdText, 0, 1);
        gridPane.add(productIdTextField, 1, 1);
        gridPane.add(quantityText, 0, 2);
        gridPane.add(quantityTextField, 1, 2);
        gridPane.add(shipmentDateText, 0, 3);
        gridPane.add(shipmentDatePicker, 1, 3);
        gridPane.add(addShipmentButton, 0, 4);
        gridPane.add(cancelButton, 1, 4);


        VBox vBox = new VBox(20, titleBox, gridPane);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(40));
        vBox.setStyle("-fx-background-color: white");

        root.setCenter(vBox);

        stage = new Stage();
        scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Add Shipment");
        stage.setResizable(false);

        cancelButton.setOnAction(e -> stage.close());
        addShipmentButton.setOnAction(e -> addAction());
    }

    public void show() {
        stage.show();
    }

    private void addAction() {
        String productId = productIdTextField.getText().trim();
        String quantityString = quantityTextField.getText().trim();

        if (productId.isEmpty() || quantityString.isEmpty() || shipmentDatePicker.getValue() == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "Please fill all fields.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "quantity must be an integer.");
            return;
        }
        if (quantity <= 0) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "quantity must be grater than 0.");
            return;
        }

        String dateString = shipmentDatePicker.getValue().toString();
        String[] dateParts = dateString.split("-");

        int year = Integer.parseInt(dateParts[0]) - 1900;
        int month = Integer.parseInt(dateParts[1]) - 1;
        int day = Integer.parseInt(dateParts[2]);

        Date shipmentDate = new Date(year, month, day);

        Shipment shipment = new Shipment(productId, quantity, shipmentDate);

        NodeDouble categoryNode = findCategoryByProductID(productId);
        if (categoryNode != null) {
            categoryNode.getShipmentQueue().enqueue(shipment);
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "Shipment added successfully.");
            stage.close();
        }
    }

    private NodeDouble findCategoryByProductID(String productID) {
        NodeDouble categoryNode = MainView.categoriesList.getFirst();
        while (categoryNode != null) {
            Node productNode = categoryNode.getProducts().getFirst();
            while (productNode != null) {
                Product product = (Product) productNode.getElement();
                if (product.getProductId().equals(productID)) {
                    return categoryNode;
                }
                productNode = productNode.getNext();
            }
            categoryNode = categoryNode.getNext();
        }
        return null;
    }
}
