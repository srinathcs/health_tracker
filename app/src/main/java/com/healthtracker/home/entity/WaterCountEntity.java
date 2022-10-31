package com.healthtracker.home.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "water_count")
public class WaterCountEntity implements Serializable {

    @ColumnInfo(name = "count")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
    @PrimaryKey
    @ColumnInfo(name = "date_time")
    private long dateTime;

}
