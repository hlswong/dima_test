package com.sahk.earlyliteracy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.sahk.earlyliteracy.utils.DatabaseUtil;

public class TableGame {

    public static final String TABLE_NAME = "tableGame";
    public static final String COLUMN_GAMEID = "gameID";
    public static final String COLUMN_GAMETYPE = "gameType";
    public static final String COLUMN_GAMEVERSION = "gameVersion";
    public static final String COLUMN_DOWNLOADPATH = "downloadPath";
    public static final String COLUMN_GAMEPATH = "gamePath";
    public static final String COLUMN_BASIC = "basic";
    public static final String COLUMN_HASUPDATE = "hasUpdate";
    public static final String COLUMN_ORDER = "sort_order";
    private static final String KEY_ID = "_id";
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_GAMEID + " INTEGER, " +
                    COLUMN_GAMETYPE + " TEXT, " +
                    COLUMN_GAMEVERSION + " INTEGER, " +
                    COLUMN_DOWNLOADPATH + " TEXT, " +
                    COLUMN_GAMEPATH + " TEXT, " +
                    COLUMN_BASIC + " INTEGER, " +
                    COLUMN_HASUPDATE + " INTEGER, " +
                    COLUMN_ORDER + " INTEGER)";

    private SQLiteDatabase sqLiteDatabase;

    public TableGame(Context context) {
        sqLiteDatabase = DatabaseUtil.getDatabase(context);
    }

    public boolean insert(ItemGame itemGame) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GAMEID, itemGame.getGameID());
        contentValues.put(COLUMN_GAMETYPE, itemGame.getGameType());
        contentValues.put(COLUMN_GAMEVERSION, itemGame.getGameVersion());
        contentValues.put(COLUMN_DOWNLOADPATH, itemGame.getDownloadPath());
        contentValues.put(COLUMN_GAMEPATH, itemGame.getGamePath());
        contentValues.put(COLUMN_BASIC, itemGame.getBasic());
        contentValues.put(COLUMN_HASUPDATE, itemGame.getHasUpdate());
        contentValues.put(COLUMN_ORDER, itemGame.getOrder());
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) > 0;
    }

    public boolean update(ItemGame itemGame) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GAMEID, itemGame.getGameID());
        contentValues.put(COLUMN_GAMETYPE, itemGame.getGameType());
        contentValues.put(COLUMN_GAMEVERSION, itemGame.getGameVersion());
        contentValues.put(COLUMN_DOWNLOADPATH, itemGame.getDownloadPath());
        contentValues.put(COLUMN_GAMEPATH, itemGame.getGamePath());
        contentValues.put(COLUMN_BASIC, itemGame.getBasic());
        contentValues.put(COLUMN_HASUPDATE, itemGame.getHasUpdate());
        if(itemGame.getOrder()>0) {
            contentValues.put(COLUMN_ORDER, itemGame.getOrder());
        }
        String where = COLUMN_GAMEID + "=" + itemGame.getGameID();
        return sqLiteDatabase.update(TABLE_NAME, contentValues, where, null) > 0;
    }

    public boolean delete(int gameId) {
        String where = COLUMN_GAMEID + "=" + gameId;
        return sqLiteDatabase.delete(TABLE_NAME, where, null) > 0;
    }

    public List<ItemGame> getAll() {
        List<ItemGame> gameList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, COLUMN_ORDER, null);
        while (cursor.moveToNext()) {
            gameList.add(getRecord(cursor));
        }
        cursor.close();
        return gameList;
    }

    public ItemGame get(int gameID) {
        ItemGame itemGame = null;
        String where = COLUMN_GAMEID + "=" + gameID;
        Cursor result = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, COLUMN_ORDER, null);
        if (result.moveToFirst()) {
            itemGame = getRecord(result);
        }
        result.close();
        return itemGame;
    }

    private ItemGame getRecord(Cursor cursor) {
        ItemGame itemGame = new ItemGame();
        itemGame.setGameID(cursor.getInt(1));
        itemGame.setGameType(cursor.getString(2));
        itemGame.setGameVersion(cursor.getInt(3));
        itemGame.setDownloadPath(cursor.getString(4));
        itemGame.setGamePath(cursor.getString(5));
        itemGame.setBasic(cursor.getInt(6));
        itemGame.setHasUpdate(cursor.getInt(7));
        itemGame.setOrder(cursor.getInt(8)); //Added by Rex
        return itemGame;
    }

    public boolean isGameDataDownloaded(int gameID) {
        int result = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE gameID = " + gameID, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result > 0;
    }

    public List<ItemGame> getBasicGame() {
        List<ItemGame> gameList = new ArrayList<>();
        String where = "basic = 1";
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        while (cursor.moveToNext()) {
            gameList.add(getRecord(cursor));
        }
        cursor.close();
        return gameList;
    }
}