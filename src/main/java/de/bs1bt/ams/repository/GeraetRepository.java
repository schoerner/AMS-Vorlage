package de.bs1bt.ams.repository;

import de.bs1bt.ams.model.Geraet;

import java.util.ArrayList;

public interface GeraetRepository {
    //public iRaumDAO(iDBConnection connection); /// TODO 2. Vorbedingung für Vertrag schließen, geht jedoch nur sinnvoll über Abstrakte Klasse

    /// TODO Problem: Welches SOLID-Prinzip wird hier verletzt? Interface-Segregation-Prinzip verletzt => Schlanke Interfaces
    //void erstelleTabelle() throws DAOException;
    //void loescheTabelle() throws DAOException;

    Geraet hole(int id) throws RepositoryException;
    ArrayList<Geraet> holeAlle() throws RepositoryException;
    int erstelle(Geraet geraetModel) throws RepositoryException;
    void aktualisiere(Geraet geraetModel) throws RepositoryException;
    void loesche(int id) throws RepositoryException;
    void loesche(Geraet geraetModel) throws RepositoryException;
}
