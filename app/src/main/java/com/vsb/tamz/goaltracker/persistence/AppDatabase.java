package com.vsb.tamz.goaltracker.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.vsb.tamz.goaltracker.persistence.converters.Converters;
import com.vsb.tamz.goaltracker.persistence.model.Goal;
import com.vsb.tamz.goaltracker.persistence.model.GoalProgress;

@Database(entities = {Goal.class, GoalProgress.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract GoalDao goalDao();
    public abstract GoalProgressDao goalProgressDao();
}
