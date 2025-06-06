package Category;

import DataStructure.NodeDouble;
import Project3.MainView;
import Project3.UIHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AddCategoryScene {
    private BorderPane root;
    private Text categoryIdText, categoryNameText, categoryDescriptionText;
    private TextField categoryNameTextField, categoryDescriptionTextField;
    public TextField categoryIdTextField;
    private Button addCategoryButton;


    public AddCategoryScene() {
        root = new BorderPane();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("Add Category");

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

        addCategoryButton = UIHelper.createStyledButton("Add");

        gridPane.add(categoryIdText, 0, 0);
        gridPane.add(categoryIdTextField, 1, 0);
        gridPane.add(categoryNameText, 0, 1);
        gridPane.add(categoryNameTextField, 1, 1);
        gridPane.add(categoryDescriptionText, 0, 2);
        gridPane.add(categoryDescriptionTextField, 1, 2);
        gridPane.add(addCategoryButton, 1, 3);

        VBox vBox = new VBox(20, titleBox, gridPane);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(40));
        vBox.setStyle("-fx-background-color: white");

        root.setCenter(vBox);

        addCategoryButton.setOnAction(e -> addAction());
    }

    public BorderPane getRoot() {
        return root;
    }

    private void addAction() {
        String categoryName = categoryNameTextField.getText().trim();
        String categoryDescription = categoryDescriptionTextField.getText().trim();

        if (categoryName.isEmpty() || categoryDescription.isEmpty()) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Please fill in all fields!");
            return;
        }

        NodeDouble current = MainView.categoriesList.getFirst();
        while (current != null) {
            if (current.getCategoryName().equalsIgnoreCase(categoryName)) {
                UIHelper.showAlert(Alert.AlertType.ERROR, "Category already exists");
                return;
            }
            current = current.getNext();
        }

        NodeDouble newCategoryNode = new NodeDouble(categoryName, categoryDescription);

        MainView.categoriesList.addLast(newCategoryNode);
        UIHelper.showAlert(Alert.AlertType.INFORMATION, "Category added Successfully");

        categoryNameTextField.clear();
        categoryDescriptionTextField.clear();
        categoryIdTextField.setText(NodeDouble.getCurrentCategoryID());
    }
}
