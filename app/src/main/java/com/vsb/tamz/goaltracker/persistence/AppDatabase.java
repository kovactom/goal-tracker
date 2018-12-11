package com.vsb.tamz.goaltracker.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.vsb.tamz.goaltracker.persistence.converters.Converters;
import com.vsb.tamz.goaltracker.persistence.model.Goal;
import com.vsb.tamz.goaltracker.persistence.model.GoalNotification;
import com.vsb.tamz.goaltracker.persistence.model.GoalProgress;

@Database(entities = {Goal.class, GoalProgress.class, GoalNotification.class}, version = 7)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "goals")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract GoalDao goalDao();
    public abstract GoalProgressDao goalProgressDao();
    public abstract GoalNotificationDao goalNotificationDao();
}
