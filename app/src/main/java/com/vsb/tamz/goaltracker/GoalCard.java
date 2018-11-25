package com.vsb.tamz.goaltracker;

public class GoalCard {
    private String name;
    private String repeat;
    private String duration;
    private String score;

    public GoalCard(String name, String repeat, String duration, String score) {
        this.name = name;
        this.repeat = repeat;
        this.duration = duration;
        this.score = score;
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
}
