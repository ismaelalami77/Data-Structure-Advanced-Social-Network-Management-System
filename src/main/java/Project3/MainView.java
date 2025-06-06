package Project3;

import Category.AddCategoryScene;
import Category.ViewCategoryScene;
import DataStructure.Queue;
import Operations.ShipmentLog;
import Operations.ViewLogFileScene;
import Shipment.ViewShipmentsScene;
import DataStructure.DoubleLinkedList;
import DataStructure.Node;
import DataStructure.NodeDouble;
import Product.AddProductScene;
import Product.Product;
import Product.ViewProductScene;
import Shipment.Shipment;
import Statistics.Statistics;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class MainView {
    public static DoubleLinkedList categoriesList = new DoubleLinkedList();

    private Stage stage;
    private Scene scene;
    private BorderPane root;

    private MenuBarPane menuBarPane = new MenuBarPane();
    private AddCategoryScene addCategoryScene = new AddCategoryScene();
    private ViewCategoryScene viewCategoryScene = new ViewCategoryScene();
    private AddProductScene addProductScene = new AddProductScene();
    private ViewProductScene viewProductScene = new ViewProductScene();
    private ViewShipmentsScene viewShipmentsScene = new ViewShipmentsScene();
    private Statistics statistics = new Statistics();
    private ViewLogFileScene viewLogFileScene = new ViewLogFileScene();


    public MainView() {
        root = new BorderPane();
        readCategories();
        fillMenu();

        root.setTop(menuBarPane.getMenuBar());

        scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/Styles.css").toExternalForm());
        stage = new Stage();
        stage.setScene(scene);
        stage.setMaximized(true);
    }

    public void showStage() {
        stage.show();
    }

    private void readCategories() {
        try (Scanner scanner = new Scanner(new File("categories.txt"))) {
            while (scanner.hasNextLine()) {
                String category = scanner.nextLine();
                if (category.isEmpty()) {
                    continue;
                }
                String[] parts = category.split(",");
                if (parts.length != 2) {
                    continue;
                }
                String categoryName = parts[0].trim();
                String categoryDescription = parts[1].trim();

                NodeDouble newNode = new NodeDouble(categoryName, categoryDescription);
                categoriesList.addLast(newNode);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readFileChooser(String fileName) {
        FileChooser fileChooser = new FileChooser();
        File defaultDir = new File(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Games" + File.separator + "COMP242 Project 3");
        if (defaultDir.exists()) {
            fileChooser.setInitialDirectory(defaultDir);
        }
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            switch (fileName) {
                case "Products":
                    readProducts(selectedFile);
                    break;
                case "Shipments":
                    readShipments(selectedFile);
                    break;
            }
        }
    }

    private void readProducts(File file) {
        int counter = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 4) continue;

                String productName = parts[1].trim();
                String categoryName = parts[2];
                String status = parts[3].trim();

                Product product = new Product(productName, categoryName, status);

                NodeDouble currentCategory = categoriesList.getFirst();
                boolean categoryFound = false;
                while (currentCategory != null) {
                    if (currentCategory.getCategoryName().equalsIgnoreCase(categoryName)) {
                        currentCategory.addProduct(product);
                        categoryFound = true;
                        break;
                    }
                    currentCategory = currentCategory.getNext();
                }

                if (!categoryFound) {
                    counter++;
                }
            }

            if (counter > 0) {
                UIHelper.showAlert(Alert.AlertType.INFORMATION, counter + " products with no category found.");
            } else {
                UIHelper.showAlert(Alert.AlertType.INFORMATION, "Products loaded successfully.");
            }

        } catch (FileNotFoundException e) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "File not found.");
        }
    }

    private void readShipments(File file) {
        int counter = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 4) continue;
                String productID = parts[1].trim();
                int quantity;
                try {
                    quantity = Integer.parseInt(parts[2].trim());
                } catch (NumberFormatException e) {
                    continue;
                }
                if (quantity <= 0) continue;
                String dateString = parts[3].trim();
                String[] dateParts = dateString.split("-");
                if (dateParts.length != 3) continue;
                int year = Integer.parseInt(dateParts[0].trim()) - 1900;
                int month = Integer.parseInt(dateParts[1].trim()) - 1;
                int day = Integer.parseInt(dateParts[2].trim());

                Date date = new Date(year, month, day);
                Shipment shipment = new Shipment(productID, quantity, date);

                NodeDouble categoryNode = categoriesList.getFirst();
                boolean productFound = false;
                while (categoryNode != null) {
                    Node productNode = categoryNode.getProducts().getFirst();
                    while (productNode != null) {
                        Product product = (Product) productNode.getElement();
                        if (product.getProductId().equalsIgnoreCase(productID)) {
                            categoryNode.getShipmentQueue().enqueue(shipment);
                            productFound = true;
                            break;
                        }
                        productNode = productNode.getNext();
                    }
                    if (productFound) break;
                    categoryNode = categoryNode.getNext();
                }
                if (!productFound) {
                    counter++;
                }
            }
            if (counter > 0) {
                UIHelper.showAlert(Alert.AlertType.INFORMATION, counter + " shipments with no product found.");
            } else {
                UIHelper.showAlert(Alert.AlertType.INFORMATION, "shipments loaded successfully.");
            }
        } catch (FileNotFoundException e) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "File not found.");
        }

    }

    private void fillMenu() {
        menuBarPane.addCategoryItem.setOnAction(e -> {
            root.setCenter(addCategoryScene.getRoot());
            addCategoryScene.categoryIdTextField.setText(NodeDouble.getCurrentCategoryID());
        });
        menuBarPane.viewCategoryItem.setOnAction(e -> {
            viewCategoryScene.fillTable();
            root.setCenter(viewCategoryScene.getRoot());
        });

        menuBarPane.addProductItem.setOnAction(e -> {
            addProductScene.getCategories();
            root.setCenter(addProductScene.getRoot());
            addProductScene.productIdTextField.setText(Product.getCurrentProductId());
        });
        menuBarPane.viewProductsItem.setOnAction(e -> {
            viewProductScene.fillAllProducts();
            root.setCenter(viewProductScene.getRoot());
        });

        menuBarPane.viewShipments.setOnAction(e -> {
            viewShipmentsScene.fillShipments();
            root.setCenter(viewShipmentsScene.getRoot());
        });
        menuBarPane.statisticsItem.setOnAction(e -> {
            statistics.updateStatistics();
            root.setCenter(statistics.getRoot());
        });


        menuBarPane.importProductDataItem.setOnAction(e -> readFileChooser("Products"));
        menuBarPane.importShipmentDataItem.setOnAction(e -> readFileChooser("Shipments"));
        menuBarPane.undoLastActionItem.setOnAction(e -> viewShipmentsScene.undoAction());
        menuBarPane.redoLastActionItem.setOnAction(e -> viewShipmentsScene.redoAction());
        menuBarPane.viewLogFileItem.setOnAction(e -> {
            viewLogFileScene.fillTextArea();
            viewLogFileScene.show();
        });
        menuBarPane.exportOperationLogsItem.setOnAction(e -> {
            try (FileWriter fileWriter = new FileWriter("log.txt", false)) {
                fileWriter.write(ShipmentLog.getStringBuilder().toString());
                UIHelper.showAlert(Alert.AlertType.INFORMATION, "logs exported.  ");
            } catch (IOException eo){
                eo.printStackTrace();
            }
        });
        menuBarPane.printStatisticsItem.setOnAction(e -> statistics.printStatistics());
    }
}
