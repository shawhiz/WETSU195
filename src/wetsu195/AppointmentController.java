/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wetsu195.Data.DbMgr;
import wetsu195.Data.model.Address;
import wetsu195.Data.model.City;
import wetsu195.Data.model.ClientView;
import wetsu195.Data.model.Country;
import wetsu195.Data.model.Customer;

/**
 * FXML Controller class
 *
 * @author shawh
 */
public class AppointmentController implements Initializable {

    private static final DbMgr db = new DbMgr() {
    };

    @FXML
    private AnchorPane root;
    @FXML
    private Label title;
    @FXML
    private Separator divider;
    @FXML
    private ButtonBar buttonbar;
    @FXML
    private Button save;
    @FXML
    private Button cancel;
    @FXML
    private VBox clientinfo;
    @FXML
    private Label personal;
    @FXML
    private TextField name;
    @FXML
    private TextField phone;
    @FXML
    private Label address;
    @FXML
    private TextField address1;

    @FXML
    private TextField address2;
    @FXML
    private HBox citstatezip;
    @FXML
    private TextField city;
    @FXML
    private TextField country;
    @FXML
    private TextField zip;
    @FXML
    private Label status;
    @FXML
    private HBox statusradio;
    @FXML
    private RadioButton active;
    @FXML
    private ToggleGroup activeStatus;
    @FXML
    private RadioButton inactive;

    //used to keep track of objects in edit mode.
    private Customer editedCustomer;
    private Address editedAddress;
    private City editedCity;
    private Country editedCountry;
    @FXML
    private Button delete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        delete.visibleProperty().set(false);
    }

    @FXML
    public void cancel() {
             db.getClientView();
            Stage thisStage = (Stage) cancel.getScene().getWindow();
            thisStage.close();

    }

    public boolean validateInput() {
        boolean valid = true;

        if (name.getText().isEmpty()) {
            valid = false;
        }
        if (phone.getText().isEmpty()) {
            valid = false;
        }
        if (address1.getText().isEmpty()) {
            valid = false;
        }
        if (city.getText().isEmpty()) {
            valid = false;
        }
        if (country.getText().isEmpty()) {
            valid = false;
        }
        if (zip.getText().isEmpty()) {
            valid = false;
        }

        return valid;
    }

    public Integer buildCustomer() {
        buttonbar.setDisable(true);

        //only building the values the user has entered. Other info will be populated at DB call time.
        int successful = 0;
        if (!validateInput()) {

            buttonbar.setDisable(false);
            showInvalidInput();

        } else {
            Customer customer = new Customer();
            customer.setCustomerName(name.getText());
            customer.setActive(active.isSelected());

            Country newCountry = new Country();
            newCountry.setCountry(country.getText());

            City newCity = new City();
            newCity.setCity(city.getText());

            Address newAddress = new Address();
            newAddress.setAddress(address1.getText());
            newAddress.setAddress2(address2.getText());
            newAddress.setPostalCode(zip.getText());
            newAddress.setPhone(phone.getText());

            try {
                successful = db.saveNewCustomer(customer, newAddress, newCity, newCountry);
            } catch (SQLException ex) {
                Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            cancel();
        }

        return successful;
    }
    
    public void updateCustomer(){
        Integer successful = 0;
        editedCustomer.setCustomerName(name.getText());
        editedCustomer.setLastUpdateBy(db.getActiveUser().getUserName());
        
        editedAddress.setPhone(phone.getText());
        editedAddress.setAddress(address1.getText());
        editedAddress.setAddress2(address2.getText());
        editedAddress.setLastUpdateBy(db.getActiveUser().getUserName());
        
        editedCity.setCity(city.getText());
        editedCity.setLastUpdateBy(db.getActiveUser().getUserName());
        
        editedCountry.setCountry(country.getText());
        editedCountry.setLastUpdateBy(db.getActiveUser().getUserName());

         db.saveUpdatedCustomer(editedCustomer, editedAddress, editedCity, editedCountry);
          cancel();

        }
        

    private void showInvalidInput() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Input for this client is not valid.");
        alert.setContentText("Please verify the client's detalis and correct the values.");
        alert.showAndWait();

    }
    
    @FXML void saveCustomer()
    {
        if (editedCustomer != null){
            updateCustomer();
        }
        else {
            buildCustomer();
        }       
    }

    void populateSelectedClient(ClientView clickedClient) {
        try {
            editedCustomer = db.getCustomer(clickedClient.getCustomerId().get());   
        name.setText(editedCustomer.getCustomerName());
        editedAddress = db.getAddress(editedCustomer.getAddressId());
        address1.setText(editedAddress.getAddress());
        address2.setText(editedAddress.getAddress2());
        phone.setText(editedAddress.getPhone());
        zip.setText(editedAddress.getPostalCode());
        editedCity = db.getCity(editedAddress.getAddressId());
        city.setText(editedCity.getCity());
        editedCountry = db.getCountry(editedCity.getCountryId());
        country.setText(editedCountry.getCountry());
        
          } catch (SQLException ex) {
            Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setModifyFields() {
        delete.visibleProperty().set(true);
    }
    

    @FXML
    private void deleteCustomer(ActionEvent event) {
        try {
            db.deleteCountry(editedCountry);
            db.deleteCity(editedCity);
            db.deleteAddress(editedAddress);
            db.deleteCustomer(editedCustomer);
            cancel();

        } catch (Exception ex) {
            Logger.getLogger(AppointmentController.class.getName()).log(Level.WARNING, null, ex);
        }
    }

}
