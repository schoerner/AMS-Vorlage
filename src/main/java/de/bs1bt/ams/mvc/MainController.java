package de.bs1bt.ams.mvc;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class MainView {
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

    public void btnRaumAnlegenAction(ActionEvent actionEvent) {
        Alert ueberAMS = new Alert(Alert.AlertType.ERROR);
        ueberAMS.setTitle("To Do");
        ueberAMS.setHeaderText("Noch nicht implementiert");
        ueberAMS.show();
    }

    public void btnRaumBearbeitenAction(ActionEvent actionEvent) {
        Alert ueberAMS = new Alert(Alert.AlertType.ERROR);
        ueberAMS.setTitle("To Do");
        ueberAMS.setHeaderText("Noch nicht implementiert");
        ueberAMS.show();
    }

    public void btnRaumLoeschenAction(ActionEvent actionEvent) {
        Alert ueberAMS = new Alert(Alert.AlertType.ERROR);
        ueberAMS.setTitle("To Do");
        ueberAMS.setHeaderText("Noch nicht implementiert");
        ueberAMS.show();
    }
}
