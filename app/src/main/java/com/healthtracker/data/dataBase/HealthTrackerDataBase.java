package com.healthtracker.data.dataBase;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;

import androidx.room.Database;

import com.healthtracker.home.dao.WaterCountDao;
import com.healthtracker.home.entity.WaterCountEntity;

@Database(entities = {WaterCountEntity.class}, version = 1, exportSchema = false)
public abstract class HealthTrackerDataBase extends RoomDatabase {
    private static HealthTrackerDataBase healthTrackerDataBase;
    private static String DATABASE_NAME = "health_tracker";

    public synchronized static HealthTrackerDataBase getInstance(Context context) {
        if (healthTrackerDataBase == null) {
            healthTrackerDataBase = Room.databaseBuilder(context.getApplicationContext(), HealthTrackerDataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return healthTrackerDataBase;

    }

    public abstract WaterCountDao waterCountDao();
}