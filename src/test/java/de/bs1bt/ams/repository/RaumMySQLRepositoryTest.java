package de.bs1bt.ams.repository;

import de.bs1bt.ams.model.Raum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class RaumMySQLRepositoryTest {

    @BeforeAll
    static void beforeAll() {
        RaumMySQLRepository rg = new RaumMySQLRepository();
        try {
            rg.loescheTabelle();
        } catch (DataGatewayException e) {
            // in Ordnung, falls Tabelle noch nicht existiert
        }
    }

    @Test
    void testKomplett() {
        RaumMySQLRepository rg = new RaumMySQLRepository();
        try {
            rg.erstelleTabelle();

            int id = rg.erstelle(new Raum("U1", "IT-C"));
            id = rg.erstelle(new Raum("U2", "IT-C"));

            ArrayList<Raum> raueme = rg.holeAlle();
            assertEquals(2, raueme.size());

            assertEquals("U1", raueme.get(0).getBezeichnung());
            assertEquals("IT-C", raueme.get(0).getGebaeude());
            assertEquals("U2", raueme.get(1).getBezeichnung());
            assertEquals("IT-C", raueme.get(1).getGebaeude());

            // hole zuletzt erstellen Datensatz
            Raum raum = rg.hole(id);
            assertEquals("U2", raum.getBezeichnung());
            assertEquals("IT-C", raum.getGebaeude());


            // Erstelle Raum zum Aktualisieren und späteren Löschen
            Raum raumNeu = new Raum("121", "Hauptgebäude");
            id = rg.erstelle(raumNeu);
            // Test über Auslesen
            Raum raumVergleich = rg.hole(id);
            assertEquals("121", raumVergleich.getBezeichnung());
            assertEquals("Hauptgebäude", raumVergleich.getGebaeude());
            assertEquals(3, rg.holeAlle().size());

            // aktualisiere Raum
            raumNeu.setBezeichnung("123");
            rg.aktualisiere(raumNeu);
            raumVergleich = rg.hole(id);
            assertEquals("123", raumVergleich.getBezeichnung());

            rg.loesche(raumVergleich);

            assertEquals(2, rg.holeAlle().size());

            rg.erstelle(new Raum("U3", "IT-C"));
            rg.erstelle(new Raum("U4", "IT-C"));
            rg.erstelle(new Raum("U5", "IT-C"));
            rg.erstelle(new Raum("U6", "IT-C"));
            rg.erstelle(new Raum("U7", "IT-C"));

            assertEquals(7, rg.holeAlle().size());
        } catch (DataGatewayException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
