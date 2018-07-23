/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data.model;

import java.util.Date;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author shawh
 */
public class ClientView {
    
   protected IntegerProperty customerId;
    
   protected StringProperty customerName;
   
    protected BooleanProperty active;
    
    protected Date createDate;
   
    protected StringProperty phone;
        
    protected StringProperty address;
    
   protected IntegerProperty appointmentCount;


    public ClientView() {
          }

    public IntegerProperty getCustomerId() {
        return customerId;
    }

    public void setCustomerId(IntegerProperty customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName.get();
    }



    public boolean isActive() {
        return active.get();
    }



    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPhone() {
        return phone.get();
    }



    public String getAddress() {
        return address.get();
    }



    public Integer getAppointmentCount() {
        return appointmentCount.get();
    }

    public void setCustomerName(StringProperty customerName) {
        this.customerName = customerName;
    }

    public void setActive(BooleanProperty active) {
        this.active = active;
    }

    public void setPhone(StringProperty phone) {
        this.phone = phone;
    }

    public void setAddress(StringProperty address) {
        this.address = address;
    }

    public void setAppointmentCount(SimpleIntegerProperty appointmentCount) {
        this.appointmentCount = appointmentCount;    }


    
    
}
