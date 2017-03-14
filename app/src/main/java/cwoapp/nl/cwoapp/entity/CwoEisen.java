package cwoapp.nl.cwoapp.entity;

import java.util.Set;

/**
 * Created by sonja on 3/14/2017.
 */

public class CwoEisen {

    private Long id;
    private Diploma diploma;
    private String titel;
    private String omschrijving;
    private Set<CursistBehaaldEisen> cursistBehaaldEisen;

}
