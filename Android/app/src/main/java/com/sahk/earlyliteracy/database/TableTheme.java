package com.sahk.earlyliteracy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.sahk.earlyliteracy.utils.DatabaseUtil;

public class TableTheme {

    public static final String TABLE_NAME = "tableTheme";
    public static final String COLUMN_THEMEID = "themeID";
    public static final String COLUMN_GAMEID = "gameID";
    public static final String COLUMN_GAMENAME = "gameName";
    public static final String COLUMN_THEMEPATH = "themePath";
    public static final String COLUMN_FINISHED = "finished";
    private static final String KEY_ID = "_id";
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_THEMEID + " INTEGER, " +
                    COLUMN_GAMEID + " INTEGER, " +
                    COLUMN_GAMENAME + " TEXT, " +
                    COLUMN_THEMEPATH + " TEXT, " +
                    COLUMN_FINISHED + " INTEGER)";
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    public TableTheme(Context context) {
        this.context = context;
        sqLiteDatabase = DatabaseUtil.getDatabase(context);
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public boolean insert(ItemTheme itemTheme) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_THEMEID, itemTheme.getThemeID());
        contentValues.put(COLUMN_GAMEID, itemTheme.getGameID());
        contentValues.put(COLUMN_GAMENAME, itemTheme.getGameName());
        contentValues.put(COLUMN_THEMEPATH, itemTheme.getThemePath());
        contentValues.put(COLUMN_FINISHED, itemTheme.getFinished());
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) > 0;
    }

    public boolean update(ItemTheme itemTheme) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_THEMEID, itemTheme.getThemeID());
        contentValues.put(COLUMN_GAMEID, itemTheme.getGameID());
        contentValues.put(COLUMN_GAMENAME, itemTheme.getGameName());
        contentValues.put(COLUMN_THEMEPATH, itemTheme.getThemePath());
        contentValues.put(COLUMN_FINISHED, itemTheme.getFinished());
        String where = COLUMN_THEMEID + "=" + itemTheme.getThemeID() + " AND " + COLUMN_GAMEID + "=" + itemTheme.getGameID();
        return sqLiteDatabase.update(TABLE_NAME, contentValues, where, null) > 0;
    }

    public boolean deleteThemeByGameId(int gameID) {
        String where = COLUMN_GAMEID + "=" + gameID;
        return sqLiteDatabase.delete(TABLE_NAME, where, null) > 0;
    }

    public List<ItemTheme> getAll() {
        List<ItemTheme> themeList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            themeList.add(getRecord(cursor));
        }
        cursor.close();
        return themeList;
    }

    public List<ItemTheme> getThemeGame(int themeID) {
        List<ItemTheme> themeGameList = new ArrayList<>();
        String where = COLUMN_THEMEID + "=" + themeID;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + ", tableGame " + " WHERE tableTheme.gameID = tableGame.gameID and tableTheme.themeID = " + themeID  + " order by tableGame.sort_order", null);
        //Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        while (cursor.moveToNext()) {
            themeGameList.add(getRecord(cursor));
        }
        cursor.close();
        return themeGameList;
    }

    public ItemTheme get(int themeID, int gameID) {
        ItemTheme itemTheme = new ItemTheme();
        String where = COLUMN_THEMEID + "=" + themeID + " AND " + COLUMN_GAMEID + "=" + gameID;
        Cursor result = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (result.moveToFirst()) {
            itemTheme = getRecord(result);
        }
        result.close();
        return itemTheme;
    }

    public ItemTheme getRecord(Cursor cursor) {
        ItemTheme itemTheme = new ItemTheme();
        itemTheme.setThemeID(cursor.getInt(1));
        itemTheme.setGameID(cursor.getInt(2));
        itemTheme.setGameName(cursor.getString(3));
        itemTheme.setThemePath(cursor.getString(4));
        itemTheme.setFinished(cursor.getInt(5));
        return itemTheme;
    }

    public boolean isThemeDataDownloaded(int themeID, int gameID) {
        int result = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE themeID = " + themeID + " AND gameID = " + gameID, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result > 0;
    }

    public List<ItemTheme> getFinishedGame(int themeID) {
        List<ItemTheme> finishedGameList = new ArrayList<>();
        String where = COLUMN_THEMEID + " = " + themeID + " AND " + COLUMN_FINISHED + " = 1";
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        while (cursor.moveToNext()) {
            finishedGameList.add(getRecord(cursor));
        }
        cursor.close();
        return finishedGameList;
    }

    public boolean isFinishedAllBasicGame(int themeID) {
        TableGame tableGame = new TableGame(context);
        List<ItemTheme> finishedGameList = getFinishedGame(themeID);
        int finishCount = 0;
        for (int i = 0; i < finishedGameList.size(); i++) {
            if (tableGame.get(finishedGameList.get(i).getGameID()).getBasic() == 1) {
                finishCount++;
            }
        }
        return finishCount == tableGame.getBasicGame().size();
    }
}
