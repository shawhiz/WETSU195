/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data.model;

import java.util.Date;
public class User  {
    
        private static Integer idTicker = 0;

     private Integer userId;
    
    private String userName;
   
    private String password;
    
    private short active;
    
    private int createBy;
    
    private Date createDate;
    
    private Date lastUpdate;
    
    private int lastUpdatedBy;

    public User() {
    }
        public static Integer getNewId(){
        idTicker = idTicker + 1;
        return idTicker;
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(Integer userId, String userName, String password, short active, int createBy, Date createDate, Date lastUpdate, int lastUpdatedBy) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createBy = createBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public short getActive() {
        return active;
    }

    public void setActive(short active) {
        this.active = active;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(int lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
   
}
