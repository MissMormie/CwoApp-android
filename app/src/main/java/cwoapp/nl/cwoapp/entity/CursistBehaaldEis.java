package cwoapp.nl.cwoapp.entity;

import java.util.Date;

/**
 * Created by sonja on 3/14/2017.
 */

public class CursistBehaaldEis {
    private Long id;
    private Cursist cursist;
    private DiplomaEis diplomaEis;
    private Date datum = new Date();


    public CursistBehaaldEis() {
    }

    public CursistBehaaldEis(Long id, DiplomaEis diplomaEis) {
        this.id = id;
        this.diplomaEis = diplomaEis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cursist getCursist() {
        return cursist;
    }

    public void setCursist(Cursist cursist) {
        this.cursist = cursist;
    }

    public DiplomaEis getDiplomaEis() {
        return diplomaEis;
    }

    public void setDiplomaEis(DiplomaEis diplomaEis) {
        this.diplomaEis = diplomaEis;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }
}
