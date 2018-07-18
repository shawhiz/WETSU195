/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
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
import wetsu195.Data.model.Customer;

/**
 * FXML Controller class
 *
 * @author shawh
 */
public class ClientController implements Initializable {

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
    private TextField street;
    @FXML
    private HBox citstatezip;
    @FXML
    private TextField city;
    @FXML
    private TextField state;
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
    public void cancel(){
            Stage thisStage = (Stage) cancel.getScene().getWindow();
            thisStage.close();
    }
    
    public void buildCustomer(){
        
        Customer customer = new Customer();
        customer.setCustomerName(name.getText());       
        customer.setCreatedBy(DbMgr.getActiveUser().getUserName());
        customer.setCreateDate(new Date());
       
    }
    
}
