package de.bs1bt.ams.repository;

import de.bs1bt.ams.model.Geraet;

import java.util.ArrayList;
import java.util.List;

public class GeraetRAMRepository implements GeraetRepository {

    private final List<Geraet> geraete = new ArrayList<>();
    private int nextId = 1;

    @Override
    public Geraet hole(int id) throws RepositoryException {
        for (Geraet g : geraete) {
            if (g.getId() == id) {
                return g;
            }
        }
        throw new RepositoryException("Kein Geraet mit id=" + id + " im RAM-Repository gefunden.");
    }

    @Override
    public ArrayList<Geraet> holeAlle() throws RepositoryException {
        // Kopie zurückgeben, um externe Modifikationen der internen Liste zu vermeiden
        return new ArrayList<>(geraete);
    }

    @Override
    public int erstelle(Geraet geraetModel) throws RepositoryException {
        if (geraetModel == null) {
            throw new RepositoryException("Geraet darf nicht null sein.");
        }
        // einfache ID-Vergabe
        geraetModel.setId(nextId++);
        geraete.add(geraetModel);
        return geraetModel.getId();
    }

    @Override
    public void aktualisiere(Geraet geraetModel) throws RepositoryException {
        if (geraetModel == null) {
            throw new RepositoryException("Geraet darf nicht null sein.");
        }
        boolean gefunden = false;
        for (int i = 0; i < geraete.size(); i++) {
            if (geraete.get(i).getId() == geraetModel.getId()) {
                geraete.set(i, geraetModel);
                gefunden = true;
                break;
            }
        }
        if (!gefunden) {
            throw new RepositoryException("Zu aktualisierendes Geraet mit id=" +
                    geraetModel.getId() + " wurde im RAM-Repository nicht gefunden.");
        }
    }

    @Override
    public void loesche(int id) throws RepositoryException {
        boolean entfernt = geraete.removeIf(g -> g.getId() == id);
        if (!entfernt) {
            throw new RepositoryException("Zu löschendes Geraet mit id=" + id + " wurde im RAM-Repository nicht gefunden.");
        }
    }

    @Override
    public void loesche(Geraet geraetModel) throws RepositoryException {
        if (geraetModel == null) {
            throw new RepositoryException("Geraet darf nicht null sein.");
        }
        loesche(geraetModel.getId());
    }
}
