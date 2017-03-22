package cwoapp.nl.cwoapp.entity;

import java.util.Date;

/**
 * Created by sonja on 3/14/2017.
 */

public class CursistHeeftDiploma {
    private Long id;

    private Long cursist;

    private Diploma cwoDiscipline;

    private Date diplomaBehaald;

    public CursistHeeftDiploma(Long id, Long cursist, Diploma cwoDiscipline) {
        this.id = id;
        this.cursist = cursist;
        this.cwoDiscipline = cwoDiscipline;
    }
}
