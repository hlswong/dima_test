package com.sahk.earlyliteracy.database;

public class ItemGame implements java.io.Serializable {

    private int gameID;
    private String gameType;
    private int gameVersion;
    private String downloadPath;
    private String gamePath;
    private int basic;
    private int hasUpdate;
    private int order;

    public ItemGame() {
    }

    public ItemGame(int gameID, String gameType, int gameVersion, String downloadPath, String gamePath, int basic, int hasUpdate, int order) {
        this.gameID = gameID;
        this.gameType = gameType;
        this.gameVersion = gameVersion;
        this.downloadPath = downloadPath;
        this.gamePath = gamePath;
        this.basic = basic;
        this.hasUpdate = hasUpdate;
        this.order = order;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public int getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(int gameVersion) {
        this.gameVersion = gameVersion;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getGamePath() {
        return gamePath;
    }

    public void setGamePath(String gamePath) {
        this.gamePath = gamePath;
    }

    public int getBasic() {
        return basic;
    }

    public void setBasic(int basic) {
        this.basic = basic;
    }

    public int getHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(int hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}