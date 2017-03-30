package cwoapp.nl.cwoapp.utility;

import java.util.ArrayList;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.CursistBehaaldEis;
import cwoapp.nl.cwoapp.entity.Diploma;
import cwoapp.nl.cwoapp.entity.DiplomaEis;

/**
 * Created by sonja on 3/14/2017.
 */

// TODO list
// TODO Info knop activeren bij diplomaEisen
// TODO Diploma's uitgeven
// TODO foto's maken en opslaan toevoegen


public class MockEntityGenerator {

    public static List<Cursist> createCursistList(int aantal) {
        List<Cursist> cursistList = new ArrayList<>();
        for (int i = 0; i < aantal; i++) {
            cursistList.add(createFullCursist(i));
        }
        return cursistList;
    }

    public static Cursist createSimpleCursist(int id) {
        return new Cursist((long) id, "Sonja" + id, "", "Duijvesteijn", "opmerking iets over koud water en wind en zon.", "foto", null);
    }

    public static Cursist createFullCursist(int id) {
        Cursist cursist = createSimpleCursist(id);
        List<CursistBehaaldEis> cbe = createCursistBehaaldEisenLijst(3);
        cursist.setCursistBehaaldEis(cbe);
        return cursist;
    }

    public static List<Diploma> createDiplomaList(int aantal) {
        List<Diploma> diplomaList = new ArrayList<>();
        for (int i = 0; i < aantal; i++) {
            diplomaList.add(createDiploma(i));
        }
        return diplomaList;
    }

    public static Diploma createDiploma(int id) {
        List<DiplomaEis> diplomaEis = createCwoEisenList(10);
        Diploma diploma = new Diploma((long) id, "Windsurfen", id, diplomaEis);
        diploma.setDiplomaEis(diplomaEis);
        return diploma;
    }

    public static List<DiplomaEis> createCwoEisenList(int aantal) {
        List<DiplomaEis> diplomaEis = new ArrayList();
        for (int i = 0; i < aantal; i++) {
            diplomaEis.add(createCwoEis(i));
        }
        return diplomaEis;
    }

    public static DiplomaEis createCwoEis(int id) {
        return new DiplomaEis((long) id, "Oploeven" + id, "Naar de wind toe draaien en nog wat meer tekst en nog een regeltje en er staat nog meer zelfs.");
    }

    public static List<CursistBehaaldEis> createCursistBehaaldEisenLijst(int aantal) {
        List<CursistBehaaldEis> cursistBehaaldEis = new ArrayList();
        for (int i = 0; i < aantal; i++) {
            cursistBehaaldEis.add(cursistBehaaldEisen(i));
        }
        return cursistBehaaldEis;
    }

    public static CursistBehaaldEis cursistBehaaldEisen(int id) {
        return new CursistBehaaldEis();
    }
}
