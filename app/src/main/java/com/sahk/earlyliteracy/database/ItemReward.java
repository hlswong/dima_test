package com.sahk.earlyliteracy.database;

public class ItemReward implements java.io.Serializable {

    private int rewardId;
    private String rewardType;
    private String rewardContent;
    private int redeemed;
    private int themeID; //Add By Rex

    ItemReward() {
    }

    public ItemReward(int rewardId, String rewardType, String rewardContent, int redeemed, int themeID) {
        this.rewardId = rewardId;
        this.rewardType = rewardType;
        this.rewardContent = rewardContent;
        this.redeemed = redeemed;
        this.themeID = themeID; //Add By Rex
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardContent() {
        return rewardContent;
    }

    public void setRewardContent(String rewardContent) {
        this.rewardContent = rewardContent;
    }

    public boolean getRedeemed() {
        return redeemed != 0;
    }

    public void setRedeemed(int redeemed) {
        this.redeemed = redeemed;
    }

    public int getThemeID() {
        return themeID;
    } //Add By Rex

    public void setThemeID(int themeID) {
        this.themeID = themeID;
    } //Add By Rex
}