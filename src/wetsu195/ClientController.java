/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import wetsu195.Data.model.Country;
import wetsu195.Data.model.Customer;

/**
 * FXML Controller class
 *
 * @author shawh
 */
public class ClientController implements Initializable {

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void cancel() {
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

    @FXML
    public Integer buildCustomer() {
        //only building the values the user has entered. Other info will be populated at DB call time.

        if (validateInput()) {
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
                return db.saveNewCustomer(customer, newAddress, newCity, newCountry);
            } catch (SQLException ex) {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        } else {
            showInvalidInput();
        }
        return null;
    }

    private void showInvalidInput() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
