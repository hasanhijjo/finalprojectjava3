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
public class AppointmentsFreeController implements Initializable {
    ObservableList<Data> dataList = FXCollections.observableArrayList();

    public Object appointmentId = 0;
    @FXML
    TableView<Data> tableView;
    @FXML
    TableColumn<Data,String> id;
    @FXML
    TableColumn<Data,String> date;
    @FXML
    TableColumn<Data,String> day;
    @FXML
    TableColumn<Data,String> time;
    @FXML
    TableColumn<Data,String> status;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setOnMouseClicked(this::handleRowClick);
        // getting the class reference by using the singleton pattern,
        ConnectionDb objDb = null;

        id.setCellValueFactory(new PropertyValueFactory<>("col1"));
        date.setCellValueFactory(new PropertyValueFactory<>("col2"));
        day.setCellValueFactory(new PropertyValueFactory<>("col3"));
        time.setCellValueFactory(new PropertyValueFactory<>("col4"));
        status.setCellValueFactory(new PropertyValueFactory<>("col5"));


        try {
            objDb = ConnectionDb.ConnectionDb();
            Connection c = objDb.getConnection();
            String query = "SELECT * FROM appointment WHERE status = ?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1,"free");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String date = resultSet.getString("date");
                String day = resultSet.getString("day");
                String time = resultSet.getString("time");
                String status = resultSet.getString("status");
                Data data = new Data(id,date,day,time,status);

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
    public void dashboard(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new Redirection().reDirect("/Viewxml/AdminDashboardPage.fxml","Admin Dashboard Page");
    }
    public void newPatient(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new AdminDashboardPageController().newPatient(event);
    }

    public void bookedAppointment(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new AdminDashboardPageController().bookedAppointment(event);
    }

    public void newAppointment(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        new Redirection().reDirect("/Viewxml/NewAppointment.fxml","Add new Appointment");
    }
    public void updateAppointmentPage(ActionEvent event) throws IOException {
        if(appointmentId.equals(0)) {
            System.out.println("Nothing Selected");
        }else {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            new UpdateAppointmentController().setId(appointmentId);
            new Redirection().reDirect("/Viewxml/UpdateAppointment.fxml","Update Appointment");
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
            PreparedStatement statement = c.prepareStatement(query);
            statement.setObject(1, appointmentId);
            int rowAffected = statement.executeUpdate();
            if(rowAffected == 1) {
                System.out.println("DELETED");
                refreshPage();
            }
        }
    }
    public void refreshPage() throws SQLException, ClassNotFoundException {
        ConnectionDb objDb = ConnectionDb.ConnectionDb();
        Connection c = objDb.getConnection();
        String query = "SELECT * FROM appointment WHERE status = ?";
        PreparedStatement statement = c.prepareStatement(query);
        statement.setString(1,"free");
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
}
