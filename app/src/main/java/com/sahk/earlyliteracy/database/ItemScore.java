package com.sahk.earlyliteracy.database;

public class ItemScore implements java.io.Serializable {

    private int themeID;
    private int score;

    public ItemScore() {
    }

    public ItemScore(int themeID, int score) {
        this.themeID = themeID;
        this.score = score;
    }

    public int getThemeID() {
        return themeID;
    }

    public void setThemeID(int themeID) {
        this.themeID = themeID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}