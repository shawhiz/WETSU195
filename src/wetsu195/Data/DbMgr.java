/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data;

import wetsu195.Data.model.AppointmentView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import wetsu195.Data.model.Address;
import wetsu195.Data.model.Appointment;
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

    public static User activeUser = new User();
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd hh:mm a");
            
            


    private static ObservableList<ClientView> clientView = FXCollections.observableArrayList();
    private static ObservableList<AppointmentView> appointmentView = FXCollections.observableArrayList();

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
        User newUser = new User();
        ResultSet results = statement.executeQuery();
        if (getResultSize(results) == 1) {
            newUser.setUserId(results.getInt(1)); //userId
            newUser.setUserName(results.getString(2)); //username
            newUser.setPassword(results.getString(3)); //password
            newUser.setActive((short) results.getInt(4)); //active 1 inactive 0
            newUser.setCreateBy(results.getInt(5)); //createBy
            newUser.setCreateDate(results.getDate(6)); //createDate
            newUser.setLastUpdate(results.getDate(7)); //lastUpdate
            newUser.setLastUpdatedBy(results.getInt(8)); //lastUpdateBy  

            closeDbConnection();
            return newUser;
        }
        return null;
    }

    public Integer saveNewCustomer(Customer customer, Address address, City city, Country country) throws SQLException, ClassNotFoundException {
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
        statement.setInt(2, activeUser.getUserId()); //createdBy
        statement.setString(3, date.toString()); //createDate
        statement.setInt(4, activeUser.getUserId()); //lastUpdateBy     

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
        statement.setInt(4, activeUser.getUserId()); //createdBy
        statement.setInt(5, activeUser.getUserId()); //lastUpdateBy     

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
        statement.setInt(7, activeUser.getUserId()); //createdBy
        statement.setInt(8, activeUser.getUserId()); //lastUpdateBy     

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
        statement.setInt(3, (customer.getActive()) ? 1 : 0); // active
        statement.setString(4, date.toString()); //createDate
        statement.setInt(5, activeUser.getUserId()); //createdBy
        statement.setInt(6, activeUser.getUserId()); //lastUpdateBy     

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

    public List<Customer> getCustomers() throws SQLException, ClassNotFoundException {

        connectToDb();
        List<Customer> customers = new ArrayList<Customer>();
        String sql = "SELECT * FROM customer";
        Statement statement = dbConnection.createStatement();
        ResultSet results = statement.executeQuery(sql);

        while (results.next()) {
            Customer customer = new Customer();
            customer.setCustomerId(results.getInt(1)); //id
            customer.setCustomerName(results.getString(2));
            customer.setAddressId(results.getInt(3));
            customer.setActive((results.getInt(4) == 1) ? true : false);
            customer.setCreateDate(results.getDate(5));
            customer.setCreatedBy(results.getInt(6));
            customer.setLastUpdate(results.getDate(7));
            customer.setLastUpdateBy(results.getInt(8));

            customers.add(customer);
        }
        closeDbConnection();

        return customers;
    }

    public ObservableList<ClientView> getClientView() {
        try {
            clientView.clear();
            connectToDb();
            String sql = "SELECT * from clientView";
            Statement statement = dbConnection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            while (results.next()) {
                ClientView client = new ClientView();              
                
                client.setCustomerId(new SimpleIntegerProperty(results.getInt(1)));
                client.setCustomerName(new SimpleStringProperty(results.getString(2)));
                
                client.setActive(new SimpleBooleanProperty((results.getInt(3) == 1)));
                client.setPhone(new SimpleStringProperty(results.getString(4)));
                client.setAddress(new SimpleStringProperty(results.getString(5)));
                client.setAppointmentCount(new SimpleIntegerProperty(results.getInt(6)));

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

    public ObservableList<AppointmentView> getAppointmentView() {
        try {
            appointmentView.clear();
            connectToDb();

            String sql = "SELECT * from appointmentView where start > NOW()";
            Statement statement = dbConnection.prepareStatement(sql);
            ResultSet results = statement.executeQuery(sql);

            while (results.next()) {

                AppointmentView appointment = new AppointmentView();
                appointment.setAppointmentId(new SimpleIntegerProperty(results.getInt(1)));
                appointment.setTitle(new SimpleStringProperty(results.getString(2)));
                appointment.setDescription(new SimpleStringProperty(results.getString(3)));
                appointment.setLocation(new SimpleStringProperty(results.getString(4)));
                appointment.setContact(new SimpleStringProperty(results.getString(5)));
                appointment.setCustomerName(new SimpleStringProperty(results.getString(6)));
                appointment.setUrl(new SimpleStringProperty(results.getString(7)));
                appointment.setStartDate(formattedDate(results.getTimestamp(8)));
                appointment.setStopDate(formattedDate(results.getTimestamp(9)));
                appointment.setStartTimestamp(results.getTimestamp(8));
                appointment.setStopTimestamp(results.getTimestamp(9));
                appointment.setType(results.getString(10));
               
                appointmentView.add(appointment);
            }
            closeDbConnection();
            return appointmentView;

        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    String formattedDate(Timestamp timestamp){
         timestamp.toInstant();
        ZonedDateTime zdt = ZonedDateTime.ofInstant(timestamp.toLocalDateTime(), ZoneOffset.UTC, ZoneId.systemDefault());
        return dtf.format(zdt.toLocalDateTime());
    }

    public Integer saveNewAppointment(Appointment appointment) throws SQLException, ClassNotFoundException {
        Integer newId = null;
        Timestamp date = new Timestamp(new Date().getTime());
        connectToDb();

        String sql = "INSERT INTO appointment (customerid, title, description, location, contact, url, start, end, createdBy, lastUpdateBy, createDate, appointmenttype) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?)";

        PreparedStatement statement = dbConnection.prepareStatement(sql);
        statement.setInt(1, appointment.getCustomerId());
        statement.setString(2, appointment.getTitle());
        statement.setString(3, appointment.getDescription());
        statement.setString(4, appointment.getLocation());
        statement.setString(5, appointment.getContact());
        statement.setString(6, appointment.getUrl());
        statement.setTimestamp(7,appointment.getStart());
        statement.setTimestamp(8, appointment.getEnd());
        statement.setInt(9, activeUser.getUserId());
        statement.setInt(10, activeUser.getUserId());
        statement.setString(11, date.toString());
        statement.setString(12, appointment.getType());

        if ((statement.executeUpdate()) > 0) {
            newId = getInsertedId();
        }
        closeDbConnection();
        return newId;
    }

    public void deleteAppointment(Appointment appointment) {
        try {
            connectToDb();
            String sql = "DELETE from appointment where appointmentId = ? ";
            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setInt(1, appointment.getAppointmentId());
            statement.executeUpdate();

            closeDbConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Customer getCustomer(Integer customerId) throws SQLException {

        try {
            Customer customer = new Customer();

            connectToDb();
            String sql = "SELECT * FROM customer where customerId = ? ";

            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setInt(1, customerId); //username
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                customer.setCustomerId(results.getInt(1)); //id
                customer.setCustomerName(results.getString(2));
                customer.setAddressId(results.getInt(3));
                customer.setActive((results.getInt(4) == 1));
                customer.setCreateDate(results.getDate(5));
                customer.setCreatedBy(results.getInt(6));
                customer.setLastUpdate(results.getDate(7));
                customer.setLastUpdateBy(results.getInt(8));
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

            if (results.next()) {
                address.setAddressId(results.getInt(1)); //id
                address.setAddress(results.getString(2));
                address.setAddress2(results.getString(3));
                address.setCityId(results.getInt(4));
                address.setPostalCode(results.getString(5));
                address.setPhone(results.getString(6));
                address.setCreateDate(results.getDate(7));
                address.setCreatedBy(results.getInt(8));
                address.setLastUpdate(results.getDate(9));
                address.setLastUpdateBy(results.getInt(10));
            }

            closeDbConnection();
            return address;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public City getCity(int cityId) {

        try {
            City city = new City();

            connectToDb();
            String sql = "SELECT * FROM city where cityId = ? ";

            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setInt(1, cityId);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                city.setCityId(results.getInt(1)); //id
                city.setCity(results.getString(2));
                city.setCountryId(results.getInt(3));
                city.setCreateDate(results.getDate(4));
                city.setCreatedBy(results.getInt(5));
                city.setLastUpdate(results.getDate(6));
                city.setLastUpdateBy(results.getInt(7));
            }

            closeDbConnection();
            return city;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Country getCountry(int countryId) {

        try {
            Country country = new Country();

            connectToDb();
            String sql = "SELECT * FROM country where countryId = ? ";

            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setInt(1, countryId);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                country.setCountryId(results.getInt(1)); //id
                country.setCountry(results.getString(2));
                country.setCreateDate(results.getDate(3));
                country.setCreatedBy(results.getInt(4));
                country.setLastUpdate(results.getDate(5));
                country.setLastUpdateBy(results.getInt(6));
            }

            closeDbConnection();
            return country;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void deleteCustomer(Customer editedCustomer) {

        try {
            connectToDb();
            String sql = "DELETE from customer where customerId = ? ";
            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setInt(1, editedCustomer.getCustomerId());
            statement.executeUpdate();

            closeDbConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteAddress(Address editedAddress) {
        try {
            connectToDb();
            String sql = "DELETE from address where addressId = ? ";
            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setInt(1, editedAddress.getAddressId());
            statement.executeUpdate();

            closeDbConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteCity(City editedCity) {
        try {
            connectToDb();
            String sql = "DELETE from city where cityId = ? ";
            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setInt(1, editedCity.getCityId());
            statement.executeUpdate();

            closeDbConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteCountry(Country editedCountry) {
        try {
            connectToDb();
            String sql = "DELETE from country where countryId = ? ";
            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setInt(1, editedCountry.getCountryId());
            statement.executeUpdate();

            closeDbConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveUpdatedCustomer(Customer editedCustomer, Address editedAddress, City editedCity, Country editedCountry) {
        updateCustomer(editedCustomer);
        updateAddress(editedAddress);
        updateCity(editedCity);
        updateCountry(editedCountry);
    }

    private void updateCustomer(Customer editedCustomer) {
        try {
            connectToDb();

            String sql = "UPDATE customer set customerName = ?,   lastUpdateBy = ? where customerId = ?";

            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setString(1, editedCustomer.getCustomerName());
            statement.setInt(2, editedCustomer.getLastUpdateBy());
            statement.setInt(3, editedCustomer.getCustomerId());

            statement.executeUpdate();

            closeDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateAddress(Address editedAddress) {
        try {
            connectToDb();

            String sql = "UPDATE address set address = ?, address2 = ?, postalCode = ?, phone = ?,  lastUpdateBy = ? where addressId = ?";

            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setString(1, editedAddress.getAddress());
            statement.setString(2, editedAddress.getAddress2());
            statement.setString(3, editedAddress.getPostalCode());
            statement.setString(4, editedAddress.getPhone());
            statement.setInt(5, editedAddress.getLastUpdateBy());
            statement.setInt(6, editedAddress.getAddressId());

            statement.executeUpdate();

            closeDbConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateCity(City editedCity) {
        try {
            connectToDb();

            String sql = "UPDATE city set city = ?, lastUpdateBy = ? where cityId = ?";

            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setString(1, editedCity.getCity()); //city
            statement.setString(2, activeUser.getUserName()); //lastUpdateBy
            statement.setInt(3, editedCity.getCityId()); //cityId

            statement.executeUpdate();

            closeDbConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateCountry(Country editedCountry) {
        try {
            connectToDb();

            String sql = "UPDATE country set country = ?, lastUpdateBy = ? where countryId = ?";

            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setString(1, editedCountry.getCountry()); //country
            statement.setString(2, activeUser.getUserName()); //lastUpdateBy
            statement.setInt(3, editedCountry.getCountryId()); //countryId

            statement.executeUpdate();

            closeDbConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Appointment getAppointment(int get) {
        try {
            connectToDb();
            String sql = "SELECT appointmentId, customerId, title, description, location, contact, url, start, end, appointmentype  from appointment where appointmentid = ?";

            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setInt(1, get);
            ResultSet results = statement.executeQuery();
            Appointment appointment = new Appointment();

            while (results.next()) {
                appointment.setAppointmentId(results.getInt(1));
                appointment.setCustomerId(results.getInt(2));
                appointment.setTitle(results.getString(3));
                appointment.setDescription(results.getString(4));
                appointment.setLocation(results.getString(5));
                appointment.setContact(results.getString(6));
                appointment.setUrl(results.getString(7));
                appointment.setStart(results.getTimestamp(8));
                appointment.setEnd(results.getTimestamp(9));
                appointment.setTitle(results.getString(10));

            }
            closeDbConnection();
            return appointment;

        } catch (SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public void saveUpdatedAppointment(Appointment editedappointment) {
        try {
            connectToDb();

            String sql = "UPDATE appointment set contact = ?, url = ? ,title = ?, description = ?, location = ?, start =?,  end =?,  lastUpdateBy = ?, appointmenttype =? where appointmentId = ?";

            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setString(1, editedappointment.getContact());
            statement.setString(2, editedappointment.getUrl());
            statement.setString(3, editedappointment.getTitle());
            statement.setString(4, editedappointment.getDescription());
            statement.setString(5, editedappointment.getLocation());
            statement.setTimestamp(6, editedappointment.getStart());
            statement.setTimestamp(7, editedappointment.getEnd());
            statement.setInt(8, activeUser.getUserId());
            statement.setInt(9, editedappointment.getAppointmentId());
            statement.setString(10, editedappointment.getType());

            statement.executeUpdate();

            closeDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean validateOverlap(Appointment appt) {
        boolean valid = false;
        String sql = "";
        Timestamp newStart = appt.getStart();
        Timestamp newEnd = appt.getEnd();

        try {
            connectToDb();
            if (appt.getAppointmentId() == null) {
                sql = "SELECT count(*) from appointment where"
                        + "((start between ? and ?) "
                        + "or (end between ? and ?) "
                        + "or ( start < ? and end > ?))"
                        + " and createdby = ?";
            } else {
                sql = "SELECT count(*) from appointment where"
                        + "((start between ? and ?) "
                        + "or (end between ? and ?) "
                        + "or ( start < ? and end > ?))"
                        + "and appointmentid != ? "
                        + "and createdby = ?";
            }

            PreparedStatement statement = dbConnection.prepareStatement(sql);

            if (appt.getAppointmentId() == null) {
                statement.setTimestamp(1, newStart);
                statement.setTimestamp(2, newEnd);
                statement.setTimestamp(3, newStart);
                statement.setTimestamp(4, newEnd);
                statement.setTimestamp(5, newStart);
                statement.setTimestamp(6, newEnd);
                statement.setInt(7, activeUser.getUserId());
            } else {
                statement.setTimestamp(1, newStart);
                statement.setTimestamp(2, newEnd);
                statement.setTimestamp(3, newStart);
                statement.setTimestamp(4, newEnd);
                statement.setTimestamp(5, newStart);
                statement.setTimestamp(6, newEnd);
                statement.setInt(7, appt.getAppointmentId());
                statement.setInt(8, activeUser.getUserId());
            }
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                if (results.getInt(1) == 0) {
                    valid = true;
                }
            }
            closeDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }

    public Appointment getUpcomingAppointment() {
        Appointment appointment = new Appointment();
          try {
           
            connectToDb();
            //where start time between now and now+15mins
                    ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC"));

            String sql = "SELECT * FROM appointment where (start between ? and ?) and createdby =? ";

            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(zdt.toLocalDateTime()));
            statement.setTimestamp(2, Timestamp.valueOf(zdt.plusMinutes(15).toLocalDateTime()));
            statement.setInt(3, activeUser.getUserId());
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                 appointment.setAppointmentId(results.getInt(1));
                appointment.setCustomerId(results.getInt(2));
                appointment.setTitle(results.getString(3));
                appointment.setDescription(results.getString(4));
                appointment.setLocation(results.getString(5));
                appointment.setContact(results.getString(6));
                appointment.setUrl(results.getString(7));
                appointment.setStart(results.getTimestamp(8));
                appointment.setEnd(results.getTimestamp(9));
                appointment.setType(results.getString(10));
            }
            closeDbConnection();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  appointment;
    }
    }
