package com.example.dajc.tabs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BadgeDao {
    // Adds a person to the database
    @Insert
    void insertAll(BadgeObject... badge);
    @Query("SELECT * FROM badge")
    List<BadgeObject> getAllBadges();
    @Query("SELECT * FROM badge WHERE :ID = Id ")
    List<BadgeObject> verifyID (String ID);

    @Update
    public void updatebadge(BadgeObject... badge);

}
