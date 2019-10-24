
import android.os.Parcel;
import android.os.Parcelable;

/*package com.example.dajc.tabs;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "Acquisition")

public class AcquisitionObject implements Parcelable {
    public String IdOeuvre;
    public String Quatrier;
    public String Categorie;
    public String Date;
    public Boolean Oeuvredujour;
    //public ArrayList<OeuvreObject> Oeuvres;

    public String getIdOeuvre() {
        return IdOeuvre;
    }
    public String getQuatrier(){ return Quatrier;}
    public String getCategorie(){ return Categorie;}
    public boolean Oeuvredujour(){ return Oeuvredujour;}
    public String getDate(){return Date;}

    public AcquisitionObject(
            String idOeuvre, String quatrier, String categorie, String date,
            boolean oeuvredujour){
        this.IdOeuvre = idOeuvre;
        this. Quatrier=quatrier;
        this.Categorie=categorie;
        this.Date=date;
        this.Oeuvredujour=oeuvredujour;
    }
    protected AcquisitionObject(Parcel in) {
        this.IdOeuvre = in.readString();
        this.Quatrier = in.readString();
        this.Categorie = in.readString();
        this.Date = in.readString();
        byte CollectifVal = in.readByte();
        this.Oeuvredujour = CollectifVal == 0x02 ? null : CollectifVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(IdOeuvre);
        dest.writeString(Quatrier);
        dest.writeString(Categorie);
        dest.writeString(Date);
        if (Oeuvredujour == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (Oeuvredujour ? 0x01 : 0x00));
        }

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AcquisitionObject> CREATOR = new Parcelable.Creator<AcquisitionObject>() {
        @Override
        public AcquisitionObject createFromParcel(Parcel in) {
            return new AcquisitionObject(in);
        }

        @Override
        public AcquisitionObject[] newArray(int size) {
            return new AcquisitionObject[size];
        }
    };
}
*/