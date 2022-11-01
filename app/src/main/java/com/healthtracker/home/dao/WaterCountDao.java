package com.healthtracker.home.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.healthtracker.home.entity.WaterCountEntity;

import java.util.List;

@Dao
public interface WaterCountDao {

    @Insert
    void insert(WaterCountEntity waterCountEntity);

    @Update
    void update(WaterCountEntity waterCountEntity);

    @Delete
    void delete(WaterCountEntity waterCountEntity);

    @Query("SELECT * FROM water_count")
    List<WaterCountEntity> getAll();
}
