package de.bs1bt.ams.mvc;

import de.bs1bt.ams.model.Raum;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.Objects;

public class RaumDialog extends Dialog<Raum> {

    public RaumDialog() {//Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("raum-dialog-view.fxml"));
            loader.setController(this);

            DialogPane dialogPane = loader.load();
            //dialogPane.lookupButton(connectButtonType).addEventFilter(ActionEvent.ANY, this::onSave);

            //initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);

            setResizable(true);
            setTitle("Raum");
            setDialogPane(dialogPane);
            setResultConverter(buttonType -> {
                if(!Objects.equals(ButtonBar.ButtonData.OK_DONE, buttonType.getButtonData())) {
                    return null;
                }

                return new Raum();
            });


            //setOnShowing(dialogEvent -> Platform.runLater(() -> hostnameTextField.requestFocus()));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends Event> void onSave(T t) {
    }


    public void okAction(ActionEvent actionEvent) {
        System.out.println("OK pressed");
        //setResultConverter();
        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data =
                    dialogButton == null ? null : dialogButton.getButtonData();
            //String bez = txtBezeichnung.getText();
            //int laenge = Integer.parseInt(txtLaenge.getText());
            return data == ButtonBar.ButtonData.OK_DONE.OK_DONE
                    ? new Raum()
                    : null;
        });

    }

    public void abbrechenAction(ActionEvent actionEvent) {
        System.out.println("Abbrechen pressed");
    }

}
