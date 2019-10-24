package com.example.dajc.tabs;

import androidx.room.*;

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
