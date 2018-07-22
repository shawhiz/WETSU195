/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wetsu195.Data.DbMgr;
import wetsu195.Data.model.ClientView;

/**
 * FXML Controller class
 *
 * @author shawh
 */
public class ClientsController implements Initializable {

    private static final DbMgr db = new DbMgr() {
    };

    @FXML
    private TextField clientSearchField;
    @FXML
    private TableView<ClientView> clientList;
    @FXML
    private Label header;
    @FXML
    private Separator headerLine;
    @FXML
    private Button addClientButton;
    @FXML
    private TableColumn<ClientView, String> clientName;
    @FXML
    private TableColumn<ClientView, String> clientPhone;
    @FXML
    private TableColumn<ClientView, Boolean> clientActive;
    @FXML
    private TableColumn<ClientView, String> clientAddress;
    @FXML
    private TableColumn<ClientView, Integer> clientAppointments;
    @FXML
    private TableColumn<ClientView, String> clientAction;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        clientName.prefWidthProperty().bind(clientList.widthProperty().multiply(.3));
        clientPhone.prefWidthProperty().bind(clientList.widthProperty().multiply(.2));
        clientActive.prefWidthProperty().bind(clientList.widthProperty().multiply(.1));
        clientAddress.prefWidthProperty().bind(clientList.widthProperty().multiply(.3));
        clientAppointments.prefWidthProperty().bind(clientList.widthProperty().multiply(.1));
        
        clientList.setRowFactory( tv -> {
            TableRow<ClientView> row = new TableRow<>();
            row.setOnMouseClicked( event -> {
                if( event.getClickCount() ==2 && (! row.isEmpty())){
                    ClientView clickedClient = row.getItem();
                    editClient(clickedClient);              
                }
            });
            return row;
        });
        
        
        clientName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        clientPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        clientActive.setCellValueFactory(new PropertyValueFactory<>("active"));
        clientAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        clientAppointments.setCellValueFactory(new PropertyValueFactory<>("appointmentCount"));

        FilteredList<ClientView> filteredClients = new FilteredList<>(db.getClientView(), p -> true);

        clientSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredClients.setPredicate(clientView -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (clientView.getCustomerName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            }
            );
        }
        );
        
        SortedList<ClientView> sortedClients = new SortedList<>(filteredClients);
        sortedClients.comparatorProperty().bind(clientList.comparatorProperty());
        clientList.setItems(sortedClients);
    }

        @FXML
        private void showClientScreen() throws IOException {
            Parent parent = FXMLLoader.load(getClass().getResource("Client.fxml"));
            Scene mainScene = new Scene(parent);
            Stage mainStage = new Stage();
            mainStage.setScene(mainScene);
            mainStage.show();
        }

    private void editClient(ClientView clickedClient) {
      try {
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getResource("Client.fxml"));
          Parent clientScreen = loader.load();
          Scene clientScene = new Scene(clientScreen);
          ClientController controller = loader.getController();
          controller.populateSelectedClient(clickedClient);
          controller.setModifyFields();
          Stage clientStage = new Stage();
          clientStage.setScene(clientScene);
          clientStage.show();
          
          
          
          
          
          
      } catch (IOException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }

    }


