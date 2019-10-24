package com.example.dajc.tabs;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * Created by emile on 2018-02-06.
 */
@Entity(tableName = "user")
public class userObject implements Parcelable {
    @PrimaryKey
    @NonNull
    public String USER;
    @NonNull
    public String PW;
    //public ArrayList<OeuvreObject> Oeuvres;

    public String getUser() {
        return USER;
    }
    public String getPw() {
        return PW;
    }

    public userObject() {}
    public userObject(String user, String pw) {
        USER = user;
        PW=pw;
    }
    public userObject(Parcel in) {
        USER = in.readString();
        PW = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(USER);
        dest.writeString(PW);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<userObject> CREATOR = new Parcelable.Creator<userObject>() {
        @Override
        public userObject createFromParcel(Parcel in) {
            return new userObject(in);
        }

        @Override
        public userObject[] newArray(int size) {
            return new userObject[size];
        }
    };
}
