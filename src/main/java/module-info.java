module com.example.comp242project3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.comp242project3 to javafx.fxml;
    opens DataStructure to javafx.base;
    opens Shipment to javafx.base;

    exports Shipment;
    exports Project3;
    exports DataStructure;
    exports Product;
}