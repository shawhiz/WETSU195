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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import wetsu195.Data.model.User;

/**
 *
 * @author shawh
 */
public  abstract class DbMgr {

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

    public ResultSet runQuery(String query) throws SQLException, ClassNotFoundException {
        connectToDB();
        try {
            
            return dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(query);
        } catch (SQLException ex) {
            showInvalidConnectionDialog(ex);
            Logger.getLogger(DbMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeDbConnection();
        return null;
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

    public  User getUserByCredentials(String username, String password) throws SQLException, ClassNotFoundException {
        String queryString = "Select * from user where username = \'" + username + "\'  AND password = \'" + password + "\'";
        ResultSet results = runQuery(queryString);
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

    private void showInvalidConnectionDialog(Exception ex) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Database Connection Error");
        alert.setHeaderText("Connection to database could not be made");
        alert.setContentText("Database error reported: " + ex.getMessage());
        alert.showAndWait();
    }

}
