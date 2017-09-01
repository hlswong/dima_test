package com.sahk.earlyliteracy.database;

public class ItemStage implements java.io.Serializable {

    private int stageID;
    private int themeID;
    private int gameID;
    private int fullMark;
    private int passingMark;

    public ItemStage() {
    }

    public ItemStage(int stageID, int themeID, int gameID, int fullMark, int passingMark) {
        this.stageID = stageID;
        this.themeID = themeID;
        this.gameID = gameID;
        this.fullMark = fullMark;
        this.passingMark = passingMark;
    }

    public int getStageID() {
        return stageID;
    }

    public void setStageID(int stageID) {
        this.stageID = stageID;
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

    public int getFullMark() {
        return fullMark;
    }

    public void setFullMark(int fullMark) {
        this.fullMark = fullMark;
    }

    public int getPassingMark() {
        return passingMark;
    }

    public void setPassingMark(int passingMark) {
        this.passingMark = passingMark;
    }
}