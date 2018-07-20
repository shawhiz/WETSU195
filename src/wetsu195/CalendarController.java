/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

/**
 * FXML Controller class
 *
 * @author shawh
 */
public class CalendarController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label header;
    @FXML
    private Separator headerLine;
    @FXML
    private Button addAppointmentsButton;
    @FXML
    private HBox calendarViewToggle;
    @FXML
    private Button monthView;
    @FXML
    private Button weekView;
    @FXML
    private TilePane calendarPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void showAppointmentScreen(ActionEvent event) {
    }

    private void populateCalendar(Date start, Date stop) {

  //      int datesShown = daysBetween(start, stop).getDays();

    }

    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

}
