package cwoapp.nl.cwoapp.entity;

import java.util.Date;

/**
 * Created by Sonja on 3/9/2017.
 */

public class Cursist {
    private Long id;
    private String voornaam;
    private String tussenvoegsel;
    private String achternaam;
    private String foto;
    private Date paspoort;

    public Cursist(String voornaam, String tussenvoegsel, String achternaam, String foto) {
        this.voornaam = voornaam;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.foto = foto;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Date getPaspoort() {
        return paspoort;
    }

    public void setPaspoort(Date paspoort) {
        this.paspoort = paspoort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String nameToString() {
        String tussenstuk = "";
        if(tussenvoegsel != null && tussenvoegsel != "")
            tussenstuk = tussenvoegsel + "";

        return voornaam + " " + tussenstuk + achternaam;
    }
}
