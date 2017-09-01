package com.sahk.earlyliteracy.database;

/**
 * Created by rex.leung on 25/1/2017.
 */

public class ItemGA implements java.io.Serializable {

    private int gaID;
    private String category;
    private String action;
    private String label;
    private int value;

    public ItemGA(int gaID, String category, String action, String label, int value) {
        this.gaID = gaID;
        this.category = category;
        this.action = action;
        this.label = label;
        this.value = value;
    }

    public ItemGA() {
    }

    public int getGaID() {
        return gaID;
    }

    public void setGaID(int gaID) {
        this.gaID = gaID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
