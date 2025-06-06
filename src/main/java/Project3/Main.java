package Project3;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        System.setProperty("javafx.accessibility.enabled", "false");
        launch(args);
    }

    public void start(Stage primaryStage) {
        MainView mainView = new MainView();
        mainView.showStage();
    }
}