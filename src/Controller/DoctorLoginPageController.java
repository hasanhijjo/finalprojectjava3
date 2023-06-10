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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sql.ConnectionDb;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class DoctorLoginPageController extends Stage implements Initializable {




    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void LoginButtonAction(ActionEvent actionEvent) throws IOException {
        if(usernameField.getText().equals("123") && passwordField.getText().equals("123")) {
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
            new Redirection().reDirect("/Viewxml/AdminDashboardPage.fxml","Admin Dashboard Page");

        }else {
            System.out.println("Wrong Admin data");
        }
    }
}
