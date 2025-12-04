package de.bs1bt.ams.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Geraet {
    private int id;
    private String bezeichnung;
    private boolean defekt;
    private String hersteller;
    private String modell;
    private LocalDate kaufDatum;
    private int garantieInMonaten;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public boolean isDefekt() {
        return defekt;
    }

    public void setDefekt(boolean defekt) {
        this.defekt = defekt;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public LocalDate getKaufDatum() {
        return kaufDatum;
    }

    // TODO Überladen von Methoden
    public void setKaufDatum(LocalDate kaufDatum) {
        this.kaufDatum = kaufDatum;
    }
    public void setKaufDatum(String date) {
        kaufDatum = Date.valueOf(date).toLocalDate();
    }

    public int getGarantieInMonaten() {
        return garantieInMonaten;
    }
    // TODO Inkonsistenten Zustand vermeiden:
    public void setGarantieInMonaten(int garantieInMonaten) throws Exception {
        if(garantieInMonaten < 0) {
            throw new Exception("Die Garantie in Monaten muss mindestens 0 betragen.");
        }
        this.garantieInMonaten = garantieInMonaten;
    }

    public long restGarantieInMonaten() {
        LocalDate heute = LocalDate.now();
        long monate = garantieInMonaten - ChronoUnit.MONTHS.between(kaufDatum,heute);
        return monate;
    }

    public boolean hatGarantie() {
        if(restGarantieInMonaten() > 0) {
            return true;
        } else if (restGarantieInMonaten() == 0 && LocalDate.now().getDayOfMonth()<=kaufDatum.getDayOfMonth()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Geraet{" +
                "bezeichnung='" + bezeichnung + '\'' +
                ", defekt=" + defekt +
                ", hersteller='" + hersteller + '\'' +
                ", modell='" + modell + '\'' +
                ", kaufDatum=" + kaufDatum +
                ", garantieInMonaten=" + garantieInMonaten +
                '}';
    }
}
