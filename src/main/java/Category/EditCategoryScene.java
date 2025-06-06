package Category;

import DataStructure.NodeDouble;
import Project3.MainView;
import Project3.UIHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EditCategoryScene {
    private Stage stage;
    private Scene scene;
    private BorderPane root;

    private TextField categoryIdTextField, categoryNameTextField, categoryDescriptionTextField;
    private Text categoryIdText, categoryNameText, categoryDescriptionText;
    private Button editButton, cancelButton;

    private NodeDouble currentCategory;

    public EditCategoryScene() {
        root = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("Edit Category");

        HBox titleBox = new HBox(titleText);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 40, 0));

        categoryIdText = UIHelper.createInfoText("ID: ");
        categoryIdTextField = UIHelper.createStyledTextField();
        categoryIdTextField.setEditable(false);

        categoryNameText = UIHelper.createInfoText("Name: ");
        categoryNameTextField = UIHelper.createStyledTextField();

        categoryDescriptionText = UIHelper.createInfoText("Description: ");
        categoryDescriptionTextField = UIHelper.createStyledTextField();

        editButton = UIHelper.createStyledButton("Edit");
        cancelButton = UIHelper.createStyledButton("Cancel");

        gridPane.add(categoryIdText, 0, 0);
        gridPane.add(categoryIdTextField, 1, 0);
        gridPane.add(categoryNameText, 0, 1);
        gridPane.add(categoryNameTextField, 1, 1);
        gridPane.add(categoryDescriptionText, 0, 2);
        gridPane.add(categoryDescriptionTextField, 1, 2);
        gridPane.add(editButton, 0, 3);
        gridPane.add(cancelButton, 1, 3);

        VBox vBox = new VBox(20, titleBox, gridPane);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(40));
        vBox.setStyle("-fx-background-color: white");

        root.setCenter(vBox);

        stage = new Stage();
        scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Edit Category");
        stage.setResizable(false);


        //actions
        editButton.setOnAction(e -> editAction());
        cancelButton.setOnAction(e -> stage.close());
    }

    public void setCategory(NodeDouble category) {
        currentCategory = category;

        categoryIdTextField.setText(category.getCategoryID());
        categoryNameTextField.setText(category.getCategoryName());
        categoryDescriptionTextField.setText(category.getCategoryDescription());
    }

    private void editAction() {
        String newName = categoryNameTextField.getText().trim();
        String newDescription = categoryDescriptionTextField.getText().trim();

        if (newName.isEmpty() || newDescription.isEmpty()) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Please fill in all fields!");
            return;
        }

        NodeDouble current = MainView.categoriesList.getFirst();
        while (current != null) {
            if (current != currentCategory && current.getCategoryName().equals(newName)) {
                UIHelper.showAlert(Alert.AlertType.ERROR, "Another category with this name already exists!");
                return;
            }
            current = current.getNext();
        }

        currentCategory.setCategoryName(newName);
        currentCategory.setCategoryDescription(newDescription);

        UIHelper.showAlert(Alert.AlertType.INFORMATION, "Category updated successfully!");
        ViewCategoryScene.categoriesTableView.refresh();
        stage.close();
    }

    public void show() {
        stage.show();
    }

}
