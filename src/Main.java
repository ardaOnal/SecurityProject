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
import java.io.File;

import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application
{

    public void start(Stage primaryStage) {


        GridPane gridPane = new GridPane();
        primaryStage.setTitle("Password Manager");
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);


        gridPane.setPadding(new Insets(25, 25, 25, 25));
        Protector protector = new Protector(new BouncyCastleProvider());
        ArrayList<Record> passwords = new ArrayList();

        if (isFirstLogin())
        {
            Text scenetitle = new Text("Welcome, please set a master password");
            scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            gridPane.add(scenetitle, 0, 0, 2, 1);

            Label passwordLabel = new Label("Password:");
            gridPane.add(passwordLabel, 0, 2);
            Label passwordLabel2 = new Label("Repeat password:");
            gridPane.add(passwordLabel2, 0, 3);

            PasswordField passwordBox = new PasswordField();
            gridPane.add(passwordBox, 1, 2);

            PasswordField passwordBox2 = new PasswordField();
            gridPane.add(passwordBox2, 1, 3);

            Button btn = new Button("Set password");
            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().add(btn);
            gridPane.add(hbBtn, 1, 4);

            final Text actiontarget = new Text();
            gridPane.add(actiontarget, 1, 6);

            btn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
                    System.out.println( passwordBox.getText() + " " + passwordBox2.getText());

                    if ( !passwordBox.getText().equals(passwordBox2.getText()))
                    {
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Passwords do not match");
                    }
                    else if( passwordBox.getText().equals("")){
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Password cannot be empty");
                    }
                    else if( passwordBox.getText().length() < 8){
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Password should be longer than 8 characters");
                    }

                    else if( !isAlphanumeric(passwordBox.getText())){
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Password should involve both letters and digits");
                    }

                    else
                    {
                        actiontarget.setFill(Color.GREEN);
                        actiontarget.setText("afferim plummy");
                        protector.encrypt(passwordBox.getText(), new Table());
                        ArrayList<Record> records= new ArrayList();
                        records.add(new Record("","","",""));
                        records.add(new Record("","","",""));
                        records.add(new Record("","","",""));
                        records.add(new Record("","","",""));
                        records.add(new Record("","","",""));
                        Manager manager = new Manager(records, passwordBox.getText());
                        primaryStage.setScene( manager.getScene());
                    }
                }
            });
        }
        else
        {
            Text scenetitle = new Text("Welcome, please enter the password");
            scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
            gridPane.add(scenetitle, 0, 0, 2, 1);


            Label passwordLabel = new Label("Password:");
            gridPane.add(passwordLabel, 0, 1);

            PasswordField passwordBox = new PasswordField();
            gridPane.add(passwordBox, 1, 1);

            Button btn = new Button("Login");
            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().add(btn);
            gridPane.add(hbBtn, 1, 2);

            final Text actiontarget = new Text();
            gridPane.add(actiontarget, 1, 6);

            btn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e)
                {

                    if ( protector.decrypt(passwordBox.getText(), passwords))
                    {
                        System.out.println( "In main : " + passwords);
                        actiontarget.setFill(Color.GREEN);
                        actiontarget.setText("Correct password");
                        Manager manager;
                        if ( passwords.size() == 0)
                        {
                            ArrayList<Record> records= new ArrayList();
                            records.add(new Record("","","",""));
                            records.add(new Record("","","",""));
                            records.add(new Record("","","",""));
                            records.add(new Record("","","",""));
                            records.add(new Record("","","",""));
                            manager = new Manager(records, passwordBox.getText());
                        }
                        else
                        {
                            manager = new Manager(passwords, passwordBox.getText());
                        }
                        primaryStage.setScene(manager.getScene());
                    }
                    else
                    {
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Incorrect password");
                    }
                }
            });
        }


        Scene scene = new Scene(gridPane, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.setScene(scene);
        primaryStage.show();



        /*
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
        protector.decrypt("123456", output);*/
    }

    public boolean isFirstLogin()
    {
        // Reading the iv from iv.txt file
        String iv = "";
        try {
            File ivFile = new File("encryptedFiles/iv.txt");
            Scanner scanner = new Scanner(ivFile);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                iv = data;
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        File aesFile = new File("encryptedFiles/" + iv + ".aes");
        return !(aesFile.exists() && !aesFile.isDirectory());
    }
    public boolean isAlphanumeric(String s){
        boolean hasDigit = false;
        boolean hasLetter = false;
        for (int i = 0; i < s.length(); i++) {
            char x = s.charAt(i);
            if (Character.isLetter(x)) {
                hasLetter = true;
                if(hasDigit){ return true;}
            }
            else if (Character.isDigit(x)) {
                hasDigit = true;
                if(hasLetter){ return true;}
            }
        }
        return false;
    }

}