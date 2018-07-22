/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data.model;

import java.util.Date;

/**
 *
 * @author shawh
 */
public class ClientView {
    
    private Integer customerId;
    
    private String customerName;
   
    private boolean active;
    
    private Date createDate;
   
    private String phone;
        
    private String address;
    
    private Integer appointmentCount;

    public ClientView(Integer customerId, String customerName, boolean active, Date createDate, String phone, String address, Integer appointmentCount) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.active = active;
        this.createDate = createDate;
        this.phone = phone;
        this.address = address;
        this.appointmentCount = appointmentCount;
    }

    public ClientView() {
          }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(Integer appointmentCount) {
        this.appointmentCount = appointmentCount;
    }
    
    
}
