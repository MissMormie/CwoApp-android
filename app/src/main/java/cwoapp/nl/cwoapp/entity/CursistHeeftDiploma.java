package cwoapp.nl.cwoapp.entity;

import java.util.Date;

/**
 * Created by sonja on 3/14/2017.
 */

public class CursistHeeftDiploma {
    private Long id;

    private Long cursist;

    private Diploma diploma;

    private Date diplomaBehaald;

    public CursistHeeftDiploma(Long id, Long cursist, Diploma diploma) {
        this.id = id;
        this.cursist = cursist;
        this.diploma = diploma;
    }

    public Diploma getDiploma() {
        return diploma;
    }

    public void setDiploma(Diploma diploma) {
        this.diploma = diploma;
    }
}
