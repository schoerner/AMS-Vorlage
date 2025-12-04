package de.bs1bt.ams.mvc;

import de.bs1bt.ams.model.Geraet;
import de.bs1bt.ams.repository.GeraetRepository;
import de.bs1bt.ams.repository.RepositoryException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class GeraetTabController {

    @FXML
    private TableView<Geraet> geraetTableView;
    @FXML
    private TableColumn<Geraet, Integer> columnGeraetId;
    @FXML
    private TableColumn<Geraet, String> columnGeraetBezeichnung;
    @FXML
    private TableColumn<Geraet, String> columnGeraetHersteller;
    @FXML
    private TableColumn<Geraet, String> columnGeraetModell;
    @FXML
    private TableColumn<Geraet, Boolean> columnGeraetDefekt;

    private GeraetRepository geraetRepository;

    /**
     * Wird vom MainController aufgerufen, sobald das Repository zur Verfügung steht.
     */
    public void setGeraetRepository(GeraetRepository geraetRepository) {
        this.geraetRepository = geraetRepository;
        zeigeGeraeteInTabelle();
    }

    private void zeigeGeraeteInTabelle() {
        if (geraetRepository == null || geraetTableView == null) {
            return;
        }

        geraetTableView.getItems().clear();

        columnGeraetId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnGeraetBezeichnung.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
        columnGeraetHersteller.setCellValueFactory(new PropertyValueFactory<>("hersteller"));
        columnGeraetModell.setCellValueFactory(new PropertyValueFactory<>("modell"));
        columnGeraetDefekt.setCellValueFactory(new PropertyValueFactory<>("defekt"));

        try {
            ArrayList<Geraet> liste = geraetRepository.holeAlle();
            // Test-Ausgabe
            for (Geraet geraet : liste) {
                System.out.println(geraet);
            }
            geraetTableView.getItems().addAll(liste);
            geraetTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (RepositoryException e) {
            zeigeDatenbankAlert("Die Geräte können nicht aus der Datenbank ausgelesen werden.\n" + e.getMessage());
        }
    }

    private void zeigeDatenbankAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle("Datenbank-Fehler");
        alert.setHeaderText("Datenbank-Fehler");
        alert.show();
    }

    private Geraet zeigeGeraetDialogView(String title, Geraet geraetModel) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("geraet-dialog-view.fxml"));
            DialogPane geraetDialogPane = fxmlLoader.load();

            GeraetDialogController geraetDialogController = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(geraetDialogPane);
            dialog.setTitle(title);

            geraetDialogController.setGeraet(geraetModel);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
                return geraetDialogController.getGeraet();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.setTitle("Fehler");
            alert.show();
        }
        return null;
    }

    // ----------------------------------------------------------------
    // Button-Handler
    // ----------------------------------------------------------------

    @FXML
    public void btnGeraetAnlegenAction(ActionEvent actionEvent) {
        try {
            Geraet neuesGeraet = new Geraet();
            neuesGeraet.setBezeichnung("Neues Gerät");
            Geraet result = zeigeGeraetDialogView("Gerät anlegen", neuesGeraet);
            if (result != null) {
                geraetRepository.erstelle(result);
                zeigeGeraeteInTabelle();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.setTitle("Fehler");
            alert.show();
        }
    }

    @FXML
    public void btnGeraetBearbeitenAction(ActionEvent actionEvent) {
        Geraet geraetBearbeiten = geraetTableView.getSelectionModel().getSelectedItem();

        if (geraetBearbeiten == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie ein Gerät aus der Liste aus.");
            alert.setHeaderText("Kein Gerät selektiert");
            alert.show();
            return;
        }

        if (zeigeGeraetDialogView("Gerät bearbeiten", geraetBearbeiten) != null) {
            try {
                geraetRepository.aktualisiere(geraetBearbeiten);
                zeigeGeraeteInTabelle();
            } catch (RepositoryException e) {
                zeigeDatenbankAlert(e.getMessage());
            }
        }
    }

    @FXML
    public void btnGeraetLoeschenAction(ActionEvent actionEvent) {
        Geraet geraetBearbeiten = geraetTableView.getSelectionModel().getSelectedItem();

        if (geraetBearbeiten == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie ein Gerät aus der Liste aus.");
            alert.setHeaderText("Kein Gerät selektiert");
            alert.show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Wollen Sie das Gerät wirklich löschen?");
        alert.setHeaderText("Gerät löschen");
        Optional<ButtonType> clickedButton = alert.showAndWait();
        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            try {
                geraetRepository.loesche(geraetBearbeiten);
                zeigeGeraeteInTabelle();
            } catch (RepositoryException e) {
                zeigeDatenbankAlert(e.getMessage());
            }
        }
    }
}
