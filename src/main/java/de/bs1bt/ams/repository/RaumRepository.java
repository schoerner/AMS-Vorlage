package de.bs1bt.ams.repository;

import de.bs1bt.ams.model.Raum;

import java.util.ArrayList;

public interface RaumRepository {
    //public iRaumDAO(iDBConnection connection); /// TODO 2. Vorbedingung für Vertrag schließen, geht jedoch nur sinnvoll über Abstrakte Klasse

    /// TODO Problem: Welches SOLID-Prinzip wird hier verletzt? Interface-Segregation-Prinzip verletzt => Schlanke Interfaces
//    public void erstelleTabelle() throws DAOException;
//    public void loescheTabelle() throws DAOException;

    Raum hole(int id) throws RepositoryException;
    ArrayList<Raum> holeAlle() throws RepositoryException;
    int erstelle(Raum raumModel) throws RepositoryException;
    void aktualisiere(Raum raumModel) throws RepositoryException;
    void loesche(int id) throws RepositoryException;
    void loesche(Raum raumModel) throws RepositoryException;
}
