package com.vsb.tamz.goaltracker;

public class GoalCard {
    private long id;
    private String name;
    private String repeat;
    private String duration;
    private String score;
    private boolean active;

    public GoalCard(long id, String name, String repeat, String duration, String score, boolean active) {
        this.id = id;
        this.name = name;
        this.repeat = repeat;
        this.duration = duration;
        this.score = score;
        this.active = active;
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

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
