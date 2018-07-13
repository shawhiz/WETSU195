/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import wetsu195.Data.DbMgr;
import wetsu195.Data.model.User;

/**
 *
 * @author shawh
 */
public class LoginController implements Initializable {

    private static final DbMgr db = new DbMgr() {};
    private Label label;
    @FXML
    private AnchorPane root;
    @FXML
    private Button login;
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    private ImageView logo;
    @FXML
    private ChoiceBox<?> locale;

    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public User checkCredentials(String username, String password) throws SQLException, ClassNotFoundException {
        return db.getUserByCredentials(username, password);
    }

    @FXML
    public void Login() throws SQLException, ClassNotFoundException {
        String name = username.getText();
        String pass = password.getText();
        User activeUser = checkCredentials(name, pass);
        if (activeUser != null) {
            login.setText(activeUser.getUserName()+ " is logged in");
            showClientScreen();
        } else {
            showInvalidLogin();
        }

    }

    private void showInvalidLogin() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Login");
        alert.setHeaderText("Invalid Login");
        alert.setContentText("Your credentials were not found. Please try again");
        alert.showAndWait();      
    }

    public void showClientScreen() {
        try {
        Parent root = FXMLLoader.load(getClass().getResource("MainApp.fxml"));
      
           // SplitPane clientScreen = (SplitPane) loader.load();
            Scene clientScene = new Scene(root);
            //   ClientsController controller = loader.getController();
            Stage clientsStage = new Stage();
            clientsStage.setScene(clientScene);
            clientsStage.show();
            Stage thisStage = (Stage) login.getScene().getWindow();
            thisStage.close();
        } catch (IOException e) {
            //do something with this error?
        }
    }

}
