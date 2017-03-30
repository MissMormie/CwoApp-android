package cwoapp.nl.cwoapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Set;

/**
 * Created by sonja on 3/14/2017.
 */

public class DiplomaEis implements Parcelable {

    private Long id;
    private Diploma diploma;
    private String titel;
    private String omschrijving;
    private Set<CursistBehaaldEis> cursistBehaaldEis;

    // Used to save whether the checkbox for this eis is checked.
    // TODO determine if this belongs here or should move to a seperate class that holds this info.
    // since it doesn't fit in the model well.
    private boolean checked;


    public DiplomaEis(Long id, String titel, String omschrijving) {
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

    public Set<CursistBehaaldEis> getCursistBehaaldEis() {
        return cursistBehaaldEis;
    }

    public void setCursistBehaaldEis(Set<CursistBehaaldEis> cursistBehaaldEis) {
        this.cursistBehaaldEis = cursistBehaaldEis;
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

    public static final Parcelable.Creator<DiplomaEis> CREATOR = new Parcelable.Creator<DiplomaEis>() {
        @Override
        public DiplomaEis createFromParcel(Parcel source) {
            return new DiplomaEis(source);
        }

        @Override
        public DiplomaEis[] newArray(int size) {
            return new DiplomaEis[size];
        }
    };

    public DiplomaEis(Parcel parcel) {
        id = parcel.readLong();
        titel = parcel.readString();
        omschrijving = parcel.readString();
        diploma = parcel.readParcelable(Diploma.class.getClassLoader());

    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(titel);
        parcel.writeString(omschrijving);
        // not great solution because now every diplomaEis will have it's own diploma object..
        parcel.writeParcelable(diploma, flags);
    }
}
