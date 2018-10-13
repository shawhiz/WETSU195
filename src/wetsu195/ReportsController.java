/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import java.net.URL;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import jdk.nashorn.internal.objects.NativeArray;
import wetsu195.Data.DbMgr;
import wetsu195.Data.model.Appointment;
import java.text.DateFormatSymbols;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shawh
 */
public class ReportsController implements Initializable {

    @FXML
    private Separator headerLine;
    @FXML
    private Label header;
    @FXML
    private HBox reportsBox;
    @FXML
    private Button byMonth;
    @FXML
    private Button byConsultant;
    @FXML
    private Button byClient;
    @FXML
    AnchorPane reportResults;
    ResultSet results;
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd hh:mm a");

    private static final DbMgr db = new DbMgr() {
    };
    DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
    String[] monthNames = symbols.getMonths();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
        //Using a lambda here because this is only used here, saving having to write a separate method.
        byMonth.setOnAction(event -> {

            try {
                results = db.getReportResults(DbMgr.ReportType.byTypeMonthly);

                ArrayList<TreeItem> type = new ArrayList<TreeItem>();
                TreeItem lawnType = new TreeItem(Appointment.TYPE.Lawn);
                TreeItem treeType = new TreeItem(Appointment.TYPE.Tree);
                TreeItem consType = new TreeItem(Appointment.TYPE.Construction);
                TreeItem otherType = new TreeItem(Appointment.TYPE.Other);

                ArrayList<TreeItem> lawnMonths = new ArrayList<TreeItem>();
                ArrayList<TreeItem> treeMonths = new ArrayList<TreeItem>();
                ArrayList<TreeItem> constructionMonths = new ArrayList<TreeItem>();
                ArrayList<TreeItem> otherMonths = new ArrayList<TreeItem>();

                while (results.next()) {

                    if (results.getString(3).equals(Appointment.TYPE.Lawn.toString())) {
                        lawnMonths.add(new TreeItem(monthNames[(results.getInt(2) - 1)] + " " + results.getInt(1) + " : " + results.getInt(4)));
                    }
                    if (results.getString(3).equals(Appointment.TYPE.Tree.toString())) {
                        treeMonths.add(new TreeItem(monthNames[(results.getInt(2) - 1)] + " " + results.getInt(1) + " : " + results.getInt(4)));
                    }
                    if (results.getString(3).equals(Appointment.TYPE.Construction.toString())) {
                        constructionMonths.add(new TreeItem(monthNames[(results.getInt(2) - 1)] + " " + results.getInt(1) + " : " + results.getInt(4)));
                    }
                    if (results.getString(3).equals(Appointment.TYPE.Other.toString())) {
                        otherMonths.add(new TreeItem(monthNames[(results.getInt(2) - 1)] + " " + results.getInt(1) + " : " + results.getInt(4)));
                    }
                }

                lawnType.getChildren().addAll(lawnMonths);
                treeType.getChildren().addAll(treeMonths);
                consType.getChildren().addAll(constructionMonths);
                otherType.getChildren().addAll(otherMonths);

                type.add(lawnType);
                type.add(treeType);
                type.add(consType);
                type.add(otherType);

                reportResults.getChildren().clear();
                TreeView treeView = new TreeView();
                treeView.setPrefWidth(1200);
                treeView.setMinWidth(1200);
                treeView.setMinHeight(600);
                treeView.setPrefHeight(600);
                TreeItem rootItem = new TreeItem("Appointment By Type By Month");
                rootItem.getChildren().addAll(type);
                treeView.setRoot(rootItem);
                rootItem.setExpanded(true);
                treeView.setBackground(Background.EMPTY);
                reportResults.getChildren().setAll(treeView);

            } catch (SQLException ex) {
                Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        byClient.setOnAction(event -> {
            try {
                results = db.getReportResults(DbMgr.ReportType.byClientSchedule);
                ArrayList<TreeItem> clients = new ArrayList<TreeItem>();
                ArrayList<String> clientName = new ArrayList<String>();

                while (results.next()) {
                    String resultClient = results.getString(4);
                    if (!clientName.contains(resultClient)) {
                        clientName.add(resultClient);
                    }
                }

                for (String name : clientName) {
                    clients.add(new TreeItem(name));
                }

                //using lamba here to efficiently iterate in place of a traditional for loop
                clients.stream().forEach(client -> {
                    try {
                        results.beforeFirst();
                        while (results.next()) {
                            
                            if (results.getString(4).equals(client.getValue())) {
                                ZonedDateTime start = ZonedDateTime.ofInstant(results.getTimestamp(5).toLocalDateTime(), ZoneOffset.UTC, ZoneId.systemDefault());
                                ZonedDateTime end = ZonedDateTime.ofInstant(results.getTimestamp(6).toLocalDateTime(), ZoneOffset.UTC, ZoneId.systemDefault());
                                
                                String apptInfo = "Title: " + results.getString(2) + "\n"
                                        + "\tType: " + results.getString(3) + "\n"
                                        + "\tFrom: " + dtf.format(start) + " until " + dtf.format(end) + "\n"
                                        + "\tWith: " + results.getString(1);
                                
                                client.getChildren().add(new TreeItem(apptInfo));
                            }
                            
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    });

                reportResults.getChildren().clear();
                TreeView treeView = new TreeView();
                treeView.setPrefWidth(1200);
                treeView.setMinWidth(1200);
                treeView.setMinHeight(600);
                treeView.setPrefHeight(600);
                TreeItem rootItem = new TreeItem("Schedules by Client");
                rootItem.getChildren().addAll(clients);
                treeView.setRoot(rootItem);
                rootItem.setExpanded(true);
                treeView.setBackground(Background.EMPTY);
                reportResults.getChildren().setAll(treeView);
            } catch (SQLException ex) {
                Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        byConsultant.setOnAction(event -> {
            try {
                results = db.getReportResults(DbMgr.ReportType.byConsultantSchedule);
                ArrayList<TreeItem> consultants = new ArrayList<TreeItem>();
                ArrayList<String> consultantNames = new ArrayList<String>();

                while (results.next()) {
                    String resultconsult = results.getString(1);
                    if (!consultantNames.contains(resultconsult)) {
                        consultantNames.add(resultconsult);
                    }
                }

                for (String name : consultantNames) {
                    consultants.add(new TreeItem(name));
                }

                for (TreeItem consultant : consultants) {
                    results.beforeFirst();
                    while (results.next()) {

                        if (results.getString(1).equals(consultant.getValue())) {
                            ZonedDateTime start = ZonedDateTime.ofInstant(results.getTimestamp(5).toLocalDateTime(), ZoneOffset.UTC, ZoneId.systemDefault());
                            ZonedDateTime end = ZonedDateTime.ofInstant(results.getTimestamp(6).toLocalDateTime(), ZoneOffset.UTC, ZoneId.systemDefault());

                            String apptInfo = "Title: " + results.getString(2) + "\n"
                                    + "\tType: " + results.getString(3) + "\n"
                                    + "\tFrom: " + dtf.format(start) + " until " + dtf.format(end) + "\n"
                                    + "\tWith: " + results.getString(4);

                            consultant.getChildren().add(new TreeItem(apptInfo));
                        }

                    }
                }

                reportResults.getChildren().clear();
                TreeView treeView = new TreeView();
                treeView.setPrefWidth(1200);
                treeView.setMinWidth(1200);
                treeView.setMinHeight(600);
                treeView.setPrefHeight(600);
                TreeItem rootItem = new TreeItem("Schedules by Consultant");
                rootItem.getChildren().addAll(consultants);
                treeView.setRoot(rootItem);
                rootItem.setExpanded(true);
                treeView.setBackground(Background.EMPTY);
                reportResults.getChildren().setAll(treeView);
            } catch (SQLException ex) {
                Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

}
