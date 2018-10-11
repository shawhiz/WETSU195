/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    private TableColumn<?, ?> typeColumn;
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

    private static final DbMgr db = new DbMgr() {
    };
    @FXML
    private HBox calendarfilters;
    @FXML
    private RadioButton all;
    @FXML
    private ToggleGroup calendar;
    @FXML
    private RadioButton byweek;
    @FXML
    private RadioButton bymonth;
    @FXML
    private Pane calendarNav;
    @FXML
    private Button prevBtn;
    @FXML
    private Label visibleTimes;
    @FXML
    private Button nextBtn;

    LocalDateTime searchStart = LocalDateTime.MIN;
    LocalDateTime searchStop = LocalDateTime.MAX;
   static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd");

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        calendarNav.visibleProperty().set(false);
        populateTable();
    }

    private void populateTable() {
        titleColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.2));
        descriptionColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.2));
        typeColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.1));
        customerColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.1));
        startColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.2));
        endColumn.prefWidthProperty().bind(appointmentsList.widthProperty().multiply(.2));

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("stopDate"));
        appointmentsList.setRowFactory(v -> {
            TableRow<AppointmentView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    AppointmentView clickedAppointment = row.getItem();
                    editAppointment(clickedAppointment);
                }
            });
            return row;
        });

        FilteredList<AppointmentView> filteredAppointments = new FilteredList<>(db.getAppointmentView(), p -> true);

        byweek.setOnAction(event -> {
            LocalDateTime now = LocalDateTime.now();
            DayOfWeek dayOfWeek = now.getDayOfWeek();
            searchStart = now.plusDays(dayOfWeek.getValue() * -1);//sunday?
            searchStop = now.plusDays(7 - dayOfWeek.getValue());//saturday?      
            visibleTimes.setText("week of " + dtf.format(searchStart.toLocalDate()));
            calendarNav.visibleProperty().set(true);

        });

        bymonth.setOnAction(event -> {
            LocalDate now = LocalDate.now();
            int lastDay = now.getMonth().length(now.isLeapYear());
            searchStart = LocalDateTime.of(now.getYear(), now.getMonthValue(), 1, 0, 0);
            searchStop = LocalDateTime.of(now.getYear(), now.getMonthValue(), lastDay, 0, 0);
            visibleTimes.setText(now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            calendarNav.visibleProperty().set(true);

        });
        
        all.setOnAction(event -> {
            calendarNav.visibleProperty().set(false);
            searchStart = LocalDateTime.MIN;
            searchStop = LocalDateTime.MAX;
            visibleTimes.setText("All Upcoming");
        });
        
        nextBtn.setOnAction(event -> {
            if(byweek.isSelected()){
                searchStart = searchStart.plusDays(7);
                searchStop = searchStop.plusDays(7);
                 visibleTimes.setText("week of " + dtf.format(searchStart.toLocalDate()));
            }
                if(bymonth.isSelected()){
                searchStart = searchStart.plusMonths(1);
                searchStop = searchStop.plusMonths(1);
                visibleTimes.setText(searchStart.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            }
        });
        prevBtn.setOnAction(event -> {
            if(byweek.isSelected()){
                searchStart = searchStart.plusDays(-7);
                searchStop = searchStop.plusDays(-7);
                 visibleTimes.setText("week of " + dtf.format(searchStart.toLocalDate()));
            }
         
            if(bymonth.isSelected()){
                searchStart = searchStart.plusMonths(-1);
                searchStop = searchStop.plusMonths(-1);
                visibleTimes.setText(searchStart.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            }
           
        });
        
        
        

        visibleTimes.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAppointments.setPredicate(appointmentView -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                LocalDateTime apptStart = ZonedDateTime.ofInstant(appointmentView.getStartTimestamp().toLocalDateTime(), ZoneOffset.UTC, ZoneId.systemDefault()).toLocalDateTime();

                if (searchStart.isBefore(apptStart) &&  searchStop.isAfter(apptStart)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        appointmentSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAppointments.setPredicate(appointmentView -> {
                 LocalDateTime apptStart = ZonedDateTime.ofInstant(appointmentView.getStartTimestamp().toLocalDateTime(), ZoneOffset.UTC, ZoneId.systemDefault()).toLocalDateTime();
                String lowerCaseFilter = newValue.toLowerCase();

                if ((newValue == null || newValue.isEmpty())  && (searchStart.isBefore(apptStart) &&  searchStop.isAfter(apptStart)))  {
                    return true;
                }

                if (appointmentView.getTitle().toLowerCase().contains(lowerCaseFilter) && (searchStart.isBefore(apptStart) &&  searchStop.isAfter(apptStart))) {
                    return true;
                }
                if (appointmentView.getLocation().toLowerCase().contains(lowerCaseFilter) && (searchStart.isBefore(apptStart) &&  searchStop.isAfter(apptStart))) {
                    return true;
                }
                if (appointmentView.getDescription().toLowerCase().contains(lowerCaseFilter) && (searchStart.isBefore(apptStart) &&  searchStop.isAfter(apptStart))) {
                    return true;
                }
                if (appointmentView.getCustomerName().toLowerCase().contains(lowerCaseFilter) && (searchStart.isBefore(apptStart) &&  searchStop.isAfter(apptStart))) {
                    return true;
                }
                  if (appointmentView.getType().toLowerCase().contains(lowerCaseFilter) && (searchStart.isBefore(apptStart) &&  searchStop.isAfter(apptStart))) {
                    return true;
                }
                if (appointmentView.getContact().toLowerCase().contains(lowerCaseFilter) && (searchStart.isBefore(apptStart) &&  searchStop.isAfter(apptStart))) {
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
