/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author shawh
 */
public class ClientsController implements Initializable {

    @FXML
    private TextField clientSearchField;
    @FXML
    private ListView<?> clientList;
    @FXML
    private Label header;
    @FXML
    private Separator headerLine;
    @FXML
    private Button addClientButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}