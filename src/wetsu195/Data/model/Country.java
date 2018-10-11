/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data.model;

import java.util.Date;

public class Country {

    private static Integer idTicker = 0;

    private Integer countryId;

    private String country;

    private Date createDate;

    private int createdBy;

    private Date lastUpdate;

    private int lastUpdateBy;

    public Country() {
    }

    public Country(Integer countryId) {
        this.countryId = countryId;
    }

    public static Integer getNewId() {
        idTicker = idTicker + 1;
        return idTicker;
    }

    public Country(Integer countryId, String country, Date createDate, int createdBy, Date lastUpdate, int lastUpdateBy) {
        this.countryId = countryId;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
