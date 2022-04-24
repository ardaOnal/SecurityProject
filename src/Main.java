import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application
{

    public void start(Stage primaryStage) {
        System.out.println("Hello, Java");
        System.out.println("Version: "+ System.getProperty("java.version"));
        System.out.println("IntelliJ sees org.bouncycastle.util.encoder.Hex");

        /*
        primaryStage.setTitle("Password Manager");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("Sign in button pressed");
            }
        });

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.setScene(scene);
        primaryStage.show();*/

        Protector protector = new Protector( new BouncyCastleProvider());
        Record record1 = new Record("Facebook", "www.facebook.com", "eren", "12345");
        Record record2 = new Record( "Twitter", "www.twitter.com", "arda", "elbetbet");
        ArrayList records = new ArrayList<Record>();
        records.add(record1);
        records.add(record2);
        records.add(new Record( "Google", "www.google.com", "efe", "robokop"));
        Table passwordTable = new Table( records);
        System.out.println( protector.encrypt("123456", passwordTable));

        ArrayList<Record> output = new ArrayList<Record>();
        protector.decrypt("123456", output);
    }

}