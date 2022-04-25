import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Manager {
    private Scene screen;
    private ListView listView;

    public Manager() {

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

        for(int i=1; i< 10; i++){
            TextField textField = new TextField();
            textField.setAlignment(Pos.CENTER);
            TextField textField1 = new TextField();
            textField1.setAlignment(Pos.CENTER);
            TextField textField2 = new TextField();
            textField2.setAlignment(Pos.CENTER);
            PasswordField passwordField = new PasswordField();
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

        Button btn = new Button("asd");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e)
            {
                for (Node node : table.getChildren()) {
                    if (GridPane.getRowIndex(node) > 0 && GridPane.getRowIndex(node) < 7) {
                        System.out.print(((TextField) node).getText());
                    }
                }
            }
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        table.add(hbBtn, 3, 10);

        table.setAlignment(Pos.CENTER);

        screen = new Scene(table, 800, 500);
    }

    public Scene getScene() {
        return screen;
    }
}
