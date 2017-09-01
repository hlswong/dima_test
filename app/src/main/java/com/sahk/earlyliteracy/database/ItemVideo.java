package com.sahk.earlyliteracy.database;

public class ItemVideo implements java.io.Serializable {

    private int themeID;
    private String themeVideo;
    private String songVideo;
    private String jQAndAVideo;
    private String sQAndAVideo;
    private int videoVersion;
    private int hasUpdate;

    public ItemVideo() {
    }

    public ItemVideo(int themeID, String themeVideo, String songVideo, String jQAndAVideo, String sQAndAVideo, int videoVersion, int hasUpdate) {
        this.themeID = themeID;
        this.themeVideo = themeVideo;
        this.songVideo = songVideo;
        this.jQAndAVideo = jQAndAVideo;
        this.sQAndAVideo = sQAndAVideo;
        this.videoVersion = videoVersion;
        this.hasUpdate = hasUpdate;
    }

    public int getThemeID() {
        return themeID;
    }

    public void setThemeID(int themeID) {
        this.themeID = themeID;
    }

    public String getThemeVideo() {
        return themeVideo;
    }

    public void setThemeVideo(String themeVideo) {
        this.themeVideo = themeVideo;
    }

    public String getSongVideo() {
        return songVideo;
    }

    public void setSongVideo(String songVideo) {
        this.songVideo = songVideo;
    }

    public String getJQAndAVideo() {
        return jQAndAVideo;
    }

    public void setJQAndAVideo(String jQAndAVideo) {
        this.jQAndAVideo = jQAndAVideo;
    }

    public String getSQAndAVideo() {
        return sQAndAVideo;
    }

    public void setSQAndAVideo(String sQAndAVideo) {
        this.sQAndAVideo = sQAndAVideo;
    }

    public int getVideoVersion() {
        return videoVersion;
    }

    public void setVideoVersion(int videoVersion) {
        this.videoVersion = videoVersion;
    }

    public int getHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(int hasUpdate) {
        this.hasUpdate = hasUpdate;
    }
}