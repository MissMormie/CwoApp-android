package cwoapp.nl.cwoapp.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by Sonja on 3/9/2017.
 */

public class Cursist {
    public Long id;
    public String voornaam;
    public String tussenvoegsel;
    public String achternaam;
    public String foto;
    public Date paspoort;
    public String opmerking;
    public List<CursistBehaaldEis> cursistBehaaldEis;

    private List<CursistHeeftDiploma> cursistHeeftDiplomas;

    public Cursist(Long id, String voornaam, String tussenvoegsel, String achternaam, String foto, Date paspoort, String opmerking, List<CursistBehaaldEis> cursistBehaaldEis, List<CursistHeeftDiploma> cursistHeeftDiplomas) {
        this.id = id;
        this.voornaam = voornaam;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.foto = foto;
        this.paspoort = paspoort;
        this.opmerking = opmerking;
        this.cursistBehaaldEis = cursistBehaaldEis;
        this.cursistHeeftDiplomas = cursistHeeftDiplomas;
    }

    public Cursist(long id, String voornaam, String tussenvoegsel, String achternaam, String opmerking, String foto, Date paspoort) {
        this.opmerking = opmerking;
        this.paspoort = paspoort;
        this.id = id;
        this.voornaam = voornaam;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.foto = foto;
    }


    public List<CursistBehaaldEis> getCursistBehaaldEis() {
        return cursistBehaaldEis;
    }

    public void setCursistBehaaldEis(List<CursistBehaaldEis> cursistBehaaldEis) {
        this.cursistBehaaldEis = cursistBehaaldEis;
    }

    public List<CursistHeeftDiploma> getCursistHeeftDiplomas() {
        return cursistHeeftDiplomas;
    }

    public void setCursistHeeftDiplomas(List<CursistHeeftDiploma> cursistHeeftDiplomas) {
        this.cursistHeeftDiplomas = cursistHeeftDiplomas;
    }


    public String nameToString() {
        String tussenstuk = "";
        if(tussenvoegsel != null && tussenvoegsel != "")
            tussenstuk = tussenvoegsel + "";

        return voornaam + " " + tussenstuk + achternaam;
    }

    public boolean isEisBehaald(DiplomaEis diplomaEis) {
        if (cursistBehaaldEis == null)
            return false;
        for (CursistBehaaldEis cbe : cursistBehaaldEis) {
            if (cbe.getDiplomaEis() != null && cbe.getDiplomaEis().getId() == diplomaEis.getId()) {
                return true;
            }
        }
        return false;
    }
}
