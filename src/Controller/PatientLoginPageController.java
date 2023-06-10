/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import PatientController.PatientDashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sql.ConnectionDb;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class PatientLoginPageController extends Stage implements Initializable {


    PatientRegisterPageController prc = new PatientRegisterPageController();

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void CreateAccountAction(ActionEvent event) throws IOException {

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        prc.goToRegisterPatient();
    }

    @FXML
    private void loginPatientAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        // getting the class reference by using the singleton pattern,
        ConnectionDb objDb = ConnectionDb.ConnectionDb();
        Connection c = objDb.getConnection();

        String query = "Select * FROM patient WHERE userName = ? && password = ?";

        PreparedStatement statement = c.prepareStatement(query);

        statement.setString(1,usernameField.getText());
        statement.setString(2,passwordField.getText());


        try {
            ResultSet resultSet =statement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("User not Found!");
            }else {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                String patient_id = resultSet.getString("id");
                new PatientDashboardController().setId(patient_id);
                new Redirection().reDirect("/PatientPages/PatientDashboard.fxml","Patient Dashboard");
            }
        }catch (SQLException ignored) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}