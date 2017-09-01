package com.sahk.earlyliteracy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sahk.earlyliteracy.utils.DatabaseUtil;

public class TableGamePlay {

    public static final String TABLE_NAME = "tableGamePlay";
    private static final String KEY_ID = "_id";
    private static final String COLUMN_THEMEID = "themeID";
    private static final String COLUMN_GAMEID = "gameID";
    private static final String COLUMN_STAGEID = "stageID";
    private static final String COLUMN_PLAYTIME = "playTime";
    private static final String COLUMN_SCORE = "score";
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_THEMEID + " INTEGER, " +
                    COLUMN_GAMEID + " INTEGER, " +
                    COLUMN_STAGEID + " INTEGER, " +
                    COLUMN_PLAYTIME + " INTEGER, " +
                    COLUMN_SCORE + " INTEGER)";

    private SQLiteDatabase sqLiteDatabase;

    public TableGamePlay(Context context) {
        sqLiteDatabase = DatabaseUtil.getDatabase(context);
    }

    public boolean insert(ItemGamePlay itemGamePlay) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_THEMEID, itemGamePlay.getThemeID());
        contentValues.put(COLUMN_GAMEID, itemGamePlay.getGameID());
        contentValues.put(COLUMN_STAGEID, itemGamePlay.getStageID());
        contentValues.put(COLUMN_PLAYTIME, itemGamePlay.getPlayTime());
        contentValues.put(COLUMN_SCORE, itemGamePlay.getScore());
        Log.d("database","insert " + TABLE_NAME + " themeId: "+ itemGamePlay.getThemeID() + " gameId: " + itemGamePlay.getGameID() + " score:" + itemGamePlay.getScore());
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) > 0;
    }

    public boolean update(ItemGamePlay itemGamePlay) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_THEMEID, itemGamePlay.getThemeID());
        contentValues.put(COLUMN_GAMEID, itemGamePlay.getGameID());
        contentValues.put(COLUMN_STAGEID, itemGamePlay.getStageID());
        contentValues.put(COLUMN_PLAYTIME, itemGamePlay.getPlayTime());
        contentValues.put(COLUMN_SCORE, itemGamePlay.getScore());
        String where = COLUMN_GAMEID + "=" + itemGamePlay.getGameID();
        Log.d("database","update " + TABLE_NAME + " themeId: "+ itemGamePlay.getThemeID() + " gameId: " + itemGamePlay.getGameID() + " score:" + itemGamePlay.getScore());
        return sqLiteDatabase.update(TABLE_NAME, contentValues, where, null) > 0;
    }
    public boolean deleteByGameId(int gameId) {
        String where = COLUMN_GAMEID + "=" + gameId;
        return sqLiteDatabase.delete(TABLE_NAME,  where, null) > 0;
    }

    public boolean updateRecord(ItemGamePlay itemGamePlay) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_THEMEID, itemGamePlay.getThemeID());
        contentValues.put(COLUMN_GAMEID, itemGamePlay.getGameID());
        contentValues.put(COLUMN_STAGEID, itemGamePlay.getStageID());
        contentValues.put(COLUMN_PLAYTIME, itemGamePlay.getPlayTime());
        contentValues.put(COLUMN_SCORE, itemGamePlay.getScore());
        String where = COLUMN_GAMEID + "=" + itemGamePlay.getGameID() + " AND " + COLUMN_THEMEID + "=" + itemGamePlay.getThemeID();
        return sqLiteDatabase.update(TABLE_NAME, contentValues, where, null) > 0;
    }

    public ItemGamePlay get(int themeID, int gameID) {
        ItemGamePlay itemGamePlay = null;
        String where = COLUMN_THEMEID + "=" + themeID + " AND " + COLUMN_GAMEID + "=" + gameID;
        Cursor result = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (result.moveToFirst()) {
            itemGamePlay = getRecord(result);
        }
        result.close();
        return itemGamePlay;
    }

    private ItemGamePlay getRecord(Cursor cursor) {
        ItemGamePlay itemGamePlay = new ItemGamePlay();
        itemGamePlay.setThemeID(cursor.getInt(1));
        itemGamePlay.setGameID(cursor.getInt(2));
        itemGamePlay.setStageID(cursor.getInt(3));
        itemGamePlay.setPlayTime(cursor.getInt(4));
        itemGamePlay.setScore(cursor.getInt(5));
        return itemGamePlay;
    }

    public boolean isNewComer(int themeID, int gameID) {
        String where = COLUMN_THEMEID + "=" + themeID + " AND " + COLUMN_GAMEID + "=" + gameID;
        Cursor result = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        int newComer = result.getCount();
        result.close();
        return newComer == 0;
    }

    public int getMark(int themeID) {
        int result = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(" + COLUMN_SCORE + ") FROM " + TABLE_NAME + " WHERE themeID=" + themeID, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }
}
