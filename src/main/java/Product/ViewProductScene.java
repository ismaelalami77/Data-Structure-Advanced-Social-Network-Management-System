package Product;

import DataStructure.Node;
import DataStructure.NodeDouble;
import Project3.MainView;
import Project3.UIHelper;
import Shipment.AddShipmentScene;
import Shipment.Shipment;
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

import java.util.Comparator;

public class ViewProductScene {
    public static TableView productsTableView;
    private BorderPane root;
    private VBox vBox;
    private Button deleteProductButton, editProductButton, previousCategoryButton, nextCategoryButton, addShipmentButton;
    private Text statusText, sortText;
    private RadioButton allRadioButton, activeRadioButton, inactiveRadioButton;
    private RadioButton sortByNameRadioButton, sortByCategoryRadioButton, sortByStatusRadioButton;
    private TextField searchTextField;

    private ObservableList<Product> productsObservableList = FXCollections.observableArrayList();
    private NodeDouble currentCategory = null;
    private EditProductScene editProductScene = new EditProductScene();
    private AddShipmentScene addShipmentScene = new AddShipmentScene();

    public ViewProductScene() {
        root = new BorderPane();

        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(50, 50, 50, 50));
        vBox.setAlignment(Pos.CENTER);

        Text titleText = UIHelper.createTitleText("View Products");

        productsTableView = new TableView();
        TableColumn<Product, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Product, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        productsTableView.getColumns().addAll(idColumn, nameColumn, categoryColumn, statusColumn);
        productsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productsTableView.setItems(productsObservableList);

        ToggleGroup statusToggleGroup = new ToggleGroup();
        statusText = UIHelper.radiButtonText("Status");
        allRadioButton = UIHelper.createRadioButton("All");
        allRadioButton.setToggleGroup(statusToggleGroup);
        activeRadioButton = UIHelper.createRadioButton("Active");
        activeRadioButton.setToggleGroup(statusToggleGroup);
        inactiveRadioButton = UIHelper.createRadioButton("Inactive");
        inactiveRadioButton.setToggleGroup(statusToggleGroup);
        allRadioButton.setSelected(true);

        HBox statusRadioButtonsHBox = new HBox(statusText, allRadioButton, activeRadioButton, inactiveRadioButton);
        statusRadioButtonsHBox.setAlignment(Pos.CENTER);
        statusRadioButtonsHBox.setSpacing(10);

        ToggleGroup sortToggleGroup = new ToggleGroup();
        sortText = UIHelper.radiButtonText("Sort");
        sortByNameRadioButton = UIHelper.createRadioButton("By Name");
        sortByNameRadioButton.setToggleGroup(sortToggleGroup);
        sortByCategoryRadioButton = UIHelper.createRadioButton("By Category");
        sortByCategoryRadioButton.setToggleGroup(sortToggleGroup);
        sortByStatusRadioButton = UIHelper.createRadioButton("By Status");
        sortByStatusRadioButton.setToggleGroup(sortToggleGroup);

        HBox sortRadioButtonsHbox = new HBox(sortText, sortByNameRadioButton, sortByCategoryRadioButton, sortByStatusRadioButton);
        sortRadioButtonsHbox.setAlignment(Pos.CENTER);
        sortRadioButtonsHbox.setSpacing(10);


        editProductButton = UIHelper.createStyledButton("✎ Edit");
        deleteProductButton = UIHelper.createStyledButton("Delete ✖");
        previousCategoryButton = UIHelper.createStyledButton("◄ Previous");
        nextCategoryButton = UIHelper.createStyledButton("Next ►");

        HBox topButtonsHBox = new HBox(editProductButton, deleteProductButton);
        topButtonsHBox.setSpacing(10);
        topButtonsHBox.setAlignment(Pos.CENTER);

        HBox bottomButtonsHBox = new HBox(previousCategoryButton, nextCategoryButton);
        bottomButtonsHBox.setSpacing(10);
        bottomButtonsHBox.setAlignment(Pos.CENTER);

        addShipmentButton = UIHelper.createStyledButton("Add Shipment");

        VBox buttonsVBox = new VBox(statusRadioButtonsHBox, sortRadioButtonsHbox, topButtonsHBox, bottomButtonsHBox, addShipmentButton);
        buttonsVBox.setSpacing(10);
        buttonsVBox.setAlignment(Pos.CENTER);

        searchTextField = UIHelper.createStyledTextField();

        vBox.getChildren().addAll(titleText, productsTableView, searchTextField, buttonsVBox);

        root.setCenter(vBox);

        //actions
        nextCategoryButton.setOnAction(e -> showNextCategory());
        previousCategoryButton.setOnAction(e -> showPreviousCategory());
        allRadioButton.setOnAction(e -> statusFilter());
        activeRadioButton.setOnAction(e -> statusFilter());
        inactiveRadioButton.setOnAction(e -> statusFilter());
        sortByNameRadioButton.setOnAction(e -> sortFilter());
        sortByCategoryRadioButton.setOnAction(e -> sortFilter());
        sortByStatusRadioButton.setOnAction(e -> sortFilter());
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> searchFilter());
        editProductButton.setOnAction(e -> editAction());
        deleteProductButton.setOnAction(e -> deleteAction());
        addShipmentButton.setOnAction(e -> addShipmentAction());
    }

    public BorderPane getRoot() {
        return root;
    }

    public void fillAllProducts() {
        productsObservableList.clear();

        NodeDouble categoryNode = MainView.categoriesList.getFirst();
        while (categoryNode != null) {
            Node productNode = categoryNode.getProducts().getFirst();
            while (productNode != null) {
                Product product = (Product) productNode.getElement();
                productsObservableList.add(product);
                productNode = productNode.getNext();
            }
            categoryNode = categoryNode.getNext();
        }

        currentCategory = null;
        statusFilter();
    }

    private void fillProductsByCategory() {
        productsObservableList.clear();

        if (currentCategory == null) {
            return;
        }
        Node productNode = currentCategory.getProducts().getFirst();
        while (productNode != null) {
            Product product = (Product) productNode.getElement();
            productsObservableList.add(product);
            productNode = productNode.getNext();
        }
        statusFilter();
    }

    private void showNextCategory() {
        if (currentCategory == null) {
            currentCategory = MainView.categoriesList.getFirst();
        } else if (currentCategory.getNext() != null) {
            currentCategory = currentCategory.getNext();
        } else {
            fillAllProducts();
            return;
        }
        fillProductsByCategory();
    }

    private void showPreviousCategory() {
        if (currentCategory == null) {
            currentCategory = MainView.categoriesList.getLast();
        } else if (currentCategory.getPrevious() != null) {
            currentCategory = currentCategory.getPrevious();
        } else {
            fillAllProducts();
            return;
        }
        fillProductsByCategory();
    }


    private void statusFilter() {
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

        for (Product product : productsObservableList) {
            String status = product.getStatus().toLowerCase();

            if (allRadioButton.isSelected()) {
                filteredProducts.add(product);
            } else if (activeRadioButton.isSelected() && status.equals("active")) {
                filteredProducts.add(product);
            } else if (inactiveRadioButton.isSelected() && status.equals("inactive")) {
                filteredProducts.add(product);
            }
        }
        productsTableView.setItems(filteredProducts);
        sortFilter();
    }

    private void sortFilter() {
        ObservableList<Product> currentItems = productsTableView.getItems();

        if (sortByNameRadioButton.isSelected()) {
            currentItems.sort(new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return p1.getName().compareToIgnoreCase(p2.getName());
                }
            });
        } else if (sortByCategoryRadioButton.isSelected()) {
            currentItems.sort(new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return p1.getCategory().compareToIgnoreCase(p2.getCategory());
                }
            });
        } else if (sortByStatusRadioButton.isSelected()) {
            currentItems.sort(new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return p1.getStatus().compareToIgnoreCase(p2.getStatus());
                }
            });
        }


        productsTableView.refresh();
    }

    private void searchFilter() {
        String searchText = searchTextField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            statusFilter();
            return;
        }

        ObservableList<Product> allItems = productsTableView.getItems();
        ObservableList<Product> filteredItems = FXCollections.observableArrayList();

        for (Product product : allItems) {
            if (product.getProductId().toLowerCase().contains(searchText) ||
                    product.getName().toLowerCase().contains(searchText)) {
                filteredItems.add(product);
            }
        }

        productsTableView.setItems(filteredItems);
    }

    private void editAction(){
        editProductScene.getCategories();
        Product product = (Product) productsTableView.getSelectionModel().getSelectedItem();
        if (product == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "No product selected");
            return;
        }
        editProductScene.setProduct(product);
        editProductScene.show();
    }

    private void deleteAction() {
        Product selectedproduct = (Product) productsTableView
                .getSelectionModel()
                .getSelectedItem();
        if (selectedproduct == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "No product selected");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete “" + selectedproduct.getName() + "”?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                NodeDouble categoryNode = MainView.categoriesList.getFirst();
                boolean productRemoved = false;
                while (categoryNode != null && !productRemoved) {
                    productRemoved = categoryNode.getProducts().remove(selectedproduct);
                    categoryNode = categoryNode.getNext();
                }

                if (productRemoved) {
                    productsObservableList.remove(selectedproduct);
                    statusFilter();
                    productsTableView.setItems(FXCollections.observableArrayList(productsTableView.getItems()));
                    productsTableView.refresh();

                    UIHelper.showAlert(Alert.AlertType.INFORMATION, "Product removed successfully");
                } else {
                    UIHelper.showAlert(Alert.AlertType.ERROR, "Unable to remove product");
                }
            }
        });
    }

    private void addShipmentAction() {
        Product selectedProduct = (Product) productsTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "No product selected");
            return;
        }

        addShipmentScene.productIdTextField.setText(selectedProduct.getProductId());
        addShipmentScene.shipmentIdTextField.setText(Shipment.getCurrentShipmentId());
        addShipmentScene.show();

    }
}
