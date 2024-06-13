package com.example.projekt;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GradeDao {
    @Insert
    void insertAll(Grade... grades);

    @Query("SELECT * FROM grade")
    List<Grade> getAll();

    @Delete
    void delete(Grade grade);
}
