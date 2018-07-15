/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
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
    public  AnchorPane contentHolder;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void showClients() throws IOException{
        AnchorPane clientPane = FXMLLoader.load(getClass().getResource("View/Clients.fxml"));             
        contentHolder.getChildren().setAll(clientPane);
    }

    public void showAppointments() throws IOException {
       AnchorPane appointmentsPane = FXMLLoader.load(getClass().getResource("View/Appointments.fxml"));
       contentHolder.getChildren().setAll(appointmentsPane);  
    }

    public void showCalendar() throws IOException {
       AnchorPane calendarPane = FXMLLoader.load(getClass().getResource("View/Calendar.fxml"));
       contentHolder.getChildren().setAll(calendarPane);  
    }

    public void showReports() throws IOException {
       AnchorPane reportsPane = FXMLLoader.load(getClass().getResource("View/Reports.fxml"));
       contentHolder.getChildren().setAll(reportsPane);  
    }

    public void signOut() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
