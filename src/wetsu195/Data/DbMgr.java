/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User aActiveUser) {
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

        connectToDB();
        String sql = "SELECT * from user where username = ? AND password = ?";

        PreparedStatement statement = dbConnection.prepareStatement(sql);
        statement.setString(1, username); //username
        statement.setString(2, password); //password   

        ResultSet results = statement.executeQuery();
        if (getResultSize(results) == 1) {
            activeUser.setUserId(results.getInt(1)); //userId
            activeUser.setUserName(results.getString(2)); //username
            activeUser.setPassword(results.getString(3)); //password
            activeUser.setActive((short) results.getInt(4)); //active 1 inactive 0
            activeUser.setCreateBy(results.getString(5)); //createBy
            activeUser.setCreateDate(results.getDate(6)); //createDate
            activeUser.setLastUpdate(results.getDate(7)); //lastUpdate
            activeUser.setLastUpdatedBy(results.getString(8)); //lastUpdateBy  

            closeDbConnection();
            return activeUser;
        }
        return null;
    }

    public Integer saveNewCustomer(Customer customer, Address address, City city, Country country) throws SQLException, ClassNotFoundException {;
        try {

            Integer countryId = createCountry(country);

            city.setCountryId(countryId);
            Integer cityId = createCity(city);

            address.setCityId(cityId);
            Integer addressId = createAddress(address);

            customer.setAddressId(addressId);
            Integer customerId = createCustomer(customer);

            return customerId;

        } catch (Exception ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Integer getInsertedId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT LAST_INSERT_ID() as 'id'; ";
        Statement statement = dbConnection.createStatement();
        ResultSet results = statement.executeQuery(sql);
        if (results.next()) {
            return results.getInt(1);
        }
        return null;
    }

    private Integer createCountry(Country country) throws SQLException, ClassNotFoundException {
        Integer newId = null;
        Timestamp date = new Timestamp(new Date().getTime());
        connectToDB();

        String sql = "INSERT INTO country (country, createdBy, createDate, lastUpdateBy) VALUES ( ?, ?, ?, ?)";

        PreparedStatement statement = dbConnection.prepareStatement(sql);
        statement.setString(1, country.getCountry()); //country
        statement.setString(2, activeUser.getUserName()); //createdBy
        statement.setString(3, date.toString()); //createDate
        statement.setString(4, activeUser.getUserName()); //lastUpdateBy     

        if ((statement.executeUpdate()) > 0) {
            newId = getInsertedId();
        }
        closeDbConnection();
        return newId;
    }

    private Integer createCity(City city) throws SQLException, ClassNotFoundException {
        Integer newId = null;
        Timestamp date = new Timestamp(new Date().getTime());
        connectToDB();

        String sql = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdateBy) VALUES ( ?, ?, ?, ?, ?)";

        PreparedStatement statement = dbConnection.prepareStatement(sql);
        statement.setString(1, city.getCity()); //city
        statement.setInt(2, city.getCountryId()); //countryId
        statement.setString(3, date.toString()); //createDate
        statement.setString(4, activeUser.getUserName()); //createdBy
        statement.setString(5, activeUser.getUserName()); //lastUpdateBy     

        if ((statement.executeUpdate()) > 0) {
            newId = getInsertedId();
        }
        closeDbConnection();
        return newId;
    }

    private Integer createAddress(Address address) throws SQLException, ClassNotFoundException {
        Integer newId = null;
        Timestamp date = new Timestamp(new Date().getTime());
        connectToDB();

        String sql = "INSERT INTO address (address, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES ( ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = dbConnection.prepareStatement(sql);
        statement.setString(1, address.getAddress()); //address
        statement.setString(2, address.getAddress2()); //address2
        statement.setInt(3, address.getCityId()); // city
        statement.setString(4, address.getPostalCode()); //postalcode
        statement.setString(5, date.toString()); //createDate
        statement.setString(6, activeUser.getUserName()); //createdBy
        statement.setString(7, activeUser.getUserName()); //lastUpdateBy     

        if ((statement.executeUpdate()) > 0) {
            newId = getInsertedId();
        }
        closeDbConnection();
        return newId;
    }

    private Integer createCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        Integer newId = null;
        Timestamp date = new Timestamp(new Date().getTime());
        connectToDB();

        String sql = "INSERT INTO customer ( customerName, addressId, active, createDate, createdBy, lastUpdateBy) VALUES ( ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = dbConnection.prepareStatement(sql);
        statement.setString(1, customer.getCustomerName()); //name
        statement.setInt(2, customer.getAddressId()); //addressId
        statement.setInt(3, (customer.getActive())? 1: 0); // active
        statement.setString(5, date.toString()); //createDate
        statement.setString(6, activeUser.getUserName()); //createdBy
        statement.setString(7, activeUser.getUserName()); //lastUpdateBy     

        if ((statement.executeUpdate()) > 0) {
            newId = getInsertedId();
        }
        closeDbConnection();
        return newId;
    }

    private void showInvalidConnectionDialog(Exception ex) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Database Connection Error");
        alert.setHeaderText("Connection to database could not be made");
        alert.setContentText("Database error reported: " + ex.getMessage());
        alert.showAndWait();
    }
}
