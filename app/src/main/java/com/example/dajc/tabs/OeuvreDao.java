package com.example.dajc.tabs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.dajc.tabs.WebAPI.Oeuvre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emile on 2018-03-01.
 */
@Dao
public interface OeuvreDao {
    // Adds a person to the database
    @Insert
    void insertAll(OeuvreObject... oeuvre);

    // Removes a person from the database
    @Delete
    void delete(OeuvreObject oeuvre);

    // Gets all people in the database
    @Query("SELECT * FROM oeuvre")
    List<OeuvreObject> getAllOeuvre();
    @Query("SELECT * FROM oeuvre WHERE Etat=1")
    List<OeuvreObject> getFavOeuvre();
    @Query("SELECT * FROM oeuvre WHERE :ID = Id ")
    List<OeuvreObject> verifyID (String ID);
    // Gets all people in the database with a favorite color
    @Query("SELECT * FROM oeuvre WHERE Quartier LIKE :quartier")
    List<OeuvreObject> getAllOeuvreQuartier(String quartier);
    @Query("SELECT * FROM oeuvre WHERE URI!='' ")
    List<OeuvreObject> getGalleryList();
    @Update
    public void updateUsers(OeuvreObject... oeuvre);
}
