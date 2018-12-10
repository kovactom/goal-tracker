package com.vsb.tamz.goaltracker.persistence.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = Goal.class, parentColumns = "id", childColumns = "goal_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class GoalProgress {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "done_date")
    private Date date;

    @ColumnInfo(name = "goal_id")
    private long goalId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getGoalId() {
        return goalId;
    }

    public void setGoalId(long goalId) {
        this.goalId = goalId;
    }
}
