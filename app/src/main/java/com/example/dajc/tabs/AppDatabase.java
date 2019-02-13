package com.example.dajc.tabs;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by emile on 2018-03-01.
 */

@Database(entities = {OeuvreObject.class , BadgeObject.class, userObject.class/*, AcquisitionObject.class*/ }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract OeuvreDao getOeuvreDao();
    public abstract BadgeDao getBadgeDao();
    public abstract userDAO getUserDao();
    //public abstract AcquisitionDao getAcquisitionDao();
}
