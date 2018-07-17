/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author shawh
 */
public class MainAppController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private SplitPane mainSplit;
    @FXML
    private AnchorPane navHolder;
    @FXML
    public AnchorPane contentHolder;
    @FXML
    private AnchorPane nav;
    @FXML
    private ImageView logo;
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

    @FXML
    public void showClients() throws IOException {
        changeActiveLink(clients);
        AnchorPane clientPane = FXMLLoader.load(getClass().getResource("Clients.fxml"));
        contentHolder.getChildren().setAll(clientPane);
    }

    @FXML
    public void showAppointments() throws IOException {
        changeActiveLink(appointments);
        AnchorPane appointmentsPane = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        contentHolder.getChildren().setAll(appointmentsPane);
    }

    @FXML
    public void showCalendar() throws IOException {
        changeActiveLink(calendar);
        AnchorPane calendarPane = FXMLLoader.load(getClass().getResource("Calendar.fxml"));
        contentHolder.getChildren().setAll(calendarPane);
    }

    @FXML
    public void showReports() throws IOException {
        changeActiveLink(reports);
        AnchorPane reportsPane = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        contentHolder.getChildren().setAll(reportsPane);
    }

    public void signOut() {
        //
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    
    @FXML
    private void changeActiveLink(Button button) {
        if (button == clients) {
            clients.getStyleClass().add("active-nav");
        } else {
            clients.getStyleClass().remove("active-nav");
        }

        if (button == appointments) {
            appointments.getStyleClass().add("active-nav");
        } else {
            appointments.getStyleClass().remove("active-nav");
        }

        if (button == calendar) {
            calendar.getStyleClass().add("active-nav");
        } else {
            calendar.getStyleClass().remove("active-nav");
        }

        if (button == reports) {
            reports.getStyleClass().add("active-nav");
        } else {
            reports.getStyleClass().remove("active-nav");
        }

    }

}
