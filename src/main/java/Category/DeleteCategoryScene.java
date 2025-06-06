package Category;

import DataStructure.Node;
import DataStructure.NodeDouble;
import Product.Product;
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

public class DeleteCategoryScene {
    public TextField categoryIdTextField, categoryNameTextField, categoryDescriptionTextField;
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private Text categoryIdText, categoryNameText, categoryDescriptionText, otherCategoryNameText, otherCategoriesText;
    private Button cancelButton, forceDeleteButton, assignManuallyButton, assignToOtherCategoryButton;
    private ComboBox<String> otherCategoryComboBox;

    public DeleteCategoryScene() {
        root = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("Delete Category");

        HBox titleBox = new HBox(titleText);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 40, 0));

        categoryIdText = UIHelper.createInfoText("ID: ");
        categoryIdTextField = UIHelper.createStyledTextField();
        categoryIdTextField.setEditable(false);

        categoryNameText = UIHelper.createInfoText("Name: ");
        categoryNameTextField = UIHelper.createStyledTextField();
        categoryNameTextField.setEditable(false);

        categoryDescriptionText = UIHelper.createInfoText("Description: ");
        categoryDescriptionTextField = UIHelper.createStyledTextField();
        categoryDescriptionTextField.setEditable(false);

        cancelButton = UIHelper.deleteStyledButton("Cancel");
        forceDeleteButton = UIHelper.deleteStyledButton("Force Delete");
        assignManuallyButton = UIHelper.deleteStyledButton("Manual");
        assignToOtherCategoryButton = UIHelper.deleteStyledButton("Other category");

        otherCategoriesText = UIHelper.createInfoText("Categories: ");
        otherCategoryComboBox = UIHelper.createComboBox();

        gridPane.add(categoryIdText, 0, 0);
        gridPane.add(categoryIdTextField, 1, 0);
        gridPane.add(categoryNameText, 0, 1);
        gridPane.add(categoryNameTextField, 1, 1);
        gridPane.add(categoryDescriptionText, 0, 2);
        gridPane.add(categoryDescriptionTextField, 1, 2);
        gridPane.add(otherCategoriesText, 0, 3);
        gridPane.add(otherCategoryComboBox, 1, 3);
        gridPane.add(cancelButton, 0, 4);
        gridPane.add(forceDeleteButton, 1, 4);
        gridPane.add(assignManuallyButton, 0, 5);
        gridPane.add(assignToOtherCategoryButton, 1, 5);


        VBox vBox = new VBox(20, titleBox, gridPane);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(40));
        vBox.setStyle("-fx-background-color: white");

        root.setCenter(vBox);

        stage = new Stage();
        scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Delete Category");
        stage.setResizable(false);

        //actions
        cancelButton.setOnAction(e -> stage.close());
        forceDeleteButton.setOnAction(e -> forceDeleteAction());
        assignManuallyButton.setOnAction(e -> assignManuallyAction());
        assignToOtherCategoryButton.setOnAction(e -> assignToOtherCategoryAction());
    }

    public void show() {
        stage.show();
    }

    private void forceDeleteAction() {
        String categoryId = categoryIdTextField.getText();
        NodeDouble categoryNode = MainView.categoriesList.getFirst();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this category?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText(null);
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {
            while (categoryNode != null) {
                if (categoryNode.getCategoryID().equals(categoryId)) {
                    MainView.categoriesList.remove(categoryNode);

                    ViewCategoryScene.categoriesTableView.getItems().remove(categoryNode);
                    UIHelper.showAlert(Alert.AlertType.INFORMATION, "Category removed Successfully");
                    stage.close();
                    return;
                }
                categoryNode = categoryNode.getNext();
            }
        }
    }

    private void assignToOtherCategoryAction() {
        String currentCategoryId = categoryIdTextField.getText();
        String newCategoryName = otherCategoryComboBox.getValue();

        if (newCategoryName == null) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Select a new category");
            return;
        }

        if (newCategoryName.equalsIgnoreCase(categoryNameTextField.getText())) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Can't assign to same category");
            return;
        }

        NodeDouble currentCategoryNode = MainView.categoriesList.getFirst();
        NodeDouble newCategoryNode = MainView.categoriesList.getFirst();
        while (currentCategoryNode != null) {
            if (currentCategoryNode.getCategoryID().equals(currentCategoryId)) {
                break;
            }
            currentCategoryNode = currentCategoryNode.getNext();
        }

        while (newCategoryNode != null) {
            if (newCategoryNode.getCategoryName().equals(newCategoryName)) {
                break;
            }
            newCategoryNode = newCategoryNode.getNext();
        }

        if (currentCategoryNode.getProducts().getFirst() == null) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "No products to resign");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure to assign all products to: " + newCategoryName,
                ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText(null);
        confirmation.showAndWait();
        if (confirmation.getResult() != ButtonType.YES) {
            return;
        }

        Node productNode = currentCategoryNode.getProducts().getFirst();
        while (productNode != null) {
            Product product = (Product) productNode.getElement();
            product.setCategory(newCategoryName);
            newCategoryNode.addProduct(product);
            productNode = productNode.getNext();
        }

        MainView.categoriesList.remove(currentCategoryNode);
        ViewCategoryScene.categoriesTableView.getItems().remove(currentCategoryNode);

        UIHelper.showAlert(Alert.AlertType.INFORMATION, "Category removed successfully");
        stage.close();
    }

    private void assignManuallyAction() {
        String categoryId = categoryIdTextField.getText();

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this category?",
                ButtonType.YES, ButtonType.NO);
        confirmAlert.setHeaderText(null);
        confirmAlert.showAndWait();

        if (confirmAlert.getResult() != ButtonType.YES) {
            return;
        }

        NodeDouble categoryNode = MainView.categoriesList.getFirst();

        while (categoryNode != null) {
            if (categoryNode.getCategoryID().equals(categoryId)) {
                if (categoryNode.getProducts().getFirst() == null) {
                    MainView.categoriesList.remove(categoryNode);

                    ViewCategoryScene.categoriesTableView.getItems().remove(categoryNode);

                    UIHelper.showAlert(Alert.AlertType.INFORMATION, "Category deleted successfully.");
                    stage.close();
                } else {
                    UIHelper.showAlert(Alert.AlertType.INFORMATION,
                            "Cannot delete category.");
                }
                return;
            }
            categoryNode = categoryNode.getNext();
        }
    }

    public void getCategories() {
        otherCategoryComboBox.getItems().clear();

        NodeDouble category = MainView.categoriesList.getFirst();
        while (category != null) {
            otherCategoryComboBox.getItems().add(category.getCategoryName());

            category = category.getNext();
        }
    }
}
