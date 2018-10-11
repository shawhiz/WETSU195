/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data.model;

import java.util.Date;


public class Address  {

    private static Integer idTicker = 0;

    private Integer addressId;

    private String address;

    private String address2;

    private int cityId;

    private String postalCode;

    private String phone;

    private Date createDate;

    private int createdBy;

    private Date lastUpdate;
 
    private int lastUpdateBy;

    public Address() {
    }
    
    public static Integer getNewId(){
        idTicker = idTicker + 1;
        return idTicker;
    }

    public Address(Integer addressId) {
        this.addressId = addressId;
    }

    public Address(Integer addressId, String address, String address2, int cityId, String postalCode, String phone, Date createDate, int createdBy, Date lastUpdate, int lastUpdateBy) {
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
