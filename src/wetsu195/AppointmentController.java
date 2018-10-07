/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.io.IOException;
import java.net.URL;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.print.attribute.standard.DateTimeAtCompleted;
import wetsu195.Data.DbMgr;
import wetsu195.Data.model.Address;
import wetsu195.Data.model.Appointment;
import wetsu195.Data.model.AppointmentView;
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
    private TextField titletext;
    @FXML
    private Separator divider;
    private ButtonBar buttonbar;
    @FXML
    private ChoiceBox<String> customerAppt;
    @FXML
    private Button save;
    @FXML
    private Button cancel;
    @FXML
    private VBox clientinfo;
    @FXML
    private Label personal;
    @FXML
    private Button delete;
    @FXML
    private TextField contact;
    @FXML
    private TextField url;
    @FXML
    private Label servicedetails;
    @FXML
    private TextField description;
    @FXML
    private TextField location;
    @FXML
    private DatePicker appointmentdate;
    @FXML
    private Label schedule;
    @FXML
    private ComboBox<Integer> stophour;
    @FXML
    private ComboBox<Integer> stopminute;
    @FXML
    private ComboBox<Integer> startminute;
    @FXML
    private ComboBox<Integer> starthour;

    private Appointment editedappointment;
    List<Customer> customers;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            delete.visibleProperty().set(false);
            customers = db.getCustomers();
            customers.stream().forEach((customer) -> {
                customerAppt.getItems().add(customer.getCustomerName());
            });
        } catch (SQLException ex) {
            customerAppt.setDisable(true);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error: Customer List is empty");
            alert.setContentText("You must add customers first.");
            alert.showAndWait();
            Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        startminute.getItems().addAll(00, 15, 30, 45);
        stopminute.getItems().addAll(00, 15, 30, 45);
        starthour.getItems().addAll(9, 10, 11, 12, 1, 2, 3, 4, 5);
        stophour.getItems().addAll(9, 10, 11, 12, 1, 2, 3, 4, 5);

    }

    @FXML
    public void cancel() {
        db.getAppointmentView();
        Stage thisStage = (Stage) cancel.getScene().getWindow();
        thisStage.close();

    }

    public boolean validateTimes() {
        boolean isvalid = true;
        if (stophour.getValue() == 17 && stopminute.getValue() != 0) {
            isvalid = false;
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("Appointment time selected is outside of business hours");
            alert.setContentText("Appointments must end by 5pm");
            alert.setTitle("Appointment falls outside business hours");
            alert.showAndWait();
        }
        return isvalid;
    }

    public boolean validateInput() {
        boolean valid = true;

        if (contact.getText().isEmpty()) {
            valid = false;
        }
        if (location.getText().isEmpty()) {
            valid = false;
        }
        if (appointmentdate.getValue() == null) {
            valid = false;
        }
        if (starthour.getValue() == null) {
            valid = false;
        }
        if (stophour.getValue() == null) {
            valid = false;
        }
        if (startminute.getValue() == null) {
            valid = false;
        }
        if (stopminute.getValue() == null) {
            valid = false;
        }

        if (validateTimes() == false) {
            valid = false;
        }
        return valid;
    }

    public Integer buildAppointment() {

        //only building the values the user has entered. Other info will be populated at DB call time.
        int successful = 0;
        if (!validateInput()) {
            showInvalidInput();
        } else {

            Appointment appointment = new Appointment();

            for (Customer customer : customers) {
                if (customerAppt.getValue().toString() == customer.getCustomerName()) {
                    appointment.setCustomerId(customer.getCustomerId());
                }
            }
            appointment.setContact(contact.getText());
            appointment.setUrl(url.getText());
            appointment.setTitle(titletext.getText());
            appointment.setDescription(description.getText());
            appointment.setLocation(location.getText());

            Calendar start = Calendar.getInstance();
            LocalDate apptdate = appointmentdate.getValue();
            if (starthour.getValue() < 9) {
                starthour.setValue(starthour.getValue() + 12);
            }
            if (stophour.getValue() < 9) {
                stophour.setValue(stophour.getValue() + 12);
            }

            start.set(apptdate.getYear(), apptdate.getMonthValue(), apptdate.getDayOfMonth(), starthour.getValue(), startminute.getValue(), 0);
            Calendar stop = Calendar.getInstance();
            stop.set(apptdate.getYear(), apptdate.getMonthValue(), apptdate.getDayOfMonth(), stophour.getValue(), stopminute.getValue(), 0);
            appointment.setStart(start);
            appointment.setEnd(stop);

            try {
                successful = db.saveNewAppointment(appointment);
            } catch (SQLException ex) {
                Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            cancel();
        }

        return successful;
    }

    private void showInvalidInput() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Input for this appointment is not valid.");
        alert.setContentText("Please verify the apointment's detalis and correct the values.");
        alert.showAndWait();

    }

    @FXML
    public void saveAppointment() {
        if (editedappointment != null) {
            updateAppointment();
        } else {
            buildAppointment();
        }
    }

    private void updateAppointment() {

        editedappointment.setContact(contact.getText());
        editedappointment.setUrl(url.getText());
        editedappointment.setTitle(titletext.getText());
        editedappointment.setDescription(description.getText());
        editedappointment.setLocation(location.getText());

        if (starthour.getValue() < 9) {
            starthour.setValue(starthour.getValue() + 12);
        }
        if (stophour.getValue() < 9) {
            stophour.setValue(stophour.getValue() + 12);
        }
        
        Calendar start = Calendar.getInstance();
        LocalDate apptdate = appointmentdate.getValue();
        start.set(apptdate.getYear(), apptdate.getMonthValue(), apptdate.getDayOfMonth(), starthour.getValue(), startminute.getValue(), 0);
        Calendar stop = Calendar.getInstance();
        stop.set(apptdate.getYear(), apptdate.getMonthValue(), apptdate.getDayOfMonth(), stophour.getValue(), stopminute.getValue(), 0);
        editedappointment.setStart(start);
        editedappointment.setEnd(stop);
        editedappointment.setLastUpdateBy(db.getActiveUser().getUserName());

        db.saveUpdatedAppointment(editedappointment);
        cancel();
    }

    void populateSelectedAppointment(AppointmentView clickedAppointment) {
        try {
            editedappointment = db.getAppointment(clickedAppointment.getAppointmentId().get());
            customerAppt.setValue(clickedAppointment.getCustomerName());
            customerAppt.setDisable(true); //customer cannot be changed
            contact.setText(editedappointment.getContact());
            url.setText(editedappointment.getUrl());
            titletext.setText(editedappointment.getTitle());
            description.setText(editedappointment.getDescription());
            location.setText(editedappointment.getLocation());

            Calendar startCalendar = editedappointment.getStart();
            LocalDate localDate = LocalDate.of(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));
            Calendar stopCalendar = editedappointment.getEnd();
            appointmentdate.setValue(localDate);

            starthour.setValue(startCalendar.get(Calendar.HOUR));
            startminute.setValue(startCalendar.get(Calendar.MINUTE));
            stophour.setValue(stopCalendar.get(Calendar.HOUR));
            stopminute.setValue(stopCalendar.get(Calendar.MINUTE));

        } catch (Exception ex) {
            Logger.getLogger(AppointmentController.class.getName()).log(Level.WARNING, null, ex);

        }
    }

    void setModifyFields() {
        delete.visibleProperty().set(true);
    }

    @FXML
    private void deleteAppointment(ActionEvent event) {
        try {
            db.deleteAppointment(editedappointment);
            cancel();

        } catch (Exception ex) {
            Logger.getLogger(AppointmentController.class.getName()).log(Level.WARNING, null, ex);
        }
    }

}
