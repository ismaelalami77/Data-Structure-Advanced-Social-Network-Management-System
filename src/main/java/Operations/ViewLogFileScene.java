package Operations;

import Project3.UIHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ViewLogFileScene {
    private Stage stage;
    private Scene scene;
    private BorderPane root;


    private TextArea textArea;

    public ViewLogFileScene() {
        root = new BorderPane();

        Text titleText = UIHelper.createTitleText("View Log File");
        HBox titleBox = new HBox(titleText);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 40, 0));

        textArea = new TextArea();
        textArea.setEditable(false);

        VBox vBox = new VBox(20, titleBox, textArea);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(40));
        vBox.setStyle("-fx-background-color: white");

        root.setCenter(vBox);

        stage = new Stage();
        scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.setTitle("View Log File");
        stage.setResizable(false);
    }

    public void show() {
        stage.show();
    }

    public void fillTextArea() {
        textArea.clear();
        try (Scanner scanner = new Scanner(new File("log.txt"))) {
            while (scanner.hasNextLine()) {
                textArea.appendText(scanner.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
