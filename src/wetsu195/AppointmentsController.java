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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wetsu195.Data.DbMgr;
import wetsu195.Data.model.AppointmentView;
import wetsu195.Data.model.ClientView;

/**
 * FXML Controller class
 *
 * @author shawh
 */
public class AppointmentsController implements Initializable {

    @FXML
    private TableView<AppointmentView> appointmentsList;
    @FXML
    private TableColumn<?, ?> titleColumn;
    @FXML
    private TableColumn<?, ?> descriptionColumn;
    @FXML
    private TableColumn<?, ?> locationColumn;
    @FXML
    private TableColumn<?, ?> customerColumn;
    @FXML
    private TableColumn<?, ?> startColumn;
    @FXML
    private TableColumn<?, ?> endColumn;
    @FXML
    private Label header;
    @FXML
    private Separator headerLine;
    @FXML
    private Button addAppointmentsButton;
    
    @FXML
    private TextField appointmentSearchField;
    
    private static final DbMgr db = new DbMgr() { };

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateTable();
    }    

    private void populateTable() {
        titleColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.2));
        descriptionColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.2));
        locationColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.1));
        customerColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.1));
        startColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.2));
        endColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.2));
        
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("stopDate"));  
        appointmentsList.setRowFactory( v -> {
            TableRow<AppointmentView> row = new TableRow<>();
            row.setOnMouseClicked( event -> {
                if( event.getClickCount() ==2 && (! row.isEmpty())){
                    AppointmentView clickedAppointment = row.getItem();
                     editAppointment(clickedAppointment);                    
                }
            });
            return row;
        });
        
        FilteredList<AppointmentView> filteredAppointments = new FilteredList<>(db.getAppointmentView(), p -> true);
        appointmentSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAppointments.setPredicate(appointmentView -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                
                if(appointmentView.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            }
            );
        }
        );
        
        SortedList<AppointmentView> sortedAppointments = new SortedList<>(filteredAppointments);
        sortedAppointments.comparatorProperty().bind(appointmentsList.comparatorProperty());
        appointmentsList.setItems(sortedAppointments);
         

    }

    @FXML
    private void showAppointmentScreen(ActionEvent event) throws IOException {
                   Parent parent = FXMLLoader.load(getClass().getResource("Appointment.fxml"));
            Scene mainScene = new Scene(parent);
            Stage mainStage = new Stage();
            mainStage.setScene(mainScene);
            mainStage.show();
    }
    
    

   
    private boolean editAppointment(AppointmentView clickedAppointment) {
      try {
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getResource("Appointment.fxml"));
          Parent clientScreen = loader.load();
          Scene clientScene = new Scene(clientScreen);
          AppointmentController controller = loader.getController();
          controller.populateSelectedAppointment(clickedAppointment);
          controller.setModifyFields();
          Stage appointmentStage = new Stage();
          appointmentStage.setScene(clientScene);
          appointmentStage.show();

      } catch (IOException ex) {
            Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        } 
      return true;
    }
    
}
