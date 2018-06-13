package com.saubhagyam.quickfinderplaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao

public interface Queries {

    @Query("SELECT * FROM mytable")
    List<DatabasePojo> getAll();

    @Insert
    void insertdata(DatabasePojo data);

    @Delete
    void delete(DatabasePojo data);
}
