/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;


import helloapplication.HelloApplication;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import view.AdminDashboard;
import view.ViewManager;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class FirstPageController implements Initializable {
    HelloApplication h = new HelloApplication();
    @FXML
    private Button PatienteButton;
    @FXML
    private Button DoctorButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void PatienteButtonAction(ActionEvent event) throws IOException {
        ViewManager.openAdminPage(1);
       h.end(event);
    }

    @FXML
    private void DoctorButtonAction(ActionEvent event) throws IOException {
        ViewManager.openAdminPage(2);
        h.end(event);
    }

}
