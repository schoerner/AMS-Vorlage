package de.bs1bt.ams;

import de.bs1bt.ams.config.Config;
import de.bs1bt.ams.mvc.MainController;
import de.bs1bt.ams.repository.*;
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
        RaumRepository raumRepository = null;
        GeraetRepository geraetRepository = null;

        /* Hier soll abhängig von der Plattform entschieden werden,
           welche Repository-Implementierung verwendet wird.
           Die Plattform wird aus der config.properties-Datei im Verzeichnis resources gelesen.
         */
        String plattform = Config.get("plattform");
        if("DEV".equals(plattform)) {
            raumRepository = new RaumRAMRepository();
            geraetRepository = new GeraetRAMRepository();
            //...
        } else if ("PROD".equals(plattform)) {
            raumRepository = new RaumMySQLRepository();
            geraetRepository = new GeraetRAMRepository(); // Copy-and-Paste-Fehler? Müsste hier nicht GeraetMySQLRepository instanziiert werden?
            // ...
        }

        mc.setRaumRepository( raumRepository );
        mc.setGeraetRepository( geraetRepository );

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}