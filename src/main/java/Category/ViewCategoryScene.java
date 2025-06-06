package Category;

import DataStructure.Node;
import DataStructure.NodeDouble;
import Product.Product;
import Project3.MainView;
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

public class ViewCategoryScene {
    public static TableView categoriesTableView;
    private BorderPane root;
    private VBox vBox;
    private Button deleteCategoryButton, editCategoryButton;
    private TextField searchTextField;

    private ObservableList<NodeDouble> categoriesObservableList = FXCollections.observableArrayList();

    private EditCategoryScene editCategoryScene = new EditCategoryScene();
    private DeleteCategoryScene deleteCategoryScene = new DeleteCategoryScene();


    public ViewCategoryScene() {
        root = new BorderPane();

        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(50, 50, 50, 50));
        vBox.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("View Categories");

        categoriesTableView = new TableView();
        TableColumn<NodeDouble, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("categoryID"));
        TableColumn<NodeDouble, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        TableColumn<NodeDouble, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("categoryDescription"));

        categoriesTableView.getColumns().addAll(idCol, nameCol, descriptionCol);
        categoriesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        categoriesTableView.setItems(categoriesObservableList);

        editCategoryButton = UIHelper.createStyledButton("✎ Edit");
        deleteCategoryButton = UIHelper.createStyledButton("Delete ✖");
        searchTextField = UIHelper.createStyledTextField();

        HBox buttonBox = new HBox(editCategoryButton, deleteCategoryButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(titleText, categoriesTableView, searchTextField, buttonBox);

        root.setCenter(vBox);

        //actions
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> searchAction());
        editCategoryButton.setOnAction(e -> editAction());
        deleteCategoryButton.setOnAction(e -> deleteCategoryAction());
    }

    public BorderPane getRoot() {
        return root;
    }

    public void fillTable() {
        categoriesObservableList.clear();

        NodeDouble categoryNode = MainView.categoriesList.getFirst();
        while (categoryNode != null) {
            categoriesObservableList.add(categoryNode);
            categoryNode = categoryNode.getNext();
        }
    }

    private void searchAction() {
        String searchTerm = searchTextField.getText().trim().toLowerCase();

        if (searchTerm.isEmpty()) {
            fillTable();
            return;
        }

        ObservableList<NodeDouble> filteredList = FXCollections.observableArrayList();
        NodeDouble categoryNode = MainView.categoriesList.getFirst();

        while (categoryNode != null) {
            if (categoryNode.getCategoryName().toLowerCase().contains(searchTerm)) {
                filteredList.add(categoryNode);
            }
            categoryNode = categoryNode.getNext();
        }

        categoriesObservableList.setAll(filteredList);

    }

    private void editAction() {
        NodeDouble categoryNode = (NodeDouble) categoriesTableView.getSelectionModel().getSelectedItem();
        if (categoryNode == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "No category selected");
            return;
        }

        editCategoryScene.setCategory(categoryNode);
        editCategoryScene.show();
    }

    private void deleteCategoryAction() {
        NodeDouble categoryNode = (NodeDouble) categoriesTableView.getSelectionModel().getSelectedItem();
        if (categoryNode == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "No category selected");
            return;
        }

        deleteCategoryScene.getCategories();
        deleteCategoryScene.categoryIdTextField.setText(categoryNode.getCategoryID());
        deleteCategoryScene.categoryNameTextField.setText(categoryNode.getCategoryName());
        deleteCategoryScene.categoryDescriptionTextField.setText(categoryNode.getCategoryDescription());

        deleteCategoryScene.show();
    }


}
