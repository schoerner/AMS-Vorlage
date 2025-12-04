package de.bs1bt.ams.repository;

import de.bs1bt.ams.model.Raum;

import java.util.ArrayList;

public class RaumRAMRepository implements RaumRepository {
    // todo static
    private static int lastID;

    private ArrayList<Raum> liste;

    public RaumRAMRepository() {
        liste = new ArrayList<Raum>();
    }

    public void erstelleTabelle() throws RepositoryException {
        loescheTabelle();
    }

    public void loescheTabelle() throws RepositoryException {
        liste.clear();
        lastID = 0;
    }

    @Override
    public Raum hole(int id) throws RepositoryException {
        for (Raum r: liste) {
            if(r.getId() == id) {
                return r;
            }
        }
        throw new RepositoryException("Der Raum mit der id {" + id + "} konnte nicht gefunden werden.");
    }

    @Override
    public ArrayList<Raum> holeAlle() throws RepositoryException {
        return liste;
    }

    @Override
    public int erstelle(Raum raumModel) throws RepositoryException {
        liste.add(raumModel);
        lastID++;
        raumModel.setId(lastID);
        return lastID;
    }

    @Override
    public void aktualisiere(Raum raumModel) throws RepositoryException {
        for (int i=0; i<liste.size(); i++) {
            if(liste.get(i).getId() == raumModel.getId()) {
                liste.set(i, raumModel);
            }
        }
        throw new RepositoryException("Der Raum mit der id {" + raumModel.getId() + "} konnte nicht gefunden werden.");
    }

    @Override
    public void loesche(int id) throws RepositoryException {
        Raum r = hole(id);
        loesche(r);
    }

    @Override
    public void loesche(Raum raumModel) throws RepositoryException {
        liste.remove(raumModel);
    }
}
