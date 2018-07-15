/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author shawh
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
       
        ResourceBundle rb = ResourceBundle.getBundle("wetsu195.Resources.Labels", getDefaultLocale()); 

        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"), rb);    
        Scene scene = new Scene(root);     
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {      
        launch(args);
    }
    
    
        public static Locale getDefaultLocale() {
     Locale sysDefault = Locale.getDefault();
      return getSupportedLocales().contains(sysDefault) ? sysDefault: Locale.ENGLISH;
     
    }
        
        public static List<Locale> getSupportedLocales() {
         return new ArrayList(Arrays.asList(Locale.ENGLISH, Locale.FRENCH));
    }
}
