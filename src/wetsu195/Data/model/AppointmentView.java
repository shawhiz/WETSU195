/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data.model;

import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author shawh
 */
public class AppointmentView {
    
    protected IntegerProperty appointmentId;
    protected  StringProperty title;
    protected StringProperty description;
    protected StringProperty location;
    protected StringProperty contact;
    protected StringProperty customerName;
    protected StringProperty url;
    protected Date startDate;
    protected Date stopDate;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public AppointmentView() {
    }
    
}
