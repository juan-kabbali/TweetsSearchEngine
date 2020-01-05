package main.tweet.search.engine;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.lucene.queryparser.classic.ParseException;

public class MainApp extends Application {

    public static Stage stage;
    
    /**
     * Here, our application starts
     * 
     * @param stage
     * @throws java.lang.Exception
     **/
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
        Scene scene = new Scene(root, Properties.width, Properties.heigth);
        MainApp.stage = stage;
        stage.setTitle(Properties.appName);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException, ParseException {
        launch(args);
    }
}
