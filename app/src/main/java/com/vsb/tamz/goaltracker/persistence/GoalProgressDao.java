package com.vsb.tamz.goaltracker.persistence;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import com.vsb.tamz.goaltracker.persistence.model.GoalProgress;

import java.util.List;

@Dao
public interface GoalProgressDao {
    @Insert
    void insert(GoalProgress goalProgress);

    @Update
    void update(GoalProgress goalProgress);

    @Delete
    void delete(GoalProgress goalProgress);

    @Query("SELECT * FROM GoalProgress WHERE id = :id")
    GoalProgress findOneById(long id);

    @Query("SELECT * FROM GoalProgress WHERE goal_id = :goalId")
    List<GoalProgress> findAllByGoalId(long goalId);

    @Query("SELECT * FROM GoalProgress")
    List<GoalProgress> findAll();

    @Query("SELECT COUNT(*) FROM GoalProgress WHERE goal_id = :goalId")
    long getCountByGoalId(long goalId);

    @Query("SELECT COUNT(*) FROM GoalProgress")
    long getGoalProgressCount();
}
