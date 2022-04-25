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

public class Manager {
    private Scene screen;
    private ListView listView;
    private ArrayList<Record> information;
    private Protector protector;

    public Manager( ArrayList<Record> data, String masterPassword) {

        this.information = data;
        protector = new Protector( new BouncyCastleProvider());
        int rowCount = information.size();
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

            //add them to the GridPane
            table.add(textField, 0, i); //  (child, columnIndex, rowIndex)
            table.add(textField1, 1, i); //  (child, columnIndex, rowIndex)
            table.add(textField2, 2, i); //  (child, columnIndex, rowIndex)
            table.add(passwordField, 3, i); //  (child, columnIndex, rowIndex)

            // set up the margins
            GridPane.setMargin(textField, new Insets(5));
            GridPane.setMargin(textField1, new Insets(5));
            GridPane.setMargin(textField2, new Insets(5));
            GridPane.setMargin(passwordField, new Insets(5));
        }

        Button btn = new Button("Add");
        Button btn2 = new Button("Save");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e)
            {
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

                table.add(hbBtn, 3, table.getRowCount() + 1);
                information.add(new Record());

            }
        });
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e)
            {
                ArrayList<Record> newInformation = new ArrayList<>();
                String name = "";
                String url = "";
                String username = "";
                String password = "";

                for (Node node : table.getChildren()) {

                    if (node instanceof HBox)
                        continue;

                    int rowIndex = GridPane.getRowIndex(node);
                    int colIndex = GridPane.getColumnIndex(node);

                    if (rowIndex > 0 && rowIndex < information.size() + 1) {
                        if (colIndex == 0) {
                            name = ((TextField) node).getText();
                        } else if (colIndex == 1) {
                            url = ((TextField) node).getText();
                        } else if (colIndex == 2) {
                            username = ((TextField) node).getText();
                        } else if (colIndex == 3) {
                            System.out.println(rowIndex + " " + colIndex);
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
                protector.encrypt( masterPassword, new Table( newInformation));
            }
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        hbBtn.getChildren().add(btn2);


        table.add(hbBtn, 3, information.size() + 1);

        table.setAlignment(Pos.CENTER);
        System.out.println( information);
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
