package de.bs1bt.ams.mvc;

import de.bs1bt.ams.repository.GeraetRepository;
import de.bs1bt.ams.repository.RaumRepository;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;

import java.io.IOException;

public class MainController {

    @FXML
    private Tab raumTab;      // aus main-view.fxml
    @FXML
    private Tab geraetTab;    // falls du den Geräte-Tab schon eingebunden hast

    private RaumRepository raumRepository;
    private GeraetRepository geraetRepository;

    private RaumTabController raumTabController;
    private GeraetTabController geraetTabController;

    // ---------------- Menü-Aktionen ----------------

    public void mnuUeberAMS(ActionEvent actionEvent) {
        Alert ueberAMS = new Alert(Alert.AlertType.INFORMATION);
        ueberAMS.setTitle("Über AMS");
        ueberAMS.setHeaderText("BS1 BT GmbH");
        ueberAMS.setContentText("Diese didaktische Software wird von den Schülerinnen und Schülern der Berufsschule 1 in Bayreuth entwickelt.");
        ueberAMS.show();
    }

    public void mnuSchliessen(ActionEvent actionEvent) {
        Platform.exit();
    }

    // ---------------- Dependency Injection ----------------

    public void setRaumRepository(RaumRepository raumRepository) {
        this.raumRepository = raumRepository;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("raum-tab-view.fxml"));
            Parent raumRoot = loader.load();

            raumTabController = loader.getController();
            raumTabController.setRaumRepository(raumRepository);

            raumTab.setContent(raumRoot);
        } catch (IOException e) {
            e.printStackTrace();
            zeigeDatenbankAlert("Räume-Tab konnte nicht geladen werden: " + e.getMessage());
        }
    }

    public void setGeraetRepository(GeraetRepository geraetRepository) {
        this.geraetRepository = geraetRepository;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("geraet-tab-view.fxml"));
            Parent geraetRoot = loader.load();

            geraetTabController = loader.getController();
            geraetTabController.setGeraetRepository(geraetRepository);

            geraetTab.setContent(geraetRoot);
        } catch (IOException e) {
            e.printStackTrace();
            zeigeDatenbankAlert("Geräte-Tab konnte nicht geladen werden: " + e.getMessage());
        }
    }

    // ---------------- Hilfsfunktion ----------------

    public void zeigeDatenbankAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle("Datenbank-Fehler");
        alert.setHeaderText("Datenbank-Fehler");
        alert.show();
    }
}
