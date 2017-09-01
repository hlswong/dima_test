package com.sahk.earlyliteracy.database;

public class ItemGamePlay implements java.io.Serializable {

    private int themeID;
    private int gameID;
    private int stageID;
    private int playTime;
    private int score;

    public ItemGamePlay() {
    }

    public ItemGamePlay(int themeID, int gameID, int stageID, int playTime, int score) {
        this.themeID = themeID;
        this.gameID = gameID;
        this.stageID = stageID;
        this.playTime = playTime;
        this.score = score;
    }


    public int getThemeID() {
        return themeID;
    }

    public void setThemeID(int themeID) {
        this.themeID = themeID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getStageID() {
        return stageID;
    }

    public void setStageID(int stageID) {
        this.stageID = stageID;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}