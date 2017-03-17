package cwoapp.nl.cwoapp.utility;

import java.util.ArrayList;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.CursistBehaaldEisen;
import cwoapp.nl.cwoapp.entity.CwoEis;
import cwoapp.nl.cwoapp.entity.Diploma;

/**
 * Created by sonja on 3/14/2017.
 */

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
        List<CursistBehaaldEisen> cbe = createCursistBehaaldEisenLijst(3);
        cursist.setCursistBehaaldEisen(cbe);
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
        List<CwoEis> cwoEis = createCwoEisenList(10);
        Diploma diploma = new Diploma((long) id, "Windsurfen", id, cwoEis);
        diploma.setCwoEis(cwoEis);
        return diploma;
    }

    public static List<CwoEis> createCwoEisenList(int aantal) {
        List<CwoEis> cwoEis = new ArrayList();
        for (int i = 0; i < aantal; i++) {
            cwoEis.add(createCwoEis(i));
        }
        return cwoEis;
    }

    public static CwoEis createCwoEis(int id) {
        return new CwoEis((long) id, "Oploeven" + id, "Naar de wind toe draaien en nog wat meer tekst en nog een regeltje en er staat nog meer zelfs.");
    }

    public static List<CursistBehaaldEisen> createCursistBehaaldEisenLijst(int aantal) {
        List<CursistBehaaldEisen> cursistBehaaldEisen = new ArrayList();
        for (int i = 0; i < aantal; i++) {
            cursistBehaaldEisen.add(cursistBehaaldEisen(i));
        }
        return cursistBehaaldEisen;
    }

    public static CursistBehaaldEisen cursistBehaaldEisen(int id) {
        return new CursistBehaaldEisen();
    }
}
