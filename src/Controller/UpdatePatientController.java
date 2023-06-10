package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sql.ConnectionDb;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdatePatientController implements Initializable {

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

    static Object id = 0;

    public void setId(Object id) {
        UpdatePatientController.id = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gender.getItems().addAll("Male","Female");
        try {
            ConnectionDb con = ConnectionDb.ConnectionDb();
            Connection c = con.getConnection();
            String query = "SELECT * FROM patient WHERE id = ?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                String firstname = resultSet.getString("firstName");
                String lastname = resultSet.getString("lastName");
                String username = resultSet.getString("userName");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String age = resultSet.getString("age");
                String phone = resultSet.getString("phone");
                String genderV = resultSet.getString("gender");

                firstNameP.setText(firstname);
                lastNameP.setText(lastname);
                userNameP.setText(username);
                passwordP.setText(password);
                emailP.setText(email);
                ageP.setText(age);
                phoneP.setText(phone);
                gender.setValue(genderV);

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updatePatient(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {


            // getting the class reference by using the singleton pattern,
            ConnectionDb objDb = ConnectionDb.ConnectionDb();
            Connection c = objDb.getConnection();
            // Prepare the insert statement
            String updateQuery = "UPDATE patient SET password = ? , firstName = ? , lastName = ? , userName = ? , email = ? , phone = ? , age = ? , gender = ? WHERE id = ?";
            PreparedStatement statement = c.prepareStatement(updateQuery);
            statement.setString(1, passwordP.getText());
            statement.setString(2, firstNameP.getText());
            statement.setString(3, lastNameP.getText());
            statement.setString(4,userNameP.getText());
            statement.setString(5,emailP.getText());
            statement.setString(6,phoneP.getText());
            statement.setInt(7, Integer.parseInt(ageP.getText()));
            statement.setString(8,gender.getValue());
            statement.setObject(9,id);

            // Execute the insert statement
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected == 1) {
                System.out.println("Updated Successfully");
            }
            // Close the database connection
            statement.close();
            c.close();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            new Redirection().reDirect("/Viewxml/AdminDashboardPage.fxml","Admin Dashboard Page");
        }


}

