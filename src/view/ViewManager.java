/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author Yahya
 */
public class ViewManager {
   
    public static AdminDashboard adminPage;
    
    
    private ViewManager(){   
    }
    

    
    public static void openAdminPage(int num) throws IOException{
        if(num == 1) {
            if (adminPage == null) {
                adminPage = new AdminDashboard();
                adminPage.showPatient();
            } else {
                adminPage.showPatient();
            }
        }else {
            if (adminPage == null) {
                adminPage = new AdminDashboard();
                adminPage.showDoctor();
            } else {
                adminPage.showDoctor();
            }
        }

    }

}
