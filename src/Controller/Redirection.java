package Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class Redirection extends Stage {

    public void reDirect(String url, String name) throws IOException {
        FXMLLoader page = new FXMLLoader(getClass().getResource(url));
        Parent pageRoot = page.load();
        Scene pageScene = new Scene(pageRoot);

        this.setScene(pageScene);
        this.setTitle(name);

        this.show();

    }
}
