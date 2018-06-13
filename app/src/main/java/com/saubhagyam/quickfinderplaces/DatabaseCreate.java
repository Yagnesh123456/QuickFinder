package com.saubhagyam.quickfinderplaces;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


public class DatabaseCreate {

    @Database(entities = {DatabasePojo.class}, version = 1)
    public abstract static class AppDatabase extends RoomDatabase {

        private static AppDatabase INSTANCE;

        public static AppDatabase getAppDatabase(Context context) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "MyPlacesDetails1")
                                .allowMainThreadQueries()
                                .build();
            }
            return INSTANCE;
        }

        public static void destroyInstance() {
            INSTANCE = null;
        }

        public abstract Queries userDao();
    }
}
