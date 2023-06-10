package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sql.ConnectionDb;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateBookedController implements Initializable {


    @FXML
    DatePicker dateE;
    @FXML
    TextField dayE;
    @FXML
    TextField timeE;
    @FXML
    ComboBox<String> status;
    @FXML
    TextField comment;



    static Object id = 0;

    public void setId(Object id) {
        UpdateBookedController.id = id;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            status.getItems().addAll("Waiting","Finished");

            ConnectionDb con = ConnectionDb.ConnectionDb();
            Connection c = con.getConnection();
            String query = "SELECT * FROM appointment WHERE id = ?";
            String query2 = "SELECT * FROM bookedappointment WHERE appointment_id = ?";

            PreparedStatement statement = c.prepareStatement(query);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            PreparedStatement statement2 = c.prepareStatement(query2);
            statement2.setObject(1, id);
            ResultSet resultSet2 = statement2.executeQuery();


            if (resultSet.next()){

                String date = resultSet.getString("date");
                String day = resultSet.getString("day");
                String time = resultSet.getString("time");

                dateE.setValue(LocalDate.parse(date));
                dayE.setText(day);
                timeE.setText(time);

            }

            if (resultSet2.next()){

                String status = resultSet2.getString("status");
                String comment = resultSet2.getString("comment");

                this.status.setValue(status);
                this.comment.setText(comment);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void updateBookedAppointment(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        ConnectionDb con = ConnectionDb.ConnectionDb();
        Connection c = con.getConnection();
        String query = "UPDATE appointment SET date = ? , day = ? , time = ? WHERE id = ?";
        String query2 = "UPDATE bookedappointment SET status = ? , comment = ? WHERE appointment_id = ?";

        PreparedStatement statement = c.prepareStatement(query);
        statement.setDate(1, Date.valueOf(dateE.getValue()));
        statement.setString(2,dayE.getText());
        statement.setString(3,timeE.getText());
        statement.setObject(4,id);

        PreparedStatement statement2 = c.prepareStatement(query2);
        statement2.setString(1,status.getValue());
        statement2.setString(2,comment.getText());
        statement2.setObject(3,id);

        int rowAffected = statement.executeUpdate();
        int rowAffected2 = statement2.executeUpdate();

        if(rowAffected == 1 && rowAffected2 == 1) {
            System.out.println("Update Successfully");
        }
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        new Redirection().reDirect("/Viewxml/AppointmentsBooked.fxml","Booked Appointment Page");
    }

}
