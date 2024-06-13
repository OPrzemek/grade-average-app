package com.example.projekt;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Grade {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "value")
    public double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
