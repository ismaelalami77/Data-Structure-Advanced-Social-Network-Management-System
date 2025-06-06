package Project3;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuBarPane {
    MenuBar menuBar;

    Menu categoryMenu, productMenu, shipmentMenu, fileMenu, statisticsMenu;

    MenuItem addCategoryItem, viewCategoryItem;
    MenuItem addProductItem, viewProductsItem;
    MenuItem viewShipments, undoLastActionItem, redoLastActionItem;
    MenuItem importProductDataItem, importShipmentDataItem, exportOperationLogsItem, viewLogFileItem;
    MenuItem statisticsItem, printStatisticsItem;

    public MenuBarPane() {
        menuBar = new MenuBar();

        //category menu
        categoryMenu = new Menu("Category");
        addCategoryItem = new MenuItem("Add Category");
        viewCategoryItem = new MenuItem("View Categories");
        categoryMenu.getItems().addAll(addCategoryItem, viewCategoryItem);

        //product menu
        productMenu = new Menu("Product");
        addProductItem = new MenuItem("Add Product");
        viewProductsItem = new MenuItem("View Products");
        productMenu.getItems().addAll(addProductItem, viewProductsItem);

        //shipment menu
        shipmentMenu = new Menu("Shipment");
        viewShipments = new MenuItem("View Shipments");
        undoLastActionItem = new MenuItem("Undo Last Action");
        redoLastActionItem = new MenuItem("Redo Last Action");
        shipmentMenu.getItems().addAll(viewShipments, undoLastActionItem, redoLastActionItem);

        //file menu
        fileMenu = new Menu("File");
        importProductDataItem = new MenuItem("Import Products Data");
        importShipmentDataItem = new MenuItem("Import Shipments Data");
        exportOperationLogsItem = new MenuItem("Export Operation Logs");
        viewLogFileItem = new MenuItem("View Log File");
        fileMenu.getItems().addAll(importProductDataItem, importShipmentDataItem, exportOperationLogsItem, viewLogFileItem);

        //statisticsItem menu
        statisticsMenu = new Menu("Statistics");
        statisticsItem = new MenuItem("Statistics");
        printStatisticsItem = new MenuItem("Print Statistics");
        statisticsMenu.getItems().addAll(statisticsItem, printStatisticsItem);

        menuBar.getMenus().addAll(categoryMenu, productMenu, shipmentMenu, fileMenu, statisticsMenu);

    }

    public MenuBar getMenuBar() {
        return menuBar;
    }
}
