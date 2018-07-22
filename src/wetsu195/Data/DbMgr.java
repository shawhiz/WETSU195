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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import wetsu195.Data.model.Address;
import wetsu195.Data.model.City;
import wetsu195.Data.model.ClientView;
import wetsu195.Data.model.Country;
import wetsu195.Data.model.Customer;
import wetsu195.Data.model.User;

/**
 *
 * @author shawh
 */
public abstract class DbMgr {

    private static String url = "jdbc:mysql://52.206.157.109/U04dK8";
    private static String user = "U04dK8";
    private static String password = "53688211336";
    private Connection dbConnection;

    private static User activeUser = new User();

    private void connectToDb() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = DriverManager.getConnection(url, user, password);
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

        connectToDb();
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
        connectToDb();

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
        connectToDb();

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
        connectToDb();

        String sql = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = dbConnection.prepareStatement(sql);
        statement.setString(1, address.getAddress()); //address
        statement.setString(2, address.getAddress2()); //address2
        statement.setInt(3, address.getCityId()); // cityId
        statement.setString(4, address.getPostalCode()); //postalcode
        statement.setString(5, address.getPhone()); //phone
        statement.setString(6, date.toString()); //createDate
        statement.setString(7, activeUser.getUserName()); //createdBy
        statement.setString(8, activeUser.getUserName()); //lastUpdateBy     

        if ((statement.executeUpdate()) > 0) {
            newId = getInsertedId();
        }
        closeDbConnection();
        return newId;
    }

    private Integer createCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        Integer newId = null;
        Timestamp date = new Timestamp(new Date().getTime());
        connectToDb();

        String sql = "INSERT INTO customer ( customerName, addressId, active, createDate, createdBy, lastUpdateBy) VALUES ( ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = dbConnection.prepareStatement(sql);
        statement.setString(1, customer.getCustomerName()); //name
        statement.setInt(2, customer.getAddressId()); //addressId
        statement.setInt(3, (customer.getActive())? 1: 0); // active
        statement.setString(4, date.toString()); //createDate
        statement.setString(5, activeUser.getUserName()); //createdBy
        statement.setString(6, activeUser.getUserName()); //lastUpdateBy     

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
    
    private List <Customer> getCustomers() throws SQLException{
        
        List<Customer> customers = new ArrayList<Customer>();
        String sql = "SELECT * FROM customer";
        Statement statement = dbConnection.createStatement();
        ResultSet results = statement.executeQuery(sql);
        
        while(results.next()) {
            Customer customer = new Customer();
            customer.setCustomerId(results.getInt(1)); //id
            customer.setCustomerName(results.getString(2));
            customer.setAddressId(results.getInt(3));
            customer.setActive((results.getInt(4) == 1)? true : false);
            customer.setCreateDate(results.getDate(5));
            customer.setCreatedBy(results.getString(6));
            customer.setLastUpdate(results.getDate(7));
            customer.setLastUpdateBy(results.getString(8));
            
            customers.add(customer);          
        }
        
        return customers;
    }
    
    
    
    public ObservableList<ClientView> getClientView () {
        try {
            ObservableList<ClientView> clientView = FXCollections.observableArrayList();
            connectToDb();
            String sql = "SELECT * from clientView";
            Statement statement = dbConnection.createStatement();
            ResultSet results =  statement.executeQuery(sql);
            
            while (results.next()){
                ClientView client = new ClientView();
                client.setCustomerId(results.getInt(1));
                client.setCustomerName(results.getString(2));
                client.setActive((results.getInt(3) == 1));
                client.setPhone(results.getString(4));
                client.setAddress(results.getString(5));
                client.setAppointmentCount(results.getInt(6));
                
                clientView.add(client);
            }
             closeDbConnection();
            return clientView;
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
     }

    public Customer getCustomer(Integer customerId) throws SQLException {
        
        try {
            Customer customer = new Customer();
            
            connectToDb();
            String sql = "SELECT * FROM customer where customerId = ? ";
            
            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setInt(1, customerId); //username
            ResultSet results = statement.executeQuery();
            
            if(results.next()) {
                customer.setCustomerId(results.getInt(1)); //id
                customer.setCustomerName(results.getString(2));
                customer.setAddressId(results.getInt(3));
                customer.setActive((results.getInt(4) == 1));
                customer.setCreateDate(results.getDate(5));
                customer.setCreatedBy(results.getString(6));
                customer.setLastUpdate(results.getDate(7));
                customer.setLastUpdateBy(results.getString(8));
            }
            
            closeDbConnection();
            return customer;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
       
    }

    public Address getAddress(int addressId) {
        
        try {
            Address address = new Address();
            
            connectToDb();
            String sql = "SELECT * FROM address where addressId = ? ";
            
            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setInt(1, addressId); 
            ResultSet results = statement.executeQuery();
            
            if(results.next()) {
                address.setAddressId(results.getInt(1)); //id
                address.setAddress(results.getString(2));
                address.setAddress2(results.getString(3));
                address.setCityId((results.getInt(4) == 1));
                address.setPostalCode(results.getString(5));
                address.setPhone(results.getString(6));             
                address.setCreateDate(results.getDate(7));
                address.setCreatedBy(results.getString(8));
                address.setLastUpdate(results.getDate(9));
                address.setLastUpdateBy(results.getString(10));
            }
            
            closeDbConnection();
            return address;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
           }
}
