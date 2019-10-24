package com.example.dajc.tabs;


import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by emile on 2018-02-06.
 */
@Entity(tableName = "badge")
public class BadgeObject implements Parcelable {

    public String Titre;
    @PrimaryKey
    @NonNull
    public String Id;
    public String Name;
    public int Progress;
    public int Objective;
    public int LogoNoColor;
    public int LogoWithColor;
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
    public int getLogoNoColor() {
        return LogoNoColor;
    }
    public int getLogoWithColor() {
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

    public BadgeObject(String titre, String condition, String conditionval, String id, int logoWithColor, int logoNoColor, String commentaire, int progress, int objective) {
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
        this.LogoWithColor = in.readInt();
        this.LogoNoColor = in.readInt();
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
        dest.writeInt(this.LogoWithColor);
        dest.writeInt(this.LogoNoColor);
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
        //Resources res = getResources();
        badges.add(
                new BadgeObject("Université de Montréal","Quartier","Universite de Montreal (Rectorat)","0",R.drawable.udm_color,R.drawable.udm,null,0,0));
        badges.add(
                new BadgeObject("Hochelaga","Quartier","Hochelaga","1",R.drawable.hoch_color,R.drawable.hoch,null,0,3));
        badges.add(
                new BadgeObject("Mont-Royal","Quartier","Mont-Royal","2",R.drawable.mr_color,R.drawable.mr,null,0,5));
        badges.add(
                new BadgeObject("Côte-des-Neiges-Notre-Dame-de-Grâce","Quartier","Côte-des-Neiges-Notre-Dame-de-Grâce","3", R.drawable.cdn_color , R.drawable.cdn,null,0,0));
        badges.add(
                new BadgeObject("Ahuntsic-Cartierville","Quartier","Ahuntsic-Cartierville","4",R.drawable.ac_color,R.drawable.ac,null,0,5));
        badges.add(
                new BadgeObject("Villeray","Quartier","Villeray","5",R.drawable.vry_color,R.drawable.vry,null,0,1));
        badges.add(
                new BadgeObject("Lachine","Quartier","Lachine","6",R.drawable.lach_color,R.drawable.lach,null,0,1));
        badges.add(
                new BadgeObject("Sud-Ouest","Quartier","Sud-Ouest","7",R.drawable.so_color,R.drawable.so,null,0,3));
        return badges;

    }
}
