package de.bs1bt.ams.mvc;

import de.bs1bt.ams.gateways.DataGatewayException;
import de.bs1bt.ams.gateways.RaumMySQLDataGateway;
import de.bs1bt.ams.model.Raum;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

public class MainController {
    @FXML
    private Label lblFlaeche;
    @FXML
    private TableView<Raum> raumTableView;
    @FXML
    private TableColumn columnRaumId;
    @FXML
    private TableColumn columnRaumBezeichnung;
    @FXML
    private TableColumn columnRaumGebaeude;
    @FXML
    private TableColumn columnRaumFlaeche;

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

    public void zeigeRaeumeInTabelle() {
        /*
         https://www.informatik-aktuell.de/entwicklung/programmiersprachen/mvvm-mit-javafx.html
         https://jenkov.com/tutorials/javafx/tableview.html
         */
        raumTableView.getItems().clear();

        columnRaumId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnRaumBezeichnung.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
        columnRaumGebaeude.setCellValueFactory(new PropertyValueFactory<>("gebaeude"));
        columnRaumFlaeche.setCellValueFactory(new PropertyValueFactory<>("flaecheInQm"));

        // Iterator Pattern
        try {
            // TODO Was bedeutet diese "feste Kopplung" für die Austauschbarkeit bei Verwendung einer anderen Datenbank?
            RaumMySQLDataGateway raumMySQLDataGateway = new RaumMySQLDataGateway();

            ObservableList<Raum> data = FXCollections.observableArrayList();
            ArrayList<Raum> liste = raumMySQLDataGateway.holeAlle();
            Iterator<Raum> iterator = liste.iterator();
            while (iterator.hasNext()) {
                Raum r = iterator.next();
                // TODO Testen Sie die Ausgabe der Raum-Objekte
                // System.out.println(r);
                raumTableView.getItems().add(r);
            }
            raumTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (DataGatewayException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Die Räume können nicht aus der Datenbank ausgelesen werden.");
            alert.show();
        }
    }

    void zeigeDatenbankAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle("Datenbank-Fehler");
        alert.setHeaderText("Datenbank-Fehler");
        alert.show();
    }

    public void zeigeGesamtflaeche() {
        RaumMySQLDataGateway raumMySQLDataGateway = new RaumMySQLDataGateway();
        // TODO Modellieren und implementieren Sie den Algorithmus zur Berechnung der Gesamtfläche. Nutzen Sie dazu das vorhandene raumDataGateway

    }

    public Raum zeigeRaumDialogView(String title, Raum raumModel) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation((getClass().getResource("raum-dialog-view.fxml")));
            DialogPane raumDialogPane = fxmlLoader.load();

            RaumDialogController raumDialogController = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(raumDialogPane);
            dialog.setTitle(title);

            raumDialogController.setRaum(raumModel);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if(clickedButton.get() == ButtonType.OK) {
                return raumDialogController.getRaum();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.setTitle("Fehler");
            alert.show();
        }
        return null;
    }

    public void btnRaumAnlegenAction(ActionEvent actionEvent) {
        try {
            Raum neuerRaum = new Raum("Bezeichnung", "Gebäude");
            if(null != zeigeRaumDialogView("Raum anlegen", neuerRaum)) {
                // TODO Was bedeutet diese "feste Kopplung" für die Austauschbarkeit bei Verwendung einer anderen Datenbank?
                RaumMySQLDataGateway raumMySQLDataGateway = new RaumMySQLDataGateway();
                raumMySQLDataGateway.erstelle(neuerRaum);
                zeigeRaeumeInTabelle();
                zeigeGesamtflaeche();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    public void btnRaumBearbeitenAction(ActionEvent actionEvent) {
        Raum raumBearbeiten = raumTableView.getSelectionModel().getSelectedItem();

        if(null == raumBearbeiten) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie einen Raum aus der Liste aus.");
            alert.setHeaderText("Kein Raum selektiert");
            alert.show();
            return;
        }

        if(null != zeigeRaumDialogView("Raum bearbeiten", raumBearbeiten)) {
            try {
                // TODO Was bedeutet diese "feste Kopplung für die Austauschbarkeit bei Verwendung einer anderen Datenbank?
                RaumMySQLDataGateway raumMySQLDataGateway = new RaumMySQLDataGateway();
                raumMySQLDataGateway.aktualisiere(raumBearbeiten);
                zeigeRaeumeInTabelle();
                zeigeGesamtflaeche();
            } catch (DataGatewayException e) {
                zeigeDatenbankAlert(e.getMessage());
            }
        }
    }

    public void btnRaumLoeschenAction(ActionEvent actionEvent) {
        Raum raumBearbeiten = raumTableView.getSelectionModel().getSelectedItem();

        if(null == raumBearbeiten) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie einen Raum aus der Liste aus.");
            alert.setHeaderText("Kein Raum selektiert");
            alert.show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Wollen Sie den Raum wirklich löschen?");
        alert.setHeaderText("Kein Raum selektiert");
        Optional<ButtonType> clickedButton = alert.showAndWait();
        if(clickedButton.get() == ButtonType.OK) {
            try {
                // TODO Was bedeutet diese "feste Kopplung für die Austauschbarkeit bei Verwendung einer anderen Datenbank?
                RaumMySQLDataGateway raumMySQLDataGateway = new RaumMySQLDataGateway();
                raumMySQLDataGateway.loesche(raumBearbeiten);
                zeigeRaeumeInTabelle();
                zeigeGesamtflaeche();
            } catch (DataGatewayException e) {
                zeigeDatenbankAlert(e.getMessage());
            }
        }
    }
}
