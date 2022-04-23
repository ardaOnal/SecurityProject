import javafx.application.Application;
import org.bouncycastle.util.encoders.Hex;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application
{

    public void start(Stage primaryStage) {
        System.out.println("Hello, Java");
        System.out.println("Version: "+ System.getProperty("java.version"));
        System.out.println("IntelliJ sees org.bouncycastle.util.encoder.Hex");

        Scene scene = new Scene(new Pane(), 300, 100);
        primaryStage.setTitle("Hello javafz");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}