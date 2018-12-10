package com.vsb.tamz.goaltracker.persistence.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
public class Goal {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "category_id")
    private long categoryId;

    @ColumnInfo(name = "date_from")
    private Date from;

    @ColumnInfo(name = "date_to")
    private Date to;

    @ColumnInfo(name = "time")
    private Date time;

    @ColumnInfo(name = "repeat_id")
    private long repeatId;

    @ColumnInfo(name = "notification_id")
    private long notificationId;

    @ColumnInfo(name = "duration")
    private long duration;

    @ColumnInfo(name = "picture")
    private String picture;

    public long getScore(long doneUnits) {
        return doneUnits * duration;
    }

    public long getMaximumScore() {
        long unitsBetween = getUnitsBetweenDates();
        return unitsBetween * duration;
    }

    public long getUnitsBetweenDates() {
        long diff = to.getTime() - from.getTime();
        switch ((int) repeatId) {
            case 0: {
                return diff / (1000L * 60 * 60 * 24 );
            }
            case 1: {
                return diff / (1000L * 60 * 60 * 24 * 7);
            }
            case 2: {
                return diff / (1000L * 60 * 60 * 24 * 30);
            }
        }
        return 0;
    }

    public boolean isTodayGoal() {
        GregorianCalendar targetDate = new GregorianCalendar();
        GregorianCalendar currentDate = new GregorianCalendar();
        targetDate.setTime(from);
        switch ((int) repeatId) {
            case 0: {
                return currentDate.getTimeInMillis() >= from.getTime() && currentDate.getTimeInMillis() <= to.getTime();
            }
            case 1: {
                return targetDate.get(Calendar.DAY_OF_WEEK) == currentDate.get(Calendar.DAY_OF_WEEK);
            }
            case 2: {
                return targetDate.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH);
            }
        }

        return false;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(long repeatId) {
        this.repeatId = repeatId;
    }

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
