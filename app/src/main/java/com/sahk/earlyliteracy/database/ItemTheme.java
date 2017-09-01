package com.sahk.earlyliteracy.database;

public class ItemTheme implements java.io.Serializable {

    private int themeID;
    private int gameID;
    private String gameName;
    private String themePath;
    private int finished;

    public ItemTheme() {
    }

    public ItemTheme(int themeID, int gameID, String gameName, String themePath, int finished) {
        this.themeID = themeID;
        this.gameID = gameID;
        this.gameName = gameName;
        this.themePath = themePath;
        this.finished = finished;
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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getThemePath() {
        return themePath;
    }

    public void setThemePath(String themePath) {
        this.themePath = themePath;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}