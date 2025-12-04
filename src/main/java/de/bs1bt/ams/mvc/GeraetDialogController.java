package de.bs1bt.ams.mvc;

import de.bs1bt.ams.model.Geraet;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class GeraetDialogController extends Dialog<Geraet> {

    private Geraet geraetModel;

    @FXML
    private TextField txtBezeichnung;
    @FXML
    private TextField txtHersteller;
    @FXML
    private TextField txtModell;
    @FXML
    private DatePicker dpKaufdatum;
    @FXML
    private TextField txtGarantieMonate;
    @FXML
    private CheckBox chkDefekt;

    @FXML
    private Label lblRestGarantie;


    /**
     * Lädt ein bestehendes Modell in die GUI.
     */
    public void setGeraet(Geraet geraetModel) {
        this.geraetModel = geraetModel;

        txtBezeichnung.setText(geraetModel.getBezeichnung());
        txtHersteller.setText(geraetModel.getHersteller());
        txtModell.setText(geraetModel.getModell());

        if (geraetModel.getKaufDatum() != null) {
            dpKaufdatum.setValue(geraetModel.getKaufDatum());
        }

        txtGarantieMonate.setText(String.valueOf(geraetModel.getGarantieInMonaten()));
        chkDefekt.setSelected(geraetModel.isDefekt());

        // Restgarantie anzeigen (falls Datum vorhanden ist)
        try {
            lblRestGarantie.setText(String.valueOf(geraetModel.restGarantieInMonaten()));
        } catch (Exception e) {
            lblRestGarantie.setText("-");
        }
    }


    /**
     * Liest die Eingaben aus und schreibt sie ins Modell zurück.
     */
    public Geraet getGeraet() throws Exception {

        // Pflichtfeld Bezeichnung
        geraetModel.setBezeichnung(txtBezeichnung.getText());

        geraetModel.setHersteller(txtHersteller.getText());
        geraetModel.setModell(txtModell.getText());

        // Kaufdatum (kann null sein)
        LocalDate kaufdatum = dpKaufdatum.getValue();
        if (kaufdatum != null) {
            geraetModel.setKaufDatum(kaufdatum);
        }

        // Garantie (mit Validierung aus der Modellklasse)
        try {
            int garantie = Integer.parseInt(txtGarantieMonate.getText());
            geraetModel.setGarantieInMonaten(garantie);
        } catch (NumberFormatException e) {
            throw new Exception("Die Garantie muss eine ganze Zahl sein.");
        }

        geraetModel.setDefekt(chkDefekt.isSelected());

        return geraetModel;
    }
}
