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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
public class AdminDashboardPageController extends Stage implements Initializable {
    ObservableList<Data> dataList = FXCollections.observableArrayList();
    @FXML
    TextField firstnameS;
    @FXML
    TableColumn<Data, String> id;
    @FXML
    TableColumn<Data, String> firstname;
    @FXML
    TableColumn<Data, Integer> age;
    @FXML
    TableColumn<Data, String> gender;
    @FXML
    TableColumn<Data, String> phone;
    @FXML
    TableView<Data> tableView;
    public Object patientId = 0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setOnMouseClicked(this::handleRowClick);


        id.setCellValueFactory(new PropertyValueFactory<>("col1"));
        firstname.setCellValueFactory(new PropertyValueFactory<>("col2"));
        age.setCellValueFactory(new PropertyValueFactory<>("col3"));
        gender.setCellValueFactory(new PropertyValueFactory<>("col4"));
        phone.setCellValueFactory(new PropertyValueFactory<>("col5"));
;
        try {
            ConnectionDb objDb = ConnectionDb.ConnectionDb();
            Connection c = objDb.getConnection();
            String query = "SELECT * FROM patient";
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String first = resultSet.getString("firstName");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String phone = resultSet.getString("phone");
                Data data = new Data(id,first,age,gender,phone);

                tableView.getItems().add(data);

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }



    }    
    public void logout(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new AdminDashboard().showDoctor();
    }

    public void search(ActionEvent event) throws SQLException, ClassNotFoundException {
        ConnectionDb objDb = ConnectionDb.ConnectionDb();
        Connection c = objDb.getConnection();
        String query = "SELECT * FROM patient WHERE firstName = ?";
        PreparedStatement statement = c.prepareStatement(query);
        statement.setString(1,firstnameS.getText());
        ResultSet resultSet = statement.executeQuery();
        dataList.clear();

        if (resultSet.next()) {
            tableView.getItems().clear();
            String id = resultSet.getString("id");
            String first = resultSet.getString("firstName");
            int age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");
            String phone = resultSet.getString("phone");
            Data data = new Data(id,first,age,gender,phone);
            dataList.add(data);

            tableView.setItems(dataList);
        }else {
            System.out.println("Not Found");
        }
    }

    public void newPatient(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new Redirection().reDirect("/Viewxml/NewPatient.fxml","Add Patient Page");
    }
    public void freeAppointment(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new Redirection().reDirect("/Viewxml/AppointmentsFree.fxml","Free Appointment Page");
    }

    public void bookedAppointment(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new Redirection().reDirect("/Viewxml/AppointmentsBooked.fxml","Booked Appointment Page");
    }
    public void handleRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // Handle single click
            Data selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                patientId = selectedItem.getCol1();
            }
        }
    }
    public void deletePatient() throws SQLException, ClassNotFoundException {
        if(patientId.equals(0)) {
            System.out.println("Nothing Selected");
        }else {
            ConnectionDb con = ConnectionDb.ConnectionDb();
            Connection c = con.getConnection();
            String query = "DELETE FROM patient WHERE id = ?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setObject(1, patientId);
            int rowAffected = statement.executeUpdate();
            if(rowAffected == 1) {
                System.out.println("DELETED");
                refreshPage();
            }
        }

    }
    public void updatePatientPage(ActionEvent event) throws IOException {
        if (patientId.equals(0)) {
            System.out.println("Nothing Selected");
        }else {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            new UpdatePatientController().setId(patientId);
            new Redirection().reDirect("/Viewxml/UpdatePatient.fxml","Update Patient Page");
        }

    }
    public void refreshPage() throws SQLException, ClassNotFoundException {
        ConnectionDb objDb = ConnectionDb.ConnectionDb();
        Connection c = objDb.getConnection();
        String query = "SELECT * FROM patient";
        PreparedStatement statement = c.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        dataList.clear();

        while (resultSet.next()) {

            String id = resultSet.getString("id");
            String first = resultSet.getString("firstName");
            int age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");
            String phone = resultSet.getString("phone");
            Data data = new Data(id,first,age,gender,phone);
            dataList.add(data);

            tableView.setItems(dataList);
        }
    }
}
