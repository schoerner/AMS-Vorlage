package de.bs1bt.ams.mvc;

import de.bs1bt.ams.model.Raum;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RaumDialogController extends Dialog<Raum> {
    private Raum raumModel;

    @FXML
    private TextField txtBezeichnung;
    @FXML
    private TextField txtGebaeude;
    @FXML
    private TextField txtLaenge;
    @FXML
    private TextField txtBreite;
    @FXML
    private Label lblFlaeche;


    public void setRaum(Raum raumModel) {
        this.raumModel = raumModel;
        txtBezeichnung.setText(raumModel.getBezeichnung());
        txtGebaeude.setText(raumModel.getGebaeude());
        txtLaenge.setText(String.valueOf(raumModel.getLaengeInCm()));
        txtBreite.setText(String.valueOf(raumModel.getBreiteInCm()));
        lblFlaeche.setText(String.valueOf(raumModel.getFlaecheInQm()) + " m²");
    }

    public Raum getRaum() throws Exception {
        raumModel.setBezeichnung(txtBezeichnung.getText());
        raumModel.setGebaeude(txtGebaeude.getText());
        raumModel.setBreiteInCm(Double.parseDouble(txtBreite.getText()));
        raumModel.setLaengeInCm(Double.parseDouble(txtLaenge.getText()));
        return raumModel;
    }

}
