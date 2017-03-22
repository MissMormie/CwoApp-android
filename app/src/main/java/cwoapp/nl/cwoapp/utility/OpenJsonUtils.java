package cwoapp.nl.cwoapp.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cwoapp.nl.cwoapp.entity.Cursist;
import cwoapp.nl.cwoapp.entity.CursistBehaaldEis;
import cwoapp.nl.cwoapp.entity.CursistHeeftDiploma;
import cwoapp.nl.cwoapp.entity.Diploma;
import cwoapp.nl.cwoapp.entity.DiplomaEis;

/**
 * Created by sonja on 3/22/2017.
 */

// TODO check what a final class is exactly.
public final class OpenJsonUtils {

    public static List<Cursist> getCursistLijst(String cursistLijstString) throws JSONException {
        if (cursistLijstString == null || cursistLijstString == "")
            return null;

        List<Cursist> cursistList = new ArrayList();
        JSONArray cursistenArray = new JSONArray(cursistLijstString);
        for (int i = 0; i < cursistenArray.length(); i++) {

            JSONObject cursistJson = cursistenArray.getJSONObject(i);
            Cursist cursist = getCursist(cursistJson);

            cursistList.add(cursist);
        }
        return cursistList;
    }

    public static Cursist getCursist(String cursistString) throws JSONException {
        if (cursistString == null || cursistString == "")
            return null;

        JSONObject cursistJson = new JSONObject(cursistString);

        return getCursist(cursistJson);
    }


    public static Cursist getCursist(JSONObject cursistJson) throws JSONException {
        // id & voornaam are required, so no check if they're not null
        Long id = cursistJson.getLong("id");
        String voornaam = cursistJson.getString("voornaam");

        String tussenvoegsel = null;
        if (!cursistJson.isNull("tussenvoegsel")) {
            tussenvoegsel = cursistJson.getString("tussenvoegsel");
        }

        String achternaam = null;
        if (!cursistJson.isNull("achternaam")) {
            achternaam = cursistJson.getString("achternaam");
        }

        String opmerking = null;
        if (!cursistJson.isNull("opmerkingen")) {
            opmerking = cursistJson.getString("opmerkingen");
        }

        String foto = null;
        if (!cursistJson.isNull("foto")) {
            foto = cursistJson.getString("foto");
        }
        // TODO get date from json.
//        Date paspoort = cursistJson.
        Date paspoort = new Date();

        // Get the cursistbehaaldeis objecten if they exist.
        List<CursistBehaaldEis> cursistBehaaldEisList = null;
        if (cursistJson.has("cursistBehaaldEis") && !cursistJson.isNull("cursistBehaaldEis")) {
            JSONArray eisenBehaaldArray = cursistJson.getJSONArray("cursistBehaaldEis");
            cursistBehaaldEisList = getCursistBehaaldEisLijst(eisenBehaaldArray);
        }

        List<CursistHeeftDiploma> cursistHeeftDiplomaList = null;
        if (cursistJson.has("cursistHeeftDiploma") && !cursistJson.isNull("cursistBehaaldEis")) {
            JSONArray diplomaBehaaldArray = cursistJson.getJSONArray("cursistBehaaldEis");
            cursistHeeftDiplomaList = getCursistHeeftDiplomaLijst(diplomaBehaaldArray);
        }

        Cursist cursist = new Cursist(id, voornaam, tussenvoegsel, achternaam, foto, paspoort, opmerking, cursistBehaaldEisList, cursistHeeftDiplomaList);

        return cursist;
    }

    public static List<CursistBehaaldEis> getCursistBehaaldEisLijst(JSONArray eisenBehaaldArray) throws JSONException {
        List<CursistBehaaldEis> cursistBehaaldEisList = new ArrayList<>();
        for (int i = 0; i < eisenBehaaldArray.length(); i++) {
            JSONObject cursistBehaaldEisJSon = eisenBehaaldArray.getJSONObject(i);
            CursistBehaaldEis cbe = getCursistBehaaldEis(cursistBehaaldEisJSon);
            cursistBehaaldEisList.add(cbe);
        }

        return cursistBehaaldEisList;
    }

    public static CursistBehaaldEis getCursistBehaaldEis(JSONObject cursistBehaaldEisJson) throws JSONException {
        Long id = cursistBehaaldEisJson.getLong("id");
        // TODO get date from JSON.
        JSONObject diplomaEisJson = cursistBehaaldEisJson.getJSONObject("diplomaEis");
        DiplomaEis diplomaEis = getDiplomaEis(diplomaEisJson);
        return new CursistBehaaldEis(id, diplomaEis);
    }

    public static List<CursistHeeftDiploma> getCursistHeeftDiplomaLijst(JSONArray diplomaBehaaldArray) throws JSONException {
        List<CursistHeeftDiploma> cursistHeeftDiplomaList = new ArrayList();
        for (int i = 0; i < diplomaBehaaldArray.length(); i++) {
            JSONObject cursistHeeftDiplomaJson = diplomaBehaaldArray.getJSONObject(i);
            CursistHeeftDiploma cursistHeeftDiploma = getCursistHeeftDiploma(cursistHeeftDiplomaJson);
            cursistHeeftDiplomaList.add(cursistHeeftDiploma);
        }

        return cursistHeeftDiplomaList;
    }

    public static CursistHeeftDiploma getCursistHeeftDiploma(JSONObject cursistHeeftDiplomaJson) throws JSONException {
        Long id = cursistHeeftDiplomaJson.getLong("id");
        Long cursist = cursistHeeftDiplomaJson.getLong("cursist");
        // TODO add date
        //Date diplomaBehaald =
        Diploma diploma = null;
        if (cursistHeeftDiplomaJson.has("diploma") && !cursistHeeftDiplomaJson.isNull("diploma")) {
            JSONObject diplomaJson = cursistHeeftDiplomaJson.getJSONObject("diploma");
            diploma = getDiploma(diplomaJson);
        }
        return new CursistHeeftDiploma(id, cursist, diploma);
    }

    public static List<Diploma> getDiplomaLijst(String diplomaLijstString) throws JSONException {
        // TODO see if i can do some extra error checking here?
        if (diplomaLijstString == null || diplomaLijstString == "")
            return null;

        List<Diploma> diplomaList = new ArrayList();
        JSONArray diplomasArray = new JSONArray(diplomaLijstString);
        for (int i = 0; i < diplomasArray.length(); i++) {

            JSONObject diplomaJson = diplomasArray.getJSONObject(i);
            Diploma diploma = getDiploma(diplomaJson);

            diplomaList.add(diploma);
        }
        return diplomaList;
    }


    /**
     * Takes a JSONobject with diploma information and returns a corresponding diploma object.
     *
     * @param diplomaJson
     * @return
     * @throws JSONException
     */
    public static Diploma getDiploma(JSONObject diplomaJson) throws JSONException {
        Long id = diplomaJson.getLong("id");
        String titel = diplomaJson.getString("titel");
        int nivo = diplomaJson.getInt("nivo");

        // Check if there's a list of diploma eisen.
        List<DiplomaEis> diplomaEisList = null;
        if (diplomaJson.has("diplomaEisen") && !diplomaJson.isNull("diplomaEisen")) {
            JSONArray eisenArray = diplomaJson.getJSONArray("diplomaEisen");
            diplomaEisList = getDiplomaEisLijst(eisenArray);
        }

        Diploma diploma = new Diploma(id, titel, nivo, diplomaEisList);
        return diploma;
    }

    /**
     * Takes a JSONArray of diplomaEis information and returns a list filled with diplomaEis objects.
     *
     * @param diplomaEisJsonArray
     * @return
     * @throws JSONException
     */
    public static List<DiplomaEis> getDiplomaEisLijst(JSONArray diplomaEisJsonArray) throws JSONException {
        List<DiplomaEis> diplomaEisList = new ArrayList();
        for (int i = 0; i < diplomaEisJsonArray.length(); i++) {
            JSONObject diplomaEisJson = diplomaEisJsonArray.getJSONObject(i);
            DiplomaEis diplomaEis = getDiplomaEis(diplomaEisJson);
            diplomaEisList.add(diplomaEis);
        }

        return diplomaEisList;
    }

    /**
     * takes a JSONObject with diplomaEis information and returns a filled DiplomaEis object
     *
     * @param diplomaEisJson
     * @return
     * @throws JSONException
     */
    public static DiplomaEis getDiplomaEis(JSONObject diplomaEisJson) throws JSONException {
        Long id = diplomaEisJson.getLong("id");
        String titel = diplomaEisJson.getString("titel");
        String omschrijving = diplomaEisJson.getString("omschrijving");
        Boolean theorie = diplomaEisJson.getBoolean("theorie");

        DiplomaEis diplomaEis = new DiplomaEis(id, titel, omschrijving);

        if (diplomaEisJson.has("diploma") && !diplomaEisJson.isNull("diploma")) {
            Diploma diploma = getDiploma(diplomaEisJson.getJSONObject("diploma"));
            diplomaEis.setDiploma(diploma);
        }
        return diplomaEis;
    }
}
