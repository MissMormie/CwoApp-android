package cwoapp.nl.cwoapp.utility;

import java.util.ArrayList;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.CursistBehaaldEis;
import cwoapp.nl.cwoapp.entity.Diploma;
import cwoapp.nl.cwoapp.entity.DiplomaEis;

/**
 * Created by sonja on 3/14/2017.
 * Mock entity generator
 */

// TODO list
// TODO Info knop activeren bij diplomaEisen
// TODO Diploma's uitgeven
// TODO foto's maken en opslaan toevoegen


class MockEntityGenerator {

    public static List<Cursist> createCursistList(int aantal) {
        List<Cursist> cursistList = new ArrayList<>();
        for (int i = 0; i < aantal; i++) {
            cursistList.add(createFullCursist(i));
        }
        return cursistList;
    }

    private static Cursist createSimpleCursist(int id) {
        return new Cursist((long) id, "Sonja" + id);
    }

    private static Cursist createFullCursist(int id) {
        Cursist cursist = createSimpleCursist(id);
        List<CursistBehaaldEis> cbe = createCursistBehaaldEisenLijst();
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

    private static Diploma createDiploma(int id) {
        List<DiplomaEis> diplomaEis = createCwoEisenList();
        Diploma diploma = new Diploma((long) id, "Windsurfen", id, diplomaEis);
        diploma.setDiplomaEis(diplomaEis);
        return diploma;
    }

    private static List<DiplomaEis> createCwoEisenList() {
        List<DiplomaEis> diplomaEis = new ArrayList();
        for (int i = 0; i < 10; i++) {
            diplomaEis.add(createCwoEis(i));
        }
        return diplomaEis;
    }

    private static DiplomaEis createCwoEis(int id) {
        return new DiplomaEis((long) id, "Oploeven" + id, "Naar de wind toe draaien en nog wat meer tekst en nog een regeltje en er staat nog meer zelfs.");
    }

    private static List<CursistBehaaldEis> createCursistBehaaldEisenLijst() {
        List<CursistBehaaldEis> cursistBehaaldEis = new ArrayList();
        for (int i = 0; i < 3; i++) {
            cursistBehaaldEis.add(cursistBehaaldEisen());
        }
        return cursistBehaaldEis;
    }

    private static CursistBehaaldEis cursistBehaaldEisen() {
        return new CursistBehaaldEis();
    }
}
