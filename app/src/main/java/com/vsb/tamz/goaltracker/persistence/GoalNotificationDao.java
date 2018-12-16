package com.vsb.tamz.goaltracker.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.vsb.tamz.goaltracker.persistence.model.GoalNotification;

import java.util.List;

@Dao
public interface GoalNotificationDao {

    @Insert
    long insert(GoalNotification goalNotification);

    @Update
    void update(GoalNotification goalNotification);

    @Delete
    void delete(GoalNotification goalNotification);

    @Query("SELECT * FROM GoalNotification WHERE id = :id")
    GoalNotification findOneById(long id);

    @Query("SELECT * FROM GoalNotification")
    List<GoalNotification> findAll();

    @Query("SELECT * FROM GoalNotification WHERE goal_id = :goalId")
    List<GoalNotification> findAllByGoalId(long goalId);

    @Query("DELETE FROM GoalNotification WHERE goal_id = :goalId")
    void deleteByGoalId(long goalId);
}
