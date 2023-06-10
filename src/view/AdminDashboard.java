/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;

import Controller.Redirection;
import javafx.stage.Stage;

/**
 *
 * @author Yahya
 */
public class AdminDashboard extends Stage{




    public void showPatient() throws IOException {
        new Redirection().reDirect("/Viewxml/PatientLoginPage.fxml", "Patient Login Page");
    }

    public void showDoctor() throws IOException {
        new Redirection().reDirect("/Viewxml/DoctorLoginPage.fxml", "Doctor Login Page");
    }
    
}
