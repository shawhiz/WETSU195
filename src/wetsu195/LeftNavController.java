/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    

    /**
     * Initializes the controller class.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public MainAppController getMainController() throws IOException{
                 FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainApp.fxml"));   
            loader.load();
         return loader.getController();
    }
        

    public void showClients() throws IOException {      
        
//        clients.getStyleClass().add("active-nav");
    //    appointments.getStyleClass().remove("active-nav");
   //     calendar.getStyleClass().remove("active-nav");
  //      reports.getStyleClass().remove("active-nav");
         getMainController().showClients();
    }

    public void showAppointments() throws IOException {
        
    //    clients.getStyleClass().remove("active-nav");
     //   appointments.getStyleClass().add("active-nav");
    //    calendar.getStyleClass().remove("active-nav");
    //    reports.getStyleClass().remove("active-nav");
        getMainController().showAppointments();

    }

    public void showCalendar() throws IOException {
       getMainController().showCalendar();
        clients.getStyleClass().remove("active-nav");
        appointments.getStyleClass().remove("active-nav");
        calendar.getStyleClass().add("active-nav");
        reports.getStyleClass().remove("active-nav");
    }

    public void showReports() throws IOException {
        getMainController().showReports();
        clients.getStyleClass().remove("active-nav");
        appointments.getStyleClass().remove("active-nav");
        calendar.getStyleClass().remove("active-nav");
        reports.getStyleClass().add("active-nav");
    }

    public void signout() throws IOException {
        getMainController().signOut();
    }



}
