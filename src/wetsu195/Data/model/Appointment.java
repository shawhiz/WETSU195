/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data.model;

import com.sun.javafx.css.CalculatedValue;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;


public class Appointment  {
    
        private static Integer idTicker = 0;

     private Integer appointmentId;
   
    private int customerId;
 
    private String title;
 
    private String description;

    private String location;

    private String contact;
 
    private String url;

    private Timestamp start;
    
    private Timestamp end;
    
    private Date createDate;
    
    private int createdBy;
  
    private Date lastUpdate;
   
    private int lastUpdateBy;
    
    public static enum TYPE { Lawn, Tree, Construction, Other };
    
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Appointment() {
    }
    
        public static Integer getNewId(){
        idTicker = idTicker + 1;
        return idTicker;
    }

    public Appointment(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Appointment(Integer appointmentId, int customerId, String title, String description, String location, String contact, String url, Timestamp start, Timestamp end, Date createDate, int createdBy, Date lastUpdate, int lastUpdateBy, String type) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.type = type;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(int lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

}
