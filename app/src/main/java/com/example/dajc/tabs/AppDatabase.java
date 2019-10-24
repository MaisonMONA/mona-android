package com.example.dajc.tabs;


import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Created by emile on 2018-03-01.
 */

@Database(entities = {OeuvreObject.class, BadgeObject.class, userObject.class/*, AcquisitionObject.class*/ }, version = 2, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract OeuvreDao getOeuvreDao();
    public abstract BadgeDao getBadgeDao();
    public abstract userDAO getUserDao();
    //public abstract AcquisitionDao getAcquisitionDao();
}
