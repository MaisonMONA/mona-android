/*package com.example.dajc.tabs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.graphics.Picture;

import java.util.List;

@Dao
public interface PictureDao {
    // Adds a person to the database
    @Insert
    void insertAll(PictureObject... picture);
    @Query("SELECT * FROM picture")
    List<PictureObject> getPictures();
    @Query("SELECT image FROM picture where Id=:Id")
    List<PictureObject> getPicture(int Id);

}
*/