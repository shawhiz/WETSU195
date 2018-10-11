/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import wetsu195.Data.DbMgr;
import wetsu195.Data.model.Appointment;
import static wetsu195.Main.getDefaultLocale;

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
    private static final DbMgr db = new DbMgr() {
    };

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
    public void showReports() throws IOException {
        changeActiveLink(reports);
        AnchorPane reportsPane = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        contentHolder.getChildren().setAll(reportsPane);
    }

    public void signOut() {
        db.setActiveUser(null);

        showLoginScreen();
        Stage thisStage = (Stage) signout.getScene().getWindow();
        thisStage.close();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkForAlerts();
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

        if (button == reports) {
            reports.getStyleClass().add("active-nav");
        } else {
            reports.getStyleClass().remove("active-nav");
        }

    }

    private void checkForAlerts() {
        Appointment upcomingAppt = db.getUpcomingAppointment();
        if (upcomingAppt.getAppointmentId() != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Beginning Soon!");
            alert.setHeaderText("You have an appointment soon");
            ZonedDateTime zdt = ZonedDateTime.ofInstant(upcomingAppt.getStart().toLocalDateTime(), ZoneOffset.UTC, ZoneId.systemDefault());
            alert.setContentText("Title: " + upcomingAppt.getTitle() + "\rLocation: " + upcomingAppt.getLocation() + "\rBeginning at: " + zdt.toLocalDateTime().toString());
            alert.showAndWait();

        }
    }

    @FXML
    private void viewLoginLogs() {
        Desktop desktop = Desktop.getDesktop();
        File loginFile = new File("./loginlog.log");
        if (loginFile.exists()) {
            try {
                desktop.open(loginFile);
            } catch (IOException ex) {
                Logger.getLogger(MainAppController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void showLoginScreen() {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("wetsu195.Resources.Labels", getDefaultLocale());
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"), rb);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainAppController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
