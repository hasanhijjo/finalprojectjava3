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
public class AppointmentsBookedController implements Initializable {
    public Object appointmentId = 0;

    ObservableList<Data> dataList = FXCollections.observableArrayList();
    @FXML
    TableView<Data> tableView;
    @FXML
    TableColumn<Data,String> id;
    @FXML
    TableColumn<Data,String> name;
    @FXML
    TableColumn<Data,String> date;
    @FXML
    TableColumn<Data,String> time;
    @FXML
    TableColumn<Data,String> day;
    @FXML
    TextField firstnameS;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setOnMouseClicked(this::handleRowClick);

        id.setCellValueFactory(new PropertyValueFactory<>("col1"));
        name.setCellValueFactory(new PropertyValueFactory<>("col2"));
        date.setCellValueFactory(new PropertyValueFactory<>("col3"));
        time.setCellValueFactory(new PropertyValueFactory<>("col4"));
        day.setCellValueFactory(new PropertyValueFactory<>("col5"));
        try {
            ConnectionDb objDb = ConnectionDb.ConnectionDb();
            Connection c = objDb.getConnection();
            String query = "SELECT * FROM bookedappointment";
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String appointment_id = resultSet.getString("appointment_id");
                String query2 = "SELECT * FROM appointment WHERE id = ?";
                PreparedStatement statement2 = c.prepareStatement(query2);
                statement2.setString(1,appointment_id);
                ResultSet appointmentData = statement2.executeQuery();
                getAppointmentData(appointmentData);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void getAppointmentData(ResultSet r) throws SQLException {
        while (r.next()) {
            String id = r.getString("id");
            String date = r.getString("date");
            String name = r.getString("nameP");
            String time = r.getString("time");
            String day = r.getString("day");
            Data data = new Data(id,name,date,time,day);

            tableView.getItems().add(data);

        }
    }
    public void logout(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new AdminDashboard().showDoctor();
    }
    public void dashboard(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new Redirection().reDirect("/Viewxml/AdminDashboardPage.fxml","Admin Dashboard Page");
    }
    public void newPatient(ActionEvent event) throws IOException {
        new AdminDashboardPageController().newPatient(event);
    }
    public void freeAppointment(ActionEvent event) throws IOException {
        new AdminDashboardPageController().freeAppointment(event);
    }

    public void search() throws SQLException, ClassNotFoundException {
        ConnectionDb objDb = ConnectionDb.ConnectionDb();
        Connection c = objDb.getConnection();
        String query = "SELECT * FROM appointment WHERE nameP = ? && status = ?";
        PreparedStatement statement = c.prepareStatement(query);
        statement.setString(1,firstnameS.getText());
        statement.setString(2,"booked");
        ResultSet resultSet = statement.executeQuery();
        dataList.clear();

        if (resultSet.next()) {
            tableView.getItems().clear();
            String id = resultSet.getString("id");
            String name = resultSet.getString("nameP");
            Date date = resultSet.getDate("date");
            String day = resultSet.getString("day");
            String time = resultSet.getString("time");
            Data data = new Data(id,name,date,day,time);
            dataList.add(data);

            tableView.setItems(dataList);
        }else {
            System.out.println("Not Found");
        }
    }
    public void updateAppointmentPage(ActionEvent event) throws IOException {
        if(appointmentId.equals(0)) {
            System.out.println("Nothing Selected");
        }else {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            new UpdateBookedController().setId(appointmentId);
            new Redirection().reDirect("/Viewxml/UpdateBookedAppointment.fxml","Update Booked Appointment");
        }

    }
    public void handleRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // Handle single click
            Data selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                appointmentId = selectedItem.getCol1();
            }
        }
    }
    public void deleteAppointment(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(appointmentId.equals(0)) {
            System.out.println("Nothing Selected");
        }else {
            ConnectionDb con = ConnectionDb.ConnectionDb();
            Connection c = con.getConnection();
            String query = "DELETE FROM appointment WHERE id = ?";
            String query2 = "DELETE FROM bookedappointment WHERE appointment_id = ?";
            PreparedStatement statement = c.prepareStatement(query);
            PreparedStatement statement2 = c.prepareStatement(query2);
            statement.setObject(1, appointmentId);
            statement2.setObject(1,appointmentId);
            int rowAffected = statement.executeUpdate();
            int rowAffected2 = statement2.executeUpdate();
            if(rowAffected == 1 &&  rowAffected2 == 1) {
                System.out.println("DELETED");
                refreshPage();
            }
        }
    }
    public void refreshPage() throws SQLException, ClassNotFoundException {

        dataList.clear();
        ConnectionDb objDb = ConnectionDb.ConnectionDb();
        Connection c = objDb.getConnection();
        String query = "SELECT * FROM bookedappointment";
        PreparedStatement statement = c.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        tableView.getItems().clear();
        while (resultSet.next()) {

            String appointment_id = resultSet.getString("appointment_id");
            String query2 = "SELECT * FROM appointment WHERE id = ?";
            PreparedStatement statement2 = c.prepareStatement(query2);
            statement2.setString(1,appointment_id);
            ResultSet appointmentData = statement2.executeQuery();

            while (appointmentData.next()) {

                String id = appointmentData.getString("id");
                String date = appointmentData.getString("date");
                String name = appointmentData.getString("nameP");
                String time = appointmentData.getString("time");
                String day = appointmentData.getString("day");
                Data data = new Data(id,name,date,time,day);
                dataList.add(data);

                tableView.setItems(dataList);
            }
        }


    }

}
