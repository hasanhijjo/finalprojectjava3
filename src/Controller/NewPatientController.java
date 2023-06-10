package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sql.ConnectionDb;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewPatientController extends Stage implements Initializable  {

    @FXML
    private TextField firstNameP;
    @FXML
    private TextField lastNameP;
    @FXML
    private TextField userNameP;
    @FXML
    private TextField passwordP;
    @FXML
    private TextField emailP;
    @FXML
    private TextField phoneP;
    @FXML
    private TextField ageP;
    @FXML
    private ComboBox<String> gender;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gender.getItems().addAll("Male", "Female");

    }

    public void addPatient(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {


        if(passwordP.getText().equals("") || firstNameP.getText().equals("") || lastNameP.getText().equals("") ||
                userNameP.getText().equals("") ||
                emailP.getText().equals("") ||
                phoneP.getText().equals("") ||
                ageP.getText().equals("") ||
                gender.getValue().equals("")) {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            new Redirection().reDirect("/Viewxml/AdminDashboardPage.fxml","Admin Dashboard Page");
        }else {
            // getting the class reference by using the singleton pattern,
            ConnectionDb objDb = ConnectionDb.ConnectionDb();
            Connection c = objDb.getConnection();
            // Prepare the insert statement
            String insertQuery = "INSERT INTO patient (password,firstName,lastName,userName,email,phone,age,gender) VALUES (? , ?, ?,?, ?,?, ?,?)";
            PreparedStatement statement = c.prepareStatement(insertQuery);
            statement.setString(1, passwordP.getText());
            statement.setString(2, firstNameP.getText());
            statement.setString(3, lastNameP.getText());
            statement.setString(4,userNameP.getText());
            statement.setString(5,emailP.getText());
            statement.setString(6,phoneP.getText());
            statement.setInt(7, Integer.parseInt(ageP.getText()));
            statement.setString(8,gender.getValue());

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

            new Redirection().reDirect("/Viewxml/AdminDashboardPage.fxml","Admin Dashboard Page");
        }


    }
}
