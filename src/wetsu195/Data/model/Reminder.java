/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data.model;

import java.util.Date;

public class Reminder  {

        private static Integer idTicker = 0;
  
    private Integer reminderId;
    
    private Date reminderDate;
    
    private int snoozeIncrement;
   
    private int snoozeIncrementTypeId;
    
    private int appointmentId;
   
    private String createdBy;
    
    private Date createdDate;
    
    private String remindercol;

    public Reminder() {
    }
        public static Integer getNewId(){
        idTicker = idTicker + 1;
        return idTicker;
    }

    public Reminder(Integer reminderId) {
        this.reminderId = reminderId;
    }

    public Reminder(Integer reminderId, Date reminderDate, int snoozeIncrement, int snoozeIncrementTypeId, int appointmentId, String createdBy, Date createdDate, String remindercol) {
        this.reminderId = reminderId;
        this.reminderDate = reminderDate;
        this.snoozeIncrement = snoozeIncrement;
        this.snoozeIncrementTypeId = snoozeIncrementTypeId;
        this.appointmentId = appointmentId;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.remindercol = remindercol;
    }

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public int getSnoozeIncrement() {
        return snoozeIncrement;
    }

    public void setSnoozeIncrement(int snoozeIncrement) {
        this.snoozeIncrement = snoozeIncrement;
    }

    public int getSnoozeIncrementTypeId() {
        return snoozeIncrementTypeId;
    }

    public void setSnoozeIncrementTypeId(int snoozeIncrementTypeId) {
        this.snoozeIncrementTypeId = snoozeIncrementTypeId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getRemindercol() {
        return remindercol;
    }

    public void setRemindercol(String remindercol) {
        this.remindercol = remindercol;
    }
}
