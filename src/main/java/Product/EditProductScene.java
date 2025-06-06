package Product;

import DataStructure.NodeDouble;
import Project3.MainView;
import Project3.UIHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EditProductScene {
    private Stage stage;
    private Scene scene;
    private BorderPane root;

    private TextField productIdTextField, productNameTextField;
    private ComboBox<String> productCategoryComboBox, productStatusComboBox;
    private Text productIdText, productNameText, categoryText, statusText;
    private Button editButton, cancelButton;

    private Product currentProduct;

    public EditProductScene() {
        root = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("Edit Product");

        HBox titleBox = new HBox(titleText);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 40, 0));

        productIdText = UIHelper.createInfoText("ID: ");
        productIdTextField = UIHelper.createStyledTextField();
        productIdTextField.setEditable(false);

        productNameText = UIHelper.createInfoText("Name: ");
        productNameTextField = UIHelper.createStyledTextField();

        categoryText = UIHelper.createInfoText("Category: ");
        productCategoryComboBox = UIHelper.createComboBox();

        statusText = UIHelper.createInfoText("Status: ");
        productStatusComboBox = UIHelper.createComboBox();
        productStatusComboBox.getItems().addAll("Active", "Inactive");

        editButton = UIHelper.createStyledButton("Edit");
        cancelButton = UIHelper.createStyledButton("Cancel");

        gridPane.add(productIdText, 0, 0);
        gridPane.add(productIdTextField, 1, 0);
        gridPane.add(productNameText, 0, 1);
        gridPane.add(productNameTextField, 1, 1);
        gridPane.add(categoryText, 0, 2);
        gridPane.add(productCategoryComboBox, 1, 2);
        gridPane.add(statusText, 0, 3);
        gridPane.add(productStatusComboBox, 1, 3);
        gridPane.add(editButton, 0, 4);
        gridPane.add(cancelButton, 1, 4);

        VBox vBox = new VBox(20, titleBox, gridPane);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(40));
        vBox.setStyle("-fx-background-color: white");

        root.setCenter(vBox);

        stage = new Stage();
        scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Edit Product");
        stage.setResizable(false);

        //actions
        cancelButton.setOnAction(e -> stage.close());
        editButton.setOnAction(e -> editAction());
    }

    public void setProduct(Product product) {
        currentProduct = product;

        productIdTextField.setText(product.getProductId());
        productNameTextField.setText(product.getName());
        productCategoryComboBox.setValue(product.getCategory());
        productStatusComboBox.setValue(product.getStatus());
    }

    public void getCategories(){
        productCategoryComboBox.getItems().clear();

        NodeDouble category = MainView.categoriesList.getFirst();
        while(category != null){
            productCategoryComboBox.getItems().add(category.getCategoryName());
            category = category.getNext();
        }
    }

    private void editAction(){
        String newName = productNameTextField.getText();
        String newCategory = productCategoryComboBox.getValue();
        String newStatus = productStatusComboBox.getValue();

        if (newName.isEmpty() || newCategory == null || newStatus == null) {
            UIHelper.showAlert(Alert.AlertType.WARNING, "Please fill in all fields!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION , "Are you sure you want to edit  " + newName + " ?", ButtonType.YES, ButtonType.NO);
        confirmAlert.setHeaderText(null);
        confirmAlert.showAndWait();

        if (confirmAlert.getResult() == ButtonType.YES) {
            if(!currentProduct.getCategory().equals(newCategory)){
                NodeDouble oldCategory = MainView.categoriesList.getFirst();
                while(oldCategory != null){
                    if(oldCategory.getCategoryName().equals(currentProduct.getCategory())){
                        oldCategory.getProducts().remove(currentProduct);
                        break;
                    }
                    oldCategory = oldCategory.getNext();
                }
                NodeDouble newCategoryNode = MainView.categoriesList.getFirst();
                while(newCategoryNode != null){
                    if(newCategoryNode.getCategoryName().equals(newCategory)){
                        newCategoryNode.addProduct(currentProduct);
                        break;
                    }
                    newCategoryNode = newCategoryNode.getNext();
                }
                currentProduct.setCategory(newCategory);
            }
            currentProduct.setName(newName);
            currentProduct.setStatus(newStatus);
            ViewProductScene.productsTableView.refresh();
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "Product Edited Successfully!");
            stage.close();

        }

    }

    public void show() {
        stage.show();
    }
}
