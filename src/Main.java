import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.processors.StyleProcessor;

public class Main extends Application {

    public static void main(String[] args) {
        StyleProcessor.writeScreenVars();
        StyleProcessor.compile();
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/Game.fxml"));
    
        Scene scene = new Scene(root);
    
        stage.setTitle("Inscryption Lite");
        stage.setScene(scene);
        stage.show();
    }
}
