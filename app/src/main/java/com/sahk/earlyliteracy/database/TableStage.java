package com.sahk.earlyliteracy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.sahk.earlyliteracy.utils.DatabaseUtil;

public class TableStage {

    public static final String TABLE_NAME = "tableStage";
    public static final String COLUMN_STAGEID = "stageID";
    public static final String COLUMN_THEMEID = "themeID";
    public static final String COLUMN_GAMEID = "gameID";
    public static final String COLUMN_FULLMARK = "fullMark";
    public static final String COLUMN_PASSINGMARK = "passingMark";
    private static final String KEY_ID = "_id";
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STAGEID + " INTEGER, " +
                    COLUMN_THEMEID + " INTEGER, " +
                    COLUMN_GAMEID + " INTEGER, " +
                    COLUMN_FULLMARK + " INTEGER, " +
                    COLUMN_PASSINGMARK + " INTEGER)";

    private SQLiteDatabase sqLiteDatabase;

    public TableStage(Context context) {
        sqLiteDatabase = DatabaseUtil.getDatabase(context);
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public boolean insert(ItemStage itemStage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STAGEID, itemStage.getStageID());
        contentValues.put(COLUMN_THEMEID, itemStage.getThemeID());
        contentValues.put(COLUMN_GAMEID, itemStage.getGameID());
        contentValues.put(COLUMN_FULLMARK, itemStage.getStageID());
        contentValues.put(COLUMN_PASSINGMARK, itemStage.getPassingMark());
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) > 0;
    }

    public boolean update(ItemStage itemStage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STAGEID, itemStage.getStageID());
        contentValues.put(COLUMN_THEMEID, itemStage.getThemeID());
        contentValues.put(COLUMN_GAMEID, itemStage.getGameID());
        contentValues.put(COLUMN_FULLMARK, itemStage.getFullMark());
        contentValues.put(COLUMN_PASSINGMARK, itemStage.getPassingMark());
        String where = COLUMN_STAGEID + "=" + itemStage.getStageID() + " AND " + COLUMN_THEMEID + "=" + itemStage.getThemeID() + " AND " + COLUMN_GAMEID + "=" + itemStage.getGameID();
        return sqLiteDatabase.update(TABLE_NAME, contentValues, where, null) > 0;
    }

    public boolean deleteByGameId(int gameId) {
        String where =  COLUMN_GAMEID + "=" + gameId;
        return sqLiteDatabase.delete(TABLE_NAME, where, null) > 0;
    }

    public List<ItemStage> getAll() {
        List<ItemStage> stageList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            stageList.add(getRecord(cursor));
        }
        cursor.close();
        return stageList;
    }

    public ItemStage get(int id) {
        ItemStage itemStage = new ItemStage();
        String where = COLUMN_GAMEID + "=" + id;
        Cursor result = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (result.moveToFirst()) {
            itemStage = getRecord(result);
        }
        result.close();
        return itemStage;
    }

    //Added By Rex
    public ItemStage get(int themeid, int gameid , int stageid) {
        ItemStage itemStage = new ItemStage();
        String where = COLUMN_THEMEID + "=" + themeid + " and " + COLUMN_GAMEID + "=" + gameid + " and " + COLUMN_STAGEID + "=" + stageid ;
        Cursor result = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (result.moveToFirst()) {
            itemStage = getRecord(result);
        }
        result.close();
        return itemStage;
    }

    public ItemStage getRecord(Cursor cursor) {
        ItemStage itemStage = new ItemStage();
        itemStage.setStageID(cursor.getInt(1));
        itemStage.setThemeID(cursor.getInt(2));
        itemStage.setGameID(cursor.getInt(3));
        itemStage.setFullMark(cursor.getInt(4));
        itemStage.setPassingMark(cursor.getInt(5));
        return itemStage;
    }

    public boolean isStageDataDownloaded(int stageID, int themeID, int gameID) {
        int result = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE stageID = " + stageID + " AND themeID = " + themeID + " AND gameID = " + gameID, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result > 0;
    }
}
