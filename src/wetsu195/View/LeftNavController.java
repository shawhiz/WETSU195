/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import wetsu195.MainAppController;

/**
 * FXML Controller class
 *
 * @author shawh
 */
public class LeftNavController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private VBox menuHolder;
    @FXML
    private Button clients;
    @FXML
    private Button appointments;
    @FXML
    private Button calendar;
    @FXML
    private Button reports;
    @FXML
    private Button signout;

    MainAppController mainController = new MainAppController();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void showClients() throws IOException {
        mainController.showClients();
        clients.getStyleClass().add("active-nav");
        appointments.getStyleClass().remove("active-nav");
        calendar.getStyleClass().remove("active-nav");
        reports.getStyleClass().remove("active-nav");
    }

    public void showAppointments() throws IOException {
        mainController.showAppointments();
        clients.getStyleClass().remove("active-nav");
        appointments.getStyleClass().add("active-nav");
        calendar.getStyleClass().remove("active-nav");
        reports.getStyleClass().remove("active-nav");
    }

    public void showCalendar() throws IOException {
        mainController.showCalendar();
        clients.getStyleClass().remove("active-nav");
        appointments.getStyleClass().remove("active-nav");
        calendar.getStyleClass().add("active-nav");
        reports.getStyleClass().remove("active-nav");
    }

    public void showReports() throws IOException {
        mainController.showReports();
        clients.getStyleClass().remove("active-nav");
        appointments.getStyleClass().remove("active-nav");
        calendar.getStyleClass().remove("active-nav");
        reports.getStyleClass().add("active-nav");
    }

    public void signout() {
        mainController.signOut();
    }

}
