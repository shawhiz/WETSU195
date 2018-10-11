/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author shawh
 */
public class AppointmentView {

    protected IntegerProperty appointmentId;
    protected StringProperty title;
    protected StringProperty description;
    protected StringProperty location;
    protected StringProperty contact;
    protected StringProperty customerName;
    protected StringProperty url;
    protected StringProperty startDate;
    protected StringProperty stopDate;
    protected Timestamp startTimestamp;
    protected Timestamp stopTimestamp;

    private enum appointmentType {

        Lawn, Tree, Construction, Other
    };

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Timestamp startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Timestamp getStopTimestamp() {
        return stopTimestamp;
    }

    public void setStopTimestamp(Timestamp stopTimestamp) {
        this.stopTimestamp = stopTimestamp;
    }

    public IntegerProperty getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(IntegerProperty appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(StringProperty title) {
        this.title = title;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(StringProperty description) {
        this.description = description;
    }

    public String getLocation() {
        return location.get();
    }

    public void setLocation(StringProperty location) {
        this.location = location;
    }

    public String getContact() {
        return contact.get();
    }

    public void setContact(StringProperty contact) {
        this.contact = contact;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(StringProperty customerName) {
        this.customerName = customerName;
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(StringProperty url) {
        this.url = url;
    }

    public String getStartDate() {

        return startDate.get();
    }

    public void setStartDate(String startDate) {
        this.startDate = new SimpleStringProperty(startDate);
    }

    public String getStopDate() {
        return stopDate.get();
    }

    public void setStopDate(String stopDate) {
        this.stopDate = new SimpleStringProperty(stopDate);
    }

    public AppointmentView() {
    }

}
