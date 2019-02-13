package com.example.dajc.tabs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
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
