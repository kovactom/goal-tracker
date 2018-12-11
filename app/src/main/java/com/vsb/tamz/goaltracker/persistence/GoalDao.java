package com.vsb.tamz.goaltracker.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.vsb.tamz.goaltracker.persistence.model.Goal;

import java.util.List;

@Dao
public interface GoalDao {
    @Insert
    void insert(Goal goal);

    @Update
    void update(Goal goal);

    @Delete
    void delete(Goal goal);

    @Query("SELECT * FROM goal WHERE id = :id")
    Goal findOneById(long id);

    @Query("SELECT * FROM goal")
    List<Goal> findAll();

    @Query("SELECT COUNT(*) FROM goal")
    long getGoalCount();
}
