/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import static com.sun.javaws.Globals.getDefaultLocale;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import wetsu195.Data.DbMgr;
import wetsu195.Data.model.User;

/**
 *
 * @author shawh
 */
public class LoginController implements Initializable {

    private static final DbMgr db = new DbMgr() {
    };
    @FXML
    private AnchorPane root;
    @FXML
    private Button login;
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    private Pane choiceBoxHolder;

    private static Locale locale;
    @FXML
    private ImageView logo;

    private ResourceBundle language;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        language = rb;
        setLabelsForLanguage();
        initializeLocaleChoiceBox();
    }

    public User checkCredentials(String username, String password) {
        try {
            return db.getUserByCredentials(username, password);
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @FXML
    public void Login() throws IOException {
        String name = username.getText();
        String pass = password.getText();
        User activeUser = checkCredentials(name, pass);
        if (activeUser != null) {
            showMainApp();
        } else {
            showInvalidLogin();
        }

    }

    private void showInvalidLogin() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(language.getString("invalid_login"));
        alert.setHeaderText(language.getString("invalid_login"));
        alert.setContentText(language.getString("invalid_credentials"));
        alert.showAndWait();
    }

    public void showMainApp() throws IOException {
            Parent parent = FXMLLoader.load(getClass().getResource("MainApp.fxml"));
            Scene mainScene = new Scene(parent);
            Stage mainStage = new Stage();
            mainStage.setScene(mainScene);
            mainStage.show();
            mainStage.setTitle("Shannon Moore - Smoo182 - C195");
            Stage thisStage = (Stage) login.getScene().getWindow();
            thisStage.close();
        
    }

    public static List<Locale> getSupportedLocales() {
        return new ArrayList<>(Arrays.asList(Locale.ENGLISH, Locale.FRENCH));
    }

    public static Locale getDefaultLocale() {
        Locale sysDefault = Locale.getDefault();
        return getSupportedLocales().contains(sysDefault) ? sysDefault : Locale.ENGLISH;
    }

    public void setLocale(String selectedLanguage) {
        if (selectedLanguage == "French") {
            Locale.setDefault(Locale.FRENCH);
        } else {
            Locale.setDefault(Locale.ENGLISH);
        }
        language = ResourceBundle.getBundle("wetsu195.Resources.Labels");
        setLabelsForLanguage();

    }

    private void setLabelsForLanguage() {
        login.setText(language.getString("login"));
        username.setPromptText(language.getString("username"));
        password.setPromptText(language.getString("password"));
    }

    private void initializeLocaleChoiceBox() {
        ChoiceBox localeChoiceBox = new ChoiceBox();
        localeChoiceBox.getItems().addAll("English", "French");
        localeChoiceBox.getStyleClass().add("button-date");

        localeChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setLocale(newValue);
            }
        });

        String displayLanguage = language.getLocale().getDisplayLanguage();

        if (localeChoiceBox.getItems().contains(displayLanguage)) {
            localeChoiceBox.setValue(displayLanguage);
        } else {
            localeChoiceBox.setValue("English");
        }
        choiceBoxHolder.getChildren().add(localeChoiceBox);
    }

}
