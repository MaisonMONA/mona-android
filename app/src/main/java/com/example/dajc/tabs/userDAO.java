package com.example.dajc.tabs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface userDAO {
    // Adds a person to the database
    @Insert
    void insertAll(userObject... user);
    @Query("SELECT * FROM user")
    List<userObject> getUser();
    @Query("SELECT count(USER) FROM user")
    int numberofuser();

}
