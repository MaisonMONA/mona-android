package com.example.dajc.tabs;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by emile on 2018-02-06.
 */
@Entity (tableName = "oeuvre")
public class OeuvreObject implements Parcelable {

    public String Titre;
    @PrimaryKey @NonNull public String Id;
    public int Etat;
    public String URI;
    public String Commentaire;
    public int Note;
    public String Quartier;
    public String Dimension;
    public String Technique;
    //public ArtisteObject Artiste;
    public String Artiste;
    public String DatedeCreation;
    public String Materiaux;
    public double LocationX;
    public double LocationY;

    public String getDatedePhoto() {
        return DatedePhoto;
    }

    public void setDatedePhoto(String datedePhoto) {
        DatedePhoto = datedePhoto;
    }

    public String DatedePhoto;

    public String getTitre() {
        return Titre;
    }
    /*
    public ArtisteObject getArtiste() {
        return Artiste;
    }
*/
    public String getArtiste(){return Artiste;}

    public String getDatedeCreation() {
        return DatedeCreation;
    }

    public String getMateriaux() {
        return Materiaux;
    }

    public double getLocationX() {
        return LocationX;
    }

    public double getLocationY() {
        return LocationY;
    }

    public int getEtat() {
        return Etat;
    }

    public String getURI() {
        return URI;
    }

    public String getCommentaire() {
        return Commentaire;
    }

    public int getNote() {
        return Note;
    }

    public String getQuartier() {
        return Quartier;
    }

    public void setQuartier(String quartier) {
        Quartier = quartier;
    }

    public String getDimension() {
        return Dimension;
    }

    public void setDimension(String dimension) {
        Dimension = dimension;
    }

    public String getTechnique() {
        return Technique;
    }

    public void setTechnique(String technique) {
        Technique = technique;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public void setEtat(int etat) {
        Etat = etat;
    }

    public void setCommentaire(String commentaire) {
        Commentaire = commentaire;
    }

    public void setNote(int note) {
        Note = note;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
    public OeuvreObject(){};
    public OeuvreObject(String titre, String id, String artiste, String datedeCreation, String materiaux, double locationX, double locationY, int etat, String URI, String commentaire, int note, String quartier, String dimension, String technique) {
        Titre = titre;
        Id = id;
        Artiste = artiste;
        DatedeCreation = datedeCreation;
        Materiaux = materiaux;
        LocationX = locationX;
        LocationY = locationY;
        Etat = etat;
        this.URI = URI;
        Commentaire = commentaire;
        Note = note;
        Quartier = quartier;
        Dimension= dimension;
        Technique= technique;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public OeuvreObject createFromParcel(Parcel in) {
            return new OeuvreObject(in);
        }

        public OeuvreObject[] newArray(int size) {
            return new OeuvreObject[size];
        }
    };
    public OeuvreObject(Parcel in){
        this.Etat = in.readInt();
        this.Id = in.readString();
        this.Titre = in.readString();
        //Artiste = (ArtisteObject) in.readValue(ArtisteObject.class.getClassLoader());
        this.Artiste = in.readString();
        this.DatedeCreation = in.readString();
        this.Materiaux = in.readString();
        this.LocationX = in.readDouble();
        this.LocationY = in.readDouble();
        this.URI = in.readString();
        this.Commentaire = in.readString();
        this.Note = in.readInt();
        this.Quartier= in.readString();
        this.Dimension= in.readString();
        this.Technique=in.readString();
        this.DatedePhoto=in.readString();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Etat);
        dest.writeString(this.Id);
        dest.writeString(this.Titre);
        //dest.writeValue(Artiste);
        dest.writeString(this.Artiste);
        dest.writeString(this.DatedeCreation);
        dest.writeString(this.Materiaux);
        dest.writeDouble(this.LocationX);
        dest.writeDouble(this.LocationY);
        dest.writeString(this.URI);
        dest.writeString(this.Commentaire);
        dest.writeInt(this.Note);
        dest.writeString(this.Quartier);
        dest.writeString(this.Dimension);
        dest.writeString(this.Technique);
        dest.writeString(this.DatedePhoto);

    }

    @Override
    public String toString() {
        return "Oeuvre{" +
                "Titre='" + Titre + '\'' +
                ", Materiaux='" + Materiaux + '\'' +
                ", LocationX='" + LocationX + '\'' +
                '}';
    }
}
