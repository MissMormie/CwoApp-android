package cwoapp.nl.cwoapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Set;

/**
 * Created by sonja on 3/14/2017.
 */

public class CwoEis implements Parcelable {

    private Long id;
    private Diploma diploma;
    private String titel;
    private String omschrijving;
    private Set<CursistBehaaldEisen> cursistBehaaldEisen;

    // Used to save whether the checkbox for this eis is checked.
    // TODO determine if this belongs here or should move to a seperate class that holds this info.
    // since it doesn't fit in the model well.
    private boolean checked;


    public CwoEis(Long id, String titel, String omschrijving) {
        this.id = id;
        this.titel = titel;
        this.omschrijving = omschrijving;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Diploma getDiploma() {
        return diploma;
    }

    public void setDiploma(Diploma diploma) {
        this.diploma = diploma;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public Set<CursistBehaaldEisen> getCursistBehaaldEisen() {
        return cursistBehaaldEisen;
    }

    public void setCursistBehaaldEisen(Set<CursistBehaaldEisen> cursistBehaaldEisen) {
        this.cursistBehaaldEisen = cursistBehaaldEisen;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toggleChecked() {
        checked = !checked;
    }


    // ---------------------------- Support for Parcelable --------------------------------------- //
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<CwoEis> CREATOR = new Parcelable.Creator<CwoEis>() {
        @Override
        public CwoEis createFromParcel(Parcel source) {
            return new CwoEis(source);
        }

        @Override
        public CwoEis[] newArray(int size) {
            return new CwoEis[size];
        }
    };

    public CwoEis(Parcel parcel) {
        id = parcel.readLong();
        titel = parcel.readString();
        omschrijving = parcel.readString();

    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // TODO make this completely parcelable, now it only saves part of the data.
        parcel.writeLong(id);
        parcel.writeString(titel);
        parcel.writeString(omschrijving);
    }
}
