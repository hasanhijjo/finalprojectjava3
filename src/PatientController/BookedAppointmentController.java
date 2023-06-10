/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PatientController;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Controller.Data;
import Controller.Redirection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sql.ConnectionDb;
import view.AdminDashboard;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class BookedAppointmentController implements Initializable {
    @FXML
    TableColumn<Data, String> date;
    @FXML
    TableColumn<Data, String> day;
    @FXML
    TableColumn<Data, String> time;
    @FXML
    TableColumn<Data, String> status;
    @FXML
    TableColumn<Data, String> comment;
    @FXML
    TableView<Data> tableView;
    static Object user_id;
    public void setId(Object id) {
        BookedAppointmentController.user_id = id;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        date.setCellValueFactory(new PropertyValueFactory<>("col1"));
        day.setCellValueFactory(new PropertyValueFactory<>("col2"));
        time.setCellValueFactory(new PropertyValueFactory<>("col3"));
        status.setCellValueFactory(new PropertyValueFactory<>("col4"));
        comment.setCellValueFactory(new PropertyValueFactory<>("col5"));
        try {
            ConnectionDb objDb = ConnectionDb.ConnectionDb();
            Connection c = objDb.getConnection();
            String query = "SELECT * FROM bookedappointment WHERE user_id = ?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setObject(1,user_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String appointment_id = resultSet.getString("appointment_id");

                getBookedAppointment(appointment_id);

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void getBookedAppointment(String id) throws SQLException, ClassNotFoundException {
        ConnectionDb objDb = ConnectionDb.ConnectionDb();
        Connection c = objDb.getConnection();
        String query = "SELECT * FROM appointment WHERE id = ?";
        PreparedStatement statement = c.prepareStatement(query);
        statement.setString(1,id);
        ResultSet resultSet = statement.executeQuery();

        // get appointment booked status , comment from its table
        String query2 = "SELECT * FROM bookedappointment WHERE appointment_id = ?";
        PreparedStatement statement2 = c.prepareStatement(query2);
        statement2.setString(1,id);
        ResultSet resultSet2 = statement2.executeQuery();

        if (resultSet.next()) {
            if(resultSet2.next()) {
                String date = resultSet.getString("date");
                String day = resultSet.getString("day");
                String time = resultSet.getString("time");

                String status = resultSet2.getString("status");
                String comment = resultSet2.getString("comment");

                Data data = new Data(date,day,time,status,comment);

                tableView.getItems().add(data);
            }

        }
    }

    public void logout(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new AdminDashboard().showPatient();
    }

    public void dashboard(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new Redirection().reDirect("/PatientPages/PatientDashboard.fxml","Patient Dashboard");
    }
    
}
