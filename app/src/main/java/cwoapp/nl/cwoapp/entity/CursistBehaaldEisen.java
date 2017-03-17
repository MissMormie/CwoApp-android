package cwoapp.nl.cwoapp.entity;

import java.util.Date;

/**
 * Created by sonja on 3/14/2017.
 */

public class CursistBehaaldEisen {
    private Long id;
    private Cursist cursist;
    private CwoEis cwoEis;
    private Date datum = new Date();


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

    public CwoEis getCwoEis() {
        return cwoEis;
    }

    public void setCwoEis(CwoEis cwoEis) {
        this.cwoEis = cwoEis;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }
}
