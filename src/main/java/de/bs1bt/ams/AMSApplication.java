package de.bs1bt.ams;

import de.bs1bt.ams.mvc.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AMSApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AMSApplication.class.getResource("mvc/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("BS1 BT - Asset Management System");
        stage.setScene(scene);

        MainController mc = fxmlLoader.getController();
        mc.zeigeRaeumeInTabelle();
        mc.zeigeGesamtflaeche();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}