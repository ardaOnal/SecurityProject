import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/*
    Manager class, responsible for the GUI of the actual password manager, as well as
    loading previous data and storing the data entered
 */
public class Manager {
    private Scene screen;
    private ArrayList<Record> information;
    private Protector protector;

    public Manager( ArrayList<Record> data, String masterPassword) {

        this.information = data;
        protector = new Protector( new BouncyCastleProvider());
        GridPane table = new GridPane();
        Label id = new Label("Name");
        Label url = new Label("URL");
        Label username = new Label("Username");
        Label password = new Label("Password");
        table.add(id,0,0);
        table.add(url,1,0);
        table.add(username,2,0);
        table.add(password,3,0);

        GridPane.setMargin(id, new Insets(5));
        GridPane.setMargin(url, new Insets(5));
        GridPane.setMargin(username, new Insets(5));
        GridPane.setMargin(password, new Insets(5));

        for(int i=1; i< information.size() + 1; i++){
            TextField textField = new TextField();
            textField.setAlignment(Pos.CENTER);
            TextField textField1 = new TextField();
            textField1.setAlignment(Pos.CENTER);
            TextField textField2 = new TextField();
            textField2.setAlignment(Pos.CENTER);
            TextField passwordField = new TextField();
            passwordField.setAlignment(Pos.CENTER);

            // adding textFields to the gridPane
            table.add(textField, 0, i);
            table.add(textField1, 1, i);
            table.add(textField2, 2, i);
            table.add(passwordField, 3, i);

            // set up the margins
            GridPane.setMargin(textField, new Insets(5));
            GridPane.setMargin(textField1, new Insets(5));
            GridPane.setMargin(textField2, new Insets(5));
            GridPane.setMargin(passwordField, new Insets(5));
        }

        Button btn = new Button("Add"); //add button
        Button btn2 = new Button("Save"); //save button
        btn.setOnAction(new EventHandler<ActionEvent>() {

            // adding a new row to the gui, and a new record object to the
            // arraylist which holds the information
            @Override
            public void handle(ActionEvent e)
            {
                GridPane.clearConstraints(table);

                TextField textField = new TextField();
                textField.setAlignment(Pos.CENTER);
                TextField textField1 = new TextField();
                textField1.setAlignment(Pos.CENTER);
                TextField textField2 = new TextField();
                textField2.setAlignment(Pos.CENTER);
                TextField passwordField = new TextField();
                passwordField.setAlignment(Pos.CENTER);


                //add them to the GridPane
                table.add(textField, 0, table.getRowCount()); //  (child, columnIndex, rowIndex)
                table.add(textField1, 1, table.getRowCount() - 1); //  (child, columnIndex, rowIndex)
                table.add(textField2, 2, table.getRowCount() - 1); //  (child, columnIndex, rowIndex)
                table.add(passwordField, 3, table.getRowCount() - 1); //  (child, columnIndex, rowIndex)

                // set up the margins
                GridPane.setMargin(textField, new Insets(5));
                GridPane.setMargin(textField1, new Insets(5));
                GridPane.setMargin(textField2, new Insets(5));
                GridPane.setMargin(passwordField, new Insets(5));

                HBox hbBtn = new HBox(10);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                hbBtn.getChildren().add(btn);
                hbBtn.getChildren().add(btn2);

                table.add(hbBtn, 3, table.getRowCount());
                information.add(new Record());

            }
        });
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            // saving the information entered to the field
            @Override
            public void handle(ActionEvent e)
            {
                ArrayList<Record> newInformation = new ArrayList<>();
                String name = "";
                String url = "";
                String username = "";
                String password = "";

                for (Node node : table.getChildren()) {

                    int rowIndex = GridPane.getRowIndex(node);
                    int colIndex = GridPane.getColumnIndex(node);

                    if (rowIndex > 0 ) {
                        if (node instanceof HBox)
                        {
                            continue;
                        }
                        if (colIndex == 0) {
                            name = ((TextField) node).getText();
                        } else if (colIndex == 1) {
                            url = ((TextField) node).getText();
                        } else if (colIndex == 2) {
                            username = ((TextField) node).getText();
                        } else if (colIndex == 3) {
                            password = ((TextField) node).getText();
                            newInformation.add(new Record(name, url, username, password));
                        }
                    }
                }
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
                } catch (Exception exception) {
                    System.out.println(exception);
                }
                File aesFile = new File("encryptedFiles/" + iv + ".aes");
                aesFile.delete();

                /* encrypting the information to an aes file named <iv>.aes
                we chose this name so that the file can be found directly
                for encrypting and decrypting, and iv is by definition public so it works*/
                protector.encrypt( masterPassword, new Table( newInformation));

            }
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        hbBtn.getChildren().add(btn2);


        table.add(hbBtn, 3, information.size() + 1);

        table.setAlignment(Pos.CENTER);
        /* setting the textFields with previous values of them, which
            have been saved to the aes file and decrypted, now stored in the
            variable called "information"
         */
        for (Node node : table.getChildren()) {
            int rowIndex = GridPane.getRowIndex(node);
            int colIndex = GridPane.getColumnIndex(node);
            if (rowIndex > 0 && rowIndex < information.size() + 1) {
                if( colIndex == 0){
                    ((TextField) node).setText( information.get( rowIndex - 1).getSite());
                }
                else if( colIndex == 1){
                    ((TextField) node).setText( information.get( rowIndex - 1).getUrl());
                }
                else if( colIndex == 2){
                    ((TextField) node).setText( information.get( rowIndex - 1).getUsername());
                }
                else if( colIndex == 3){

                    ((TextField) node).setText( information.get( rowIndex - 1).getPassword());
                }
            }

        }
        screen = new Scene(table, 800, 500);

    }

    public Scene getScene() {
        return screen;
    }
    public void setInformation(ArrayList<Record> information){
        this.information = information;
    }
    public ArrayList<Record> getInformation(){
        return information;
    }
}
