package com.sahk.earlyliteracy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sahk.earlyliteracy.utils.DatabaseUtil;

public class TableScore {

    public static final String TABLE_NAME = "tableScore";
    private static final String KEY_ID = "_id";
    private static final String COLUMN_THEMEID = "themeID";
    private static final String COLUMN_SCORE = "score";
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_THEMEID + " INTEGER, " +
                    COLUMN_SCORE + " INTEGER)";

    private SQLiteDatabase sqLiteDatabase;

    public TableScore(Context context) {
        sqLiteDatabase = DatabaseUtil.getDatabase(context);
    }

    public boolean insert(ItemScore itemScore) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_THEMEID, itemScore.getThemeID());
        contentValues.put(COLUMN_SCORE, itemScore.getScore());
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) > 0;
    }

    public boolean update(ItemScore itemScore) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_THEMEID, itemScore.getThemeID());
        contentValues.put(COLUMN_SCORE, itemScore.getScore());
        String where = COLUMN_THEMEID + "=" + itemScore.getThemeID();
        return sqLiteDatabase.update(TABLE_NAME, contentValues, where, null) > 0;
    }

    public ItemScore get(int themeID) {
        ItemScore itemScore = null;
        String where = COLUMN_THEMEID + "=" + themeID;
        Cursor result = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (result.moveToFirst()) {
            itemScore = getRecord(result);
        }
        result.close();
        return itemScore;
    }

    private ItemScore getRecord(Cursor cursor) {
        ItemScore itemScore = new ItemScore();
        itemScore.setThemeID(cursor.getInt(1));
        itemScore.setScore(cursor.getInt(2));
        return itemScore;
    }

    public boolean isNoScore(int themeID) {
        int result = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE themeID = " + themeID, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result == 0;
    }
}
