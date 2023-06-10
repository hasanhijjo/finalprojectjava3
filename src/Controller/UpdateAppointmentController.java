package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sql.ConnectionDb;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable {

    @FXML
    DatePicker dateE;
    @FXML
    TextField dayE;
    @FXML
    TextField timeE;
    @FXML
    TextField statusE;
    // user first name
    String name = "";
    // for user id
    static Object id = 0;

    public void setId(Object id) {
        UpdateAppointmentController.id = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ConnectionDb con = ConnectionDb.ConnectionDb();
            Connection c = con.getConnection();
            String query = "SELECT * FROM appointment WHERE id = ?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                LocalDate date = resultSet.getDate("date").toLocalDate();
                String day = resultSet.getString("day");
                String time = resultSet.getString("time");
                String status = resultSet.getString("status");
                name = resultSet.getString("nameP");
                dateE.setValue(date);
                dayE.setText(day);
                timeE.setText(time);
                statusE.setText(status);

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void updateAppointment(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        ConnectionDb con = ConnectionDb.ConnectionDb();
        Connection c = con.getConnection();
        String query = "update appointment SET date = ? , day = ? , time = ? , status = ? WHERE id = ?";
        PreparedStatement statement = c.prepareStatement(query);
        statement.setDate(1, Date.valueOf(dateE.getValue()));
        statement.setString(2,dayE.getText());
        statement.setString(3,timeE.getText());
        statement.setString(4,statusE.getText());
        if(statusE.getText().equals("booked")) {
            int user_id = 0;
            // getting user id that booked the appointment
            String query2 = "SELECT * FROM patient WHERE firstName = ?";
            PreparedStatement statement2 = c.prepareStatement(query2);
            statement2.setString(1,name);
            ResultSet resultSet = statement2.executeQuery();
            if(resultSet.next()) {
                user_id = resultSet.getInt("id");
            }

            // inserting the booked appointment in its table after updating the value of it
            String query3 = "INSERT INTO bookedappointment (appointment_id , user_id , status , comment) VALUES (? , ? , ? , ?)";
            PreparedStatement statement3 = c.prepareStatement(query3);
            statement3.setObject(1,id);
            statement3.setInt(2,user_id);
            statement3.setString(3,"Waiting");
            statement3.setString(4,"");
            statement3.executeUpdate();

        }
        statement.setObject(5,id);
        int rowAffected = statement.executeUpdate();

        if(rowAffected == 1) {
            System.out.println("Update Successfully");
        }
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        new Redirection().reDirect("/Viewxml/AppointmentsFree.fxml","Free Appointment Page");
    }

}
