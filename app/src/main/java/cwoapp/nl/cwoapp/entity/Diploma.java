package cwoapp.nl.cwoapp.entity;

import java.util.List;

/**
 * Created by sonja on 3/14/2017.
 */

public class Diploma {
    private Long id;
    private String titel;
    private List<CwoEis> cwoEis;
    private int nivo;

    public Diploma(Long id, String titel, int nivo, List<CwoEis> cwoEis) {
        this.id = id;
        this.titel = titel;
        this.cwoEis = cwoEis;
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

    public List<CwoEis> getCwoEis() {
        return cwoEis;
    }

    public void setCwoEis(List<CwoEis> cwoEis) {
        this.cwoEis = cwoEis;
    }

    public int getNivo() {
        return nivo;
    }

    public void setNivo(int nivo) {
        this.nivo = nivo;
    }
}
