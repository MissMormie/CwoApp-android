package cwoapp.nl.cwoapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import cwoapp.nl.cwoapp.utility.DateUtil;

/**
 * Created by Sonja on 3/9/2017.
 */

public class Cursist implements Parcelable {
    public Long id;
    public String voornaam;
    public String tussenvoegsel;
    public String achternaam;
    public String foto;
    public Date paspoort;
    public String opmerking;
    public List<CursistBehaaldEis> cursistBehaaldEis;

    private List<CursistHeeftDiploma> cursistHeeftDiplomas;

    public Cursist() {
    }

    public Cursist(Long id, String voornaam, String tussenvoegsel, String achternaam, String foto, Date paspoort, String opmerking, List<CursistBehaaldEis> cursistBehaaldEis, List<CursistHeeftDiploma> cursistHeeftDiplomas) {
        this.id = id;
        this.voornaam = voornaam;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.foto = foto;
        this.paspoort = paspoort;
        this.opmerking = opmerking;
        this.cursistBehaaldEis = cursistBehaaldEis;
        this.cursistHeeftDiplomas = cursistHeeftDiplomas;
    }

    public Cursist(long id, String voornaam, String tussenvoegsel, String achternaam, String opmerking, String foto, Date paspoort) {
        this.opmerking = opmerking;
        this.paspoort = paspoort;
        this.id = id;
        this.voornaam = voornaam;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.foto = foto;
    }


    public List<CursistBehaaldEis> getCursistBehaaldEis() {
        return cursistBehaaldEis;
    }

    public void setCursistBehaaldEis(List<CursistBehaaldEis> cursistBehaaldEis) {
        this.cursistBehaaldEis = cursistBehaaldEis;
    }

    public List<CursistHeeftDiploma> getCursistHeeftDiplomas() {
        return cursistHeeftDiplomas;
    }

    public void setCursistHeeftDiplomas(List<CursistHeeftDiploma> cursistHeeftDiplomas) {
        this.cursistHeeftDiplomas = cursistHeeftDiplomas;
    }


    public String nameToString() {
        String tussenstuk = "";
        if(tussenvoegsel != null && tussenvoegsel != "")
            tussenstuk = tussenvoegsel + " ";

        return voornaam + " " + tussenstuk + achternaam;
    }

    public String simpleCursistToJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("voornaam", voornaam);
            jsonObject.put("tussenvoegsel", tussenvoegsel);
            jsonObject.put("achternaam", achternaam);
            jsonObject.put("opmerkingen", opmerking);
            jsonObject.put("foto", foto);
            if (paspoort != null) {
                jsonObject.put("paspoort", DateUtil.dateToYYYYMMDDString(paspoort));
            } else {
                jsonObject.put("paspoort", null);
            }


            String jsonString = jsonObject.toString();
            return jsonString;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    // TODO Make this smarter with a map so i don't have to run through everything every time. Only the first time. (together with isAlleEisenBehaald)
    // Low prio, low numbers make this not very slow.
    public boolean isEisBehaald(DiplomaEis diplomaEis) {
        if (cursistBehaaldEis == null)
            return false;
        for (CursistBehaaldEis cbe : cursistBehaaldEis) {
            if (cbe.getDiplomaEis() != null && cbe.getDiplomaEis().getId() == diplomaEis.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlleEisenBehaald(List<DiplomaEis> diplomaEisList) {
        for (DiplomaEis diplomaEis : diplomaEisList) {
            // Als 1 eis niet is behaald, is niet alles behaald, dus return false.
            if (!isEisBehaald(diplomaEis))
                return false;
        }
        return true;
    }


    // ---------------------------- Support for Parcelable --------------------------------------- //

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(voornaam);
        parcel.writeString(tussenvoegsel);
        parcel.writeString(achternaam);
        parcel.writeString(opmerking);
        parcel.writeString(foto);
        if (paspoort != null) {
            parcel.writeLong(paspoort.getTime());
        } else {
            parcel.writeLong(0l);
        }
    }

    public static final Parcelable.Creator<Cursist> CREATOR = new Parcelable.Creator<Cursist>() {
        @Override
        public Cursist createFromParcel(Parcel source) {
            return new Cursist(source);
        }

        @Override
        public Cursist[] newArray(int size) {
            return new Cursist[size];
        }
    };

    // Note, the order IS important, if it's not the same as when parceling it doesn't work.
    public Cursist(Parcel parcel) {
        id = parcel.readLong();
        voornaam = parcel.readString();
        tussenvoegsel = parcel.readString();
        achternaam = parcel.readString();
        opmerking = parcel.readString();
        foto = parcel.readString();

        // Get around paspoort sometimes being null.
        Long paspoortTemp = parcel.readLong();

        if (paspoortTemp == 0l) {
            paspoort = null;
        } else {
            paspoort = new Date(paspoortTemp);
        }
    }
}
