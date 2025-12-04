package de.bs1bt.ams.mvc;

import de.bs1bt.ams.model.Raum;
import de.bs1bt.ams.repository.RaumRepository;
import de.bs1bt.ams.repository.RepositoryException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

public class RaumTabController {

    @FXML
    private Label lblFlaeche;
    @FXML
    private TableView<Raum> raumTableView;
    @FXML
    private TableColumn<Raum, Integer> columnRaumId;
    @FXML
    private TableColumn<Raum, String> columnRaumBezeichnung;
    @FXML
    private TableColumn<Raum, String> columnRaumGebaeude;
    @FXML
    private TableColumn<Raum, Double> columnRaumFlaeche;

    private RaumRepository raumRepository;

    /**
     * Wird vom MainController gesetzt.
     */
    public void setRaumRepository(RaumRepository raumRepository) {
        this.raumRepository = raumRepository;
        zeigeRaeumeInTabelle();
        zeigeGesamtflaeche();
    }

    private void zeigeRaeumeInTabelle() {
        if (raumRepository == null || raumTableView == null) {
            return;
        }

        raumTableView.getItems().clear();

        columnRaumId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnRaumBezeichnung.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
        columnRaumGebaeude.setCellValueFactory(new PropertyValueFactory<>("gebaeude"));
        columnRaumFlaeche.setCellValueFactory(new PropertyValueFactory<>("flaecheInQm"));

        try {
            ArrayList<Raum> liste = raumRepository.holeAlle();
            Iterator<Raum> iterator = liste.iterator();
            while (iterator.hasNext()) {
                Raum r = iterator.next();
                System.out.println(r);
                raumTableView.getItems().add(r);
            }
            raumTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (RepositoryException e) {
            zeigeDatenbankAlert("Die Räume können nicht aus der Datenbank ausgelesen werden.\n" + e.getMessage());
        }
    }

    private void zeigeGesamtflaeche() {
        if (raumRepository == null) {
            return;
        }
        try {
            double gesamtFlaeche = 0.0;
            ArrayList<Raum> liste = raumRepository.holeAlle();
            for (Raum r : liste) {
                gesamtFlaeche += r.getFlaecheInQm();
            }
            lblFlaeche.setText(gesamtFlaeche + "m²");
        } catch (RepositoryException e) {
            lblFlaeche.setText("Fehler");
        }
    }

    private void zeigeDatenbankAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle("Datenbank-Fehler");
        alert.setHeaderText("Datenbank-Fehler");
        alert.show();
    }

    private Raum zeigeRaumDialogView(String title, Raum raumModel) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("raum-dialog-view.fxml"));
            DialogPane raumDialogPane = fxmlLoader.load();

            RaumDialogController raumDialogController = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(raumDialogPane);
            dialog.setTitle(title);

            raumDialogController.setRaum(raumModel);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
                return raumDialogController.getRaum();
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

    // --------------------------------------------------------------
    // Button-Handler
    // --------------------------------------------------------------

    @FXML
    public void btnRaumAnlegenAction(ActionEvent actionEvent) {
        try {
            Raum neuerRaum = new Raum("Bezeichnung", "Gebäude");
            if (null != zeigeRaumDialogView("Raum anlegen", neuerRaum)) {
                raumRepository.erstelle(neuerRaum);
                zeigeRaeumeInTabelle();
                zeigeGesamtflaeche();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.setTitle("Fehler");
            alert.show();
        }
    }

    @FXML
    public void btnRaumBearbeitenAction(ActionEvent actionEvent) {
        Raum raumBearbeiten = raumTableView.getSelectionModel().getSelectedItem();

        if (null == raumBearbeiten) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie einen Raum aus der Liste aus.");
            alert.setHeaderText("Kein Raum selektiert");
            alert.show();
            return;
        }

        if (null != zeigeRaumDialogView("Raum bearbeiten", raumBearbeiten)) {
            try {
                raumRepository.aktualisiere(raumBearbeiten);
                zeigeRaeumeInTabelle();
                zeigeGesamtflaeche();
            } catch (RepositoryException e) {
                zeigeDatenbankAlert(e.getMessage());
            }
        }
    }

    @FXML
    public void btnRaumLoeschenAction(ActionEvent actionEvent) {
        Raum raumBearbeiten = raumTableView.getSelectionModel().getSelectedItem();

        if (null == raumBearbeiten) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie einen Raum aus der Liste aus.");
            alert.setHeaderText("Kein Raum selektiert");
            alert.show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Wollen Sie den Raum wirklich löschen?");
        alert.setHeaderText("Raum löschen");
        Optional<ButtonType> clickedButton = alert.showAndWait();
        if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
            try {
                raumRepository.loesche(raumBearbeiten);
                zeigeRaeumeInTabelle();
                zeigeGesamtflaeche();
            } catch (RepositoryException e) {
                zeigeDatenbankAlert(e.getMessage());
            }
        }
    }
}
