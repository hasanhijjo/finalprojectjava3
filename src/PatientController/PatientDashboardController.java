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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sql.ConnectionDb;
import view.AdminDashboard;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class PatientDashboardController implements Initializable {
    ObservableList<Data> dataList = FXCollections.observableArrayList();
    @FXML
    TableColumn<Data, String> id;
    @FXML
    TableColumn<Data, String> date;
    @FXML
    TableColumn<Data, String> day;
    @FXML
    TableColumn<Data, String> time;
    @FXML
    TableColumn<Data, String> status;
    @FXML
    TableView<Data> tableView;
    // for getting the appointment data
    static Object appointmentId = 0;
    // for getting the name of the user
    static Object patientId = 0;
    // for preventing book the booked appointment
    String statusValue = "";
    public void setId(Object id) {
        PatientDashboardController.patientId = id;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tableView.setOnMouseClicked(this::handleRowClick);

        id.setCellValueFactory(new PropertyValueFactory<>("col1"));
        date.setCellValueFactory(new PropertyValueFactory<>("col2"));
        day.setCellValueFactory(new PropertyValueFactory<>("col3"));
        time.setCellValueFactory(new PropertyValueFactory<>("col4"));
        status.setCellValueFactory(new PropertyValueFactory<>("col5"));
        try {
            ConnectionDb objDb = ConnectionDb.ConnectionDb();
            Connection c = objDb.getConnection();
            String query = "SELECT * FROM appointment";
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String date = resultSet.getString("date");
                String day = resultSet.getString("day");
                String time = resultSet.getString("time");
                String status = resultSet.getString("status");
                Data data = new Data(id,date,day,time,status);
                statusValue = status;
                tableView.getItems().add(data);

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void BookAppointment() throws SQLException, ClassNotFoundException {
        if(appointmentId.equals(0)) {
            System.out.println("Nothing Selected");
        }else {
            if(!statusValue.equals("booked")) {
                ConnectionDb objDb = ConnectionDb.ConnectionDb();
                Connection c = objDb.getConnection();
                String query = "SELECT * FROM patient WHERE id = ?";
                PreparedStatement statement = c.prepareStatement(query);
                statement.setObject(1,patientId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String name = resultSet.getString("firstName");
                    String query2 = "UPDATE appointment SET nameP = ? , status = ? WHERE id = ?";
                    PreparedStatement statement2 = c.prepareStatement(query2);
                    statement2.setString(1,name);
                    statement2.setString(2,"booked");
                    statement2.setObject(3,appointmentId);
                    int rowAffected = statement2.executeUpdate();
                    if(rowAffected == 1) {
                        moveAppointmentToBooked();
                        refreshPage();
                    }
                }
            }else {
                System.out.println("Sorry this is a booked appointment !");
            }
        }
    }

    public void BookedAppointmentPage(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new BookedAppointmentController().setId(patientId);
        new Redirection().reDirect("/PatientPages/BookedAppointment.fxml","Booked Appointment Page");
    }
    public void handleRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // Handle single click
            Data selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                appointmentId = selectedItem.getCol1();
            }
        }
    }

    public void moveAppointmentToBooked() throws SQLException, ClassNotFoundException {
        ConnectionDb con = ConnectionDb.ConnectionDb();
        Connection c = con.getConnection();
        // inserting the booked appointment in its table after updating the value of it
        String query3 = "INSERT INTO bookedappointment (appointment_id , user_id , status , comment) VALUES (? , ? , ? , ?)";
        PreparedStatement statement3 = c.prepareStatement(query3);
        statement3.setObject(1,appointmentId);
        statement3.setObject(2,patientId);
        statement3.setString(3,"Waiting");
        statement3.setString(4,"");
        int rowAffected = statement3.executeUpdate();
        if(rowAffected == 1) {
            System.out.println("Booked Successfully ");
        }
    }


    public void refreshPage() throws SQLException, ClassNotFoundException {
        ConnectionDb objDb = ConnectionDb.ConnectionDb();
        Connection c = objDb.getConnection();
        String query = "SELECT * FROM appointment";
        PreparedStatement statement = c.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        dataList.clear();
        tableView.getItems().clear();
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String date = resultSet.getString("date");
            String day = resultSet.getString("day");
            String time = resultSet.getString("time");
            String status = resultSet.getString("status");
            Data data = new Data(id,date,day,time,status);
            dataList.add(data);

            tableView.setItems(dataList);
        }
    }
    public void logout(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new AdminDashboard().showPatient();
    }
}
