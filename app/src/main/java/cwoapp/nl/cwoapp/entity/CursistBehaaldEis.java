package cwoapp.nl.cwoapp.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by sonja on 3/14/2017.
 * CursistBehaaldEis
 */

public class CursistBehaaldEis {
    private Long id;
    private Cursist cursist;
    private DiplomaEis diplomaEis;
    private Date datum = new Date();
    private boolean behaald;


    public CursistBehaaldEis() {
    }

    public CursistBehaaldEis(Long id, DiplomaEis diplomaEis) {
        this.id = id;
        this.diplomaEis = diplomaEis;
    }

    public CursistBehaaldEis(Cursist cursist, DiplomaEis diplomaEis, boolean behaald) {
        this.cursist = cursist;
        this.diplomaEis = diplomaEis;
        this.behaald = behaald;
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

    public boolean isBehaald() {
        return behaald;
    }

    public void setBehaald(boolean behaald) {
        this.behaald = behaald;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("eisId", diplomaEis.getId());
            jsonObject.put("cursistId", cursist.id);
            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }
}
