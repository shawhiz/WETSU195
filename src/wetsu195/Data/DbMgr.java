/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import wetsu195.Data.model.Address;
import wetsu195.Data.model.City;
import wetsu195.Data.model.Country;
import wetsu195.Data.model.Customer;
import wetsu195.Data.model.User;

/**
 *
 * @author shawh
 */
public abstract class DbMgr {

    private String url = "jdbc:mysql://52.206.157.109/U04dK8";
    private String user = "U04dK8";
    private String password = "53688211336";
    private Connection dbConnection;

    private static User activeUser = new User();

    private void connectToDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = DriverManager.getConnection(url, user, password);
        dbConnection.isValid(10);
        /*
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection.isValid(5)) {
                dbConnection = connection;
            }
        } catch (Exception ex) {
            showInvalidConnectionDialog(ex);
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
    }

    public void closeDbConnection() throws SQLException {
        if (dbConnection != null) {
            dbConnection.close();
        }
    }

    public static User getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(User aActiveUser) {
        activeUser = aActiveUser;
    }

    public ResultSet executeQuery(String sql) throws SQLException, ClassNotFoundException {
        connectToDB();
        try {

            return dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);
        } catch (SQLException ex) {
            showInvalidConnectionDialog(ex);
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeDbConnection();
        return null;
    }

    public int executeUpdate(String sql) throws SQLException, ClassNotFoundException {
        connectToDB();
        try {
            return dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeDbConnection();
        return 0;
    }

    private int getResultSize(ResultSet rs) {
        int size = 0;
        try {
            rs.last();
            size = rs.getRow();
            rs.first();
        } catch (Exception ex) {
            return 0;
        }
        return size;
    }

    public User getUserByCredentials(String username, String password) throws SQLException, ClassNotFoundException {
        String queryString = "Select * from user where username = \'" + username + "\'  AND password = \'" + password + "\'";
        ResultSet results = executeQuery(queryString);
        if (getResultSize(results) == 1) {
            try {
                User newUser = new User();
                newUser.setUserId(results.getInt("userId"));
                newUser.setUserName(results.getString("userName"));
                newUser.setPassword(results.getString("password"));
                newUser.setActive(results.getShort("active"));
                activeUser = newUser;
                return newUser;
            } catch (SQLException ex) {
                return null;
            }
        }
        return null;
    }

    public Integer saveNewCustomer(Customer customer, Address address, City city, Country country) throws SQLException, ClassNotFoundException {;
        try {
            Date now = new Date(); //getting the date to use for all inserts 1 time
            String createdBy = activeUser.getUserName(); //getting the activeuser username 1 time

            country.setCreateDate(now);
            country.setLastUpdate(now);
            country.setCreatedBy(createdBy);
            country.setLastUpdateBy(createdBy);
            Integer countryId = createCountry(country);
            
            city.setCreateDate(now);
            city.setLastUpdate(now);
            city.setCreatedBy(createdBy);
            city.setLastUpdateBy(createdBy);
            city.setCountryId(countryId);
            Integer cityId = createCity(city);
            
            address.setCreateDate(now);
            address.setLastUpdate(now);
            address.setCreatedBy(createdBy);
            address.setLastUpdateBy(createdBy);
            address.setCityId(cityId);
            Integer addressId = createAddress(address);
            
            customer.setCreateDate(now);
            customer.setLastUpdate(now);
            customer.setCreatedBy(createdBy);
            customer.setLastUpdateBy(createdBy);
            customer.setAddressId(addressId);
            Integer customerId = createCustomer(customer);
            
            return customerId;
        } catch (Exception ex){
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Integer createCountry(Country country) throws SQLException, ClassNotFoundException {
        int countryId = Country.getNewId();
        String updateString = "INSERT INTO city VALUES ("
                + countryId + ", "
                + country.getCountry() + ", "
                + country.getCreateDate() + ", "
                + country.getCreatedBy() + ", "
                + country.getLastUpdate() + ", "
                + country.getLastUpdateBy() + ") ";
        int results = executeUpdate(updateString);
        return (results > 0) ? countryId : null;
    }

    private Integer createCity(City city) throws SQLException, ClassNotFoundException {
        int cityId = City.getNewId();
        String updateString = "INSERT INTO city VALUES ("
                + cityId + ", "
                + city.getCity() + ", "
                + city.getCountryId() + ", "
                + city.getCreateDate() + ", "
                + city.getCreatedBy() + ", "
                + city.getLastUpdate() + ", "
                + city.getLastUpdateBy() + ")";
        int results = executeUpdate(updateString);
        return (results > 0) ? cityId : null;
    }

    private Integer createAddress(Address address) throws SQLException, ClassNotFoundException {
        int addressId = Address.getNewId();
        String updateString = "INSERT INTO address VALUES ("
                + addressId + ", "
                + address.getAddress() + ", "
                + address.getAddress2() + ", "
                + address.getCityId() + ", "
                + address.getPostalCode() + ", "
                + address.getPhone() + ", "
                + address.getCreateDate() + ", "
                + address.getCreatedBy() + ", "
                + address.getLastUpdate() + ", "
                + address.getLastUpdateBy() + ")";
        int results = executeUpdate(updateString);
        return (results > 0) ? addressId : null;
    }

    private Integer createCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        int customerId = Customer.getNewId();
        String updateString = "INSERT INTO customer VALUES( "
                + customerId + ", "
                + customer.getCustomerName() + ", "
                + customer.getAddressId() + ", "
                + ((customer.getActive() == true) ? 1 : 0) + ", "
                + new Date() + ", "
                + activeUser.getUserName() + ", "
                + new Date() + ", "
                + activeUser.getUserName() + ")";
        int results = executeUpdate(updateString);

        return (results > 0) ? customerId: null;

    }

    private void showInvalidConnectionDialog(Exception ex) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Database Connection Error");
        alert.setHeaderText("Connection to database could not be made");
        alert.setContentText("Database error reported: " + ex.getMessage());
        alert.showAndWait();
    }
}
