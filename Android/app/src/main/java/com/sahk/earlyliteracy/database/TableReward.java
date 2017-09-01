package com.sahk.earlyliteracy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.sahk.earlyliteracy.utils.DatabaseUtil;

public class TableReward {

    public static final String TABLE_NAME = "tableReward";
    private static final String KEY_ID = "_id";
    private static final String COLUMN_REWARDID = "rewardId";
    private static final String COLUMN_REWARDTYPE = "rewardType";
    private static final String COLUMN_REWARDCONTENT = "rewardContent";
    private static final String COLUMN_REDEEMED = "redeemed";
    private static final String COLUMN_THEMEID = "themeId";  //Add By Rex
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_REWARDID + " INTEGER, " +
                    COLUMN_REWARDTYPE + " TEXT, " +
                    COLUMN_REWARDCONTENT + " TEXT, " +
                    COLUMN_REDEEMED + " INTEGER," +
                    COLUMN_THEMEID + " INTEGER)";

    private SQLiteDatabase sqLiteDatabase;

    public TableReward(Context context) {
        sqLiteDatabase = DatabaseUtil.getDatabase(context);
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public boolean insert(ItemReward itemReward) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_REWARDID, itemReward.getRewardId());
        contentValues.put(COLUMN_REWARDTYPE, itemReward.getRewardType());
        contentValues.put(COLUMN_REWARDCONTENT, itemReward.getRewardContent());
        contentValues.put(COLUMN_REDEEMED, itemReward.getRedeemed());
        contentValues.put(COLUMN_THEMEID, itemReward.getThemeID()); //Add By Rex
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) > 0;
    }

    public boolean update(ItemReward itemReward) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_REWARDID, itemReward.getRewardId());
        contentValues.put(COLUMN_REWARDTYPE, itemReward.getRewardType());
        contentValues.put(COLUMN_REWARDCONTENT, itemReward.getRewardContent());
        contentValues.put(COLUMN_REDEEMED, itemReward.getRedeemed());
        contentValues.put(COLUMN_THEMEID, itemReward.getThemeID()); //Add By Rex
        String where = KEY_ID + "=" + itemReward.getRewardId();
        return sqLiteDatabase.update(TABLE_NAME, contentValues, where, null) > 0;
    }

    public boolean delete(int id) {
        String where = KEY_ID + "=" + id;
        return sqLiteDatabase.delete(TABLE_NAME, where, null) > 0;
    }

    //Edited By Rex
    public boolean isRedeemed(int id , int themeId ) {
//Commented By Rex
//    public boolean isRedeemed(int id ) {
//        String where = COLUMN_REWARDID + " = " + id + " AND " + COLUMN_REDEEMED + " = 1";

        //Added By Rex
        String where = COLUMN_REWARDID + " = " + id + " AND " + COLUMN_REDEEMED + " = 1 AND " + COLUMN_THEMEID + " = " + themeId ;
        int count;
        Cursor result = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        count = result.getCount();
        result.close();
        return count != 0;
    }

    public List<ItemReward> getAll() {
        List<ItemReward> result = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }
        cursor.close();
        return result;
    }

    public ItemReward get(int id) {
        ItemReward itemReward = null;
        String where = KEY_ID + "=" + id;
        Cursor result = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (result.moveToFirst()) {
            itemReward = getRecord(result);
        }
        result.close();
        return itemReward;
    }

    public ItemReward getRecord(Cursor cursor) {
        ItemReward itemReward = new ItemReward();
        itemReward.setRewardId(cursor.getInt(1));
        itemReward.setRewardType(cursor.getString(2));
        itemReward.setRewardContent(cursor.getString(3));
        itemReward.setRedeemed(cursor.getInt(4));
        itemReward.setThemeID(cursor.getInt(5)); //Add by Rex
        return itemReward;
    }

    public int getCount() {
        int result = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }

    public void initReward() {
        //Commented By Rex

//        insert(new ItemReward(1, "egg", "egg1", 0));
//        insert(new ItemReward(2, "egg", "egg2", 0));
//        insert(new ItemReward(3, "egg", "egg3", 0));
//        insert(new ItemReward(4, "egg", "egg4", 0));
//        insert(new ItemReward(5, "monster", "monster1", 0));
//        insert(new ItemReward(6, "monster", "monster2", 0));
//        insert(new ItemReward(7, "monster", "monster3", 0));
//        insert(new ItemReward(8, "monster", "monster4", 0));
//        insert(new ItemReward(9, "item", "grass", 0));
//        insert(new ItemReward(10, "item", "lamp", 0));
//        insert(new ItemReward(11, "item", "spray", 0));
//        insert(new ItemReward(12, "item", "hand", 0));
//        insert(new ItemReward(13, "item", "teats", 0));
//        insert(new ItemReward(14, "item", "bag", 0));
//        insert(new ItemReward(15, "item", "skateboard", 0));
//        insert(new ItemReward(16, "item", "guitar", 0));
    }
}
