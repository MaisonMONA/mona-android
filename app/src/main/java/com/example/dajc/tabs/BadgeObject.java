package com.example.dajc.tabs;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by emile on 2018-02-06.
 */
@Entity (tableName = "badge")
public class BadgeObject implements Parcelable {

    public String Titre;
    @PrimaryKey @NonNull public String Id;
    public String Name;
    public int Progress;
    public int Objective;
    public String LogoNoColor;
    public String LogoWithColor;
    public String Commentaire;
    public String Condition;
    public String ConditionValue;



    public String getTitre() {
        return Titre;
    }
    public String getId() {
        return Id;
    }
    public int getProgress(){return Progress;}
    public int getObjective() {
        return Objective;
    }
    public String getLogoNoColor() {
        return LogoNoColor;
    }
    public String getLogoWithColor() {
        return LogoWithColor;
    }
    public String getCommentaire() {
        return Commentaire;
    }
    public String getCondition() {
        return Condition;
    }
    public String getConditionValue() {
        return ConditionValue;
    }

    public void setProgress(int progress_) {
        Progress = progress_;
    }
    public void addToProgress(int toAdd) { setProgress(toAdd+Progress); }

    public BadgeObject(){};

    public BadgeObject(String titre, String condition, String conditionval, String id, String logoWithColor, String logoNoColor, String commentaire, int progress, int objective) {
        this.Titre = titre;
        this.Id = id;
        this.LogoNoColor = logoNoColor;
        this.LogoWithColor = logoWithColor;
        this.Objective = objective;
        this.Progress = progress;
        this.Commentaire = commentaire;
        this.Condition = condition;
        this.ConditionValue = conditionval;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BadgeObject createFromParcel(Parcel in) {
            return new BadgeObject(in);
        }

        public BadgeObject[] newArray(int size) {
            return new BadgeObject[size];
        }
    };
    public BadgeObject(Parcel in){
        this.Progress = in.readInt();
        this.Objective = in.readInt();
        this.LogoWithColor = in.readString();
        this.LogoNoColor = in.readString();
        this.Id = in.readString();
        this.Titre = in.readString();
        this.Commentaire = in.readString();
        this.Condition = in.readString();
        this.ConditionValue = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Progress);
        dest.writeInt(this.Objective);
        dest.writeString(this.Id);
        dest.writeString(this.LogoWithColor);
        dest.writeString(this.LogoNoColor);
        dest.writeString(this.Titre);
        dest.writeString(this.Commentaire);
        dest.writeString(this.Condition);
        dest.writeString(this.ConditionValue);

    }

    @Override
    public String toString() {
        return "Badge{" +
                "Titre='" + Titre + '\'' +
                ", ID='" + Id + '\'' +
                ", Objective='" + Objective + '\'' +
                ", Progress='" + Progress + '\'' +
                ", LogoWithColor='" + LogoWithColor + '\'' +
                ", LogoNoColor='" + LogoNoColor + '\'' +
                ", LogoNoColor='" + Commentaire + '\'' +
                ", Condition='" + Condition + '\'' +
                ", Condition='" + ConditionValue + '\'' +
                '}';
    }
    public static ArrayList<BadgeObject> getBadges(){
        ArrayList<BadgeObject> badges = new ArrayList<BadgeObject>();
        badges.add(
                new BadgeObject("Arts décoratifs","Categorie","Arts décoratifs","0",null,null,null,0,3));
        badges.add(
                new BadgeObject("Peinture","SousCategorie","Peinture","1",null,null,null,0,3));
        badges.add(
                new BadgeObject("Sculpture","SousCategorie","Sculpture","2",null,null,null,0,5));
        badges.add(
                new BadgeObject("Côte-des-Neiges-Notre-Dame-de-Grâce","Quartier","Côte-des-Neiges-Notre-Dame-de-Grâce","3",null,null,null,0,3));
        badges.add(
                new BadgeObject("Université de Montreal","Quartier","Universite de Montreal (Rectorat)","4",null,null,null,0,5));
        badges.add(
                new BadgeObject("Louis-Collin","Id","10004","5",null,null,"Louis-Collin",0,1));
        return badges;
    }
}
