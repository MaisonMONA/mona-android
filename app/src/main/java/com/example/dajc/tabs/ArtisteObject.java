package com.example.dajc.tabs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by emile on 2018-02-06.
 */

public class ArtisteObject implements Parcelable {
    public String Nom;
    public String Prenom;
    public String idArtiste;
    public Boolean Collectif;
    //public ArrayList<OeuvreObject> Oeuvres;

    public String getNom() {
        return Nom;
    }

    /*public void addOeuvres(OeuvreObject oeuvres) {
        Oeuvres.add(oeuvres);
    }
*/
    public String getPrenom() {
        return Prenom;
    }

    public String getIdArtiste() {
        return idArtiste;
    }

    public Boolean getCollectif() {
        return Collectif;
    }
/*
    public ArrayList<OeuvreObject> getOeuvres() {
        return Oeuvres;
    }
*/
    public ArtisteObject(String nom, String prenom, String idArtiste, Boolean collectif) {
        Nom = nom;
        if(collectif==false) {
            Prenom = prenom;

        }

        else Prenom=null;
        this.idArtiste = idArtiste;
        Collectif = collectif;
        //Oeuvres= new ArrayList<OeuvreObject>();
    }
    protected ArtisteObject(Parcel in) {
        Nom = in.readString();
        Prenom = in.readString();
        idArtiste = in.readString();
        byte CollectifVal = in.readByte();
        Collectif = CollectifVal == 0x02 ? null : CollectifVal != 0x00;
        /*if (in.readByte() == 0x01) {
            Oeuvres = new ArrayList<OeuvreObject>();
            in.readList(Oeuvres, OeuvreObject.class.getClassLoader());
        } else {
            Oeuvres = null;
        }
        */
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Nom);
        dest.writeString(Prenom);
        dest.writeString(idArtiste);
        if (Collectif == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (Collectif ? 0x01 : 0x00));
        }
        /*
        if (Oeuvres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Oeuvres);
        }
        */
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ArtisteObject> CREATOR = new Parcelable.Creator<ArtisteObject>() {
        @Override
        public ArtisteObject createFromParcel(Parcel in) {
            return new ArtisteObject(in);
        }

        @Override
        public ArtisteObject[] newArray(int size) {
            return new ArtisteObject[size];
        }
    };
}
