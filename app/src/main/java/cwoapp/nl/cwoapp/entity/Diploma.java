package cwoapp.nl.cwoapp.entity;

import java.util.List;

/**
 * Created by sonja on 3/14/2017.
 */

public class Diploma {
    private Long id;
    private String titel;
    private List<DiplomaEis> diplomaEis;
    private int nivo;

    public Diploma(Long id, String titel, int nivo, List<DiplomaEis> diplomaEis) {
        this.id = id;
        this.titel = titel;
        this.diplomaEis = diplomaEis;
        this.nivo = nivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public List<DiplomaEis> getDiplomaEis() {
        return diplomaEis;
    }

    public void setDiplomaEis(List<DiplomaEis> diplomaEis) {
        this.diplomaEis = diplomaEis;
    }

    public int getNivo() {
        return nivo;
    }

    public void setNivo(int nivo) {
        this.nivo = nivo;
    }
}
