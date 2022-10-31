package com.healthtracker.home.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.healthtracker.home.entity.WaterCountEntity;

@Dao
public interface WaterCountDao {
    @Insert
    void insert(WaterCountEntity waterCountEntity);

    @Update
    void update(WaterCountEntity waterCountEntity);

    @Delete
    void delete(WaterCountEntity waterCountEntity);
}
