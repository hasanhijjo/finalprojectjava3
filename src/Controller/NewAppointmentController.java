/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sql.ConnectionDb;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class NewAppointmentController implements Initializable {
    @FXML
    DatePicker date;
    @FXML
    TextField day;
    @FXML
    TextField time;
    @FXML
    TextField nameP;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void addAppointment(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

        if(day.getText().equals("") || time.getText().equals("")) {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            new Redirection().reDirect("/Viewxml/AppointmentsFree.fxml","Free Appointment Page");
        }else {
            // getting the class reference by using the singleton pattern,
            ConnectionDb objDb = ConnectionDb.ConnectionDb();
            Connection c = objDb.getConnection();
            // Prepare the insert statement
            String insertQuery = "INSERT INTO appointment (date,day,time,nameP, status) VALUES (? , ? , ? , ?, ?)";
            PreparedStatement statement = c.prepareStatement(insertQuery);
            statement.setDate(1, Date.valueOf(date.getValue()));
            statement.setString(2, day.getText());
            statement.setString(3, time.getText());
            statement.setString(4,nameP.getText());
            statement.setString(5,"free");


            // Execute the insert statement
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected == 1) {
                System.out.println("Inserted Successfully");

            }
            // Close the database connection
            statement.close();
            c.close();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            new Redirection().reDirect("/Viewxml/AppointmentsFree.fxml","Free Appointment Page");
        }

    }

}
