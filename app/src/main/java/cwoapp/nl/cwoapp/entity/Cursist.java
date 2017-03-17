package cwoapp.nl.cwoapp.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
    public List<CursistBehaaldEisen> cursistBehaaldEisen;

    private Set<CursistHeeftDiploma> cursistHeeftDiplomas;


    public Cursist(long id, String voornaam, String tussenvoegsel, String achternaam, String opmerking, String foto, Date paspoort) {
        this.opmerking = opmerking;
        this.paspoort = paspoort;
        this.id = id;
        this.voornaam = voornaam;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.foto = foto;
    }


    public List<CursistBehaaldEisen> getCursistBehaaldEisen() {
        return cursistBehaaldEisen;
    }

    public void setCursistBehaaldEisen(List<CursistBehaaldEisen> cursistBehaaldEisen) {
        this.cursistBehaaldEisen = cursistBehaaldEisen;
    }

    public Set<CursistHeeftDiploma> getCursistHeeftDiplomas() {
        return cursistHeeftDiplomas;
    }

    public void setCursistHeeftDiplomas(Set<CursistHeeftDiploma> cursistHeeftDiplomas) {
        this.cursistHeeftDiplomas = cursistHeeftDiplomas;
    }


    public String nameToString() {
        String tussenstuk = "";
        if(tussenvoegsel != null && tussenvoegsel != "")
            tussenstuk = tussenvoegsel + "";

        return voornaam + " " + tussenstuk + achternaam;
    }

    public boolean isEisBehaald(CwoEis cwoEis) {
        if (cursistBehaaldEisen == null)
            return false;
        for (CursistBehaaldEisen cbe : cursistBehaaldEisen) {
            if (cbe.getCwoEis() != null && cbe.getCwoEis().getId() == cwoEis.getId()) {
                return true;
            }
        }
        return false;
    }
}
