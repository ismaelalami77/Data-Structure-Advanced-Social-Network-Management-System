package Product;

import DataStructure.NodeDouble;
import Project3.MainView;
import Project3.UIHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AddProductScene {
    private BorderPane root;

    private Text productIdText, productNameText, productCategoryText, productStatusText;
    public TextField productIdTextField;
    private TextField productNameTextField;
    private ComboBox<String> productCategoryComboBox, productStatusComboBox;


    private Button addProductButton;


    public AddProductScene(){
        root = new BorderPane();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("Add Product");

        HBox titleBox = new HBox(titleText);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 40, 0));

        productIdText = UIHelper.createInfoText("ID: ");
        productIdTextField = UIHelper.createStyledTextField();
        productIdTextField.setEditable(false);

        productNameText = UIHelper.createInfoText("Name: ");
        productNameTextField = UIHelper.createStyledTextField();

        productCategoryText = UIHelper.createInfoText("Category: ");
        productCategoryComboBox = UIHelper.createComboBox();


        productStatusText = UIHelper.createInfoText("Status: ");
        productStatusComboBox = UIHelper.createComboBox();
        productStatusComboBox.getItems().addAll("Active", "Inactive");

        addProductButton = UIHelper.createStyledButton("Add");


        gridPane.add(productIdText, 0, 0);
        gridPane.add(productIdTextField, 1, 0);

        gridPane.add(productNameText, 0, 1);
        gridPane.add(productNameTextField, 1, 1);

        gridPane.add(productCategoryText, 0, 2);
        gridPane.add(productCategoryComboBox, 1, 2);

        gridPane.add(productStatusText, 0, 3);
        gridPane.add(productStatusComboBox, 1, 3);

        gridPane.add(addProductButton, 1, 4);

        VBox vBox = new VBox(20, titleBox, gridPane);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(40));
        vBox.setStyle("-fx-background-color: white");

        root.setCenter(vBox);
        addProductButton.setOnAction(e -> addAction());
    }

    public BorderPane getRoot(){
        return root;
    }

    public void getCategories(){
        productCategoryComboBox.getItems().clear();

        NodeDouble category = MainView.categoriesList.getFirst();
        while(category != null){
            productCategoryComboBox.getItems().add(category.getCategoryName());
            category = category.getNext();
        }
    }

    private void addAction(){
        String productName = productNameTextField.getText().trim();
        String category = productCategoryComboBox.getValue();
        String status = productStatusComboBox.getValue();

        if(productName.isEmpty() || category == null || status == null){
            UIHelper.showAlert(Alert.AlertType.ERROR, "Please fill in all fields!");
            return;
        }

        Product product = new Product(productName, category, status);

        NodeDouble categoryNode = MainView.categoriesList.getFirst();
        while(categoryNode != null){
            if(categoryNode.getCategoryName().equalsIgnoreCase(category)){
                categoryNode.addProduct(product);
                break;
            }
            categoryNode = categoryNode.getNext();
        }

        UIHelper.showAlert(Alert.AlertType.INFORMATION, "Product Added Successfully!");

        productNameTextField.clear();
        productCategoryComboBox.getSelectionModel().clearSelection();
        productStatusComboBox.getSelectionModel().clearSelection();

        productIdTextField.setText(Product.getCurrentProductId());
    }
}
