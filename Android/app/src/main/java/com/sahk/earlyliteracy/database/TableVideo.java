package com.sahk.earlyliteracy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.sahk.earlyliteracy.utils.DatabaseUtil;

public class TableVideo {

    public static final String TABLE_NAME = "tableVideo";
    public static final String COLUMN_THEMEID = "themeID";
    public static final String COLUMN_THEMEVIDEO = "themeVideo";
    public static final String COLUMN_SONGVIDEO = "songVideo";
    public static final String COLUMN_JQANDAVIDEO = "jQAndAVideo";
    public static final String COLUMN_SQANDAVIDEO = "sQAndAVideo";
    public static final String COLUMN_VIDEOVERSION = "videoVersion";
    public static final String COLUMN_HASUPDATE = "hasUpdate";
    private static final String KEY_ID = "_id";
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_THEMEID + " INTEGER, " +
                    COLUMN_THEMEVIDEO + " TEXT, " +
                    COLUMN_SONGVIDEO + " TEXT, " +
                    COLUMN_JQANDAVIDEO + " TEXT, " +
                    COLUMN_SQANDAVIDEO + " TEXT, " +
                    COLUMN_VIDEOVERSION + " INTEGER, " +
                    COLUMN_HASUPDATE + " INTEGER)";

    private SQLiteDatabase sqLiteDatabase;

    public TableVideo(Context context) {
        sqLiteDatabase = DatabaseUtil.getDatabase(context);
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public boolean insert(ItemVideo itemVideo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_THEMEID, itemVideo.getThemeID());
        contentValues.put(COLUMN_THEMEVIDEO, itemVideo.getThemeVideo());
        contentValues.put(COLUMN_SONGVIDEO, itemVideo.getSongVideo());
        contentValues.put(COLUMN_JQANDAVIDEO, itemVideo.getJQAndAVideo());
        contentValues.put(COLUMN_SQANDAVIDEO, itemVideo.getSQAndAVideo());
        contentValues.put(COLUMN_VIDEOVERSION, itemVideo.getVideoVersion());
        contentValues.put(COLUMN_HASUPDATE, itemVideo.getHasUpdate());
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) > 0;
    }

    public boolean update(ItemVideo itemVideo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_THEMEID, itemVideo.getThemeID());
        contentValues.put(COLUMN_THEMEVIDEO, itemVideo.getThemeVideo());
        contentValues.put(COLUMN_SONGVIDEO, itemVideo.getSongVideo());
        contentValues.put(COLUMN_JQANDAVIDEO, itemVideo.getJQAndAVideo());
        contentValues.put(COLUMN_SQANDAVIDEO, itemVideo.getSQAndAVideo());
        contentValues.put(COLUMN_VIDEOVERSION, itemVideo.getVideoVersion());
        contentValues.put(COLUMN_HASUPDATE, itemVideo.getHasUpdate());
        String where = COLUMN_THEMEID + "=" + itemVideo.getThemeID();
        return sqLiteDatabase.update(TABLE_NAME, contentValues, where, null) > 0;
    }

    public List<ItemVideo> getAll() {
        List<ItemVideo> stageList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            stageList.add(getRecord(cursor));
        }
        cursor.close();
        return stageList;
    }

    public ItemVideo get(int themeID) {
        ItemVideo itemVideo = new ItemVideo();
        String where = COLUMN_THEMEID + "=" + themeID;
        Cursor result = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (result.moveToFirst()) {
            itemVideo = getRecord(result);
        }
        result.close();
        return itemVideo;
    }

    public ItemVideo getRecord(Cursor cursor) {
        ItemVideo itemVideo = new ItemVideo();
        itemVideo.setThemeID(cursor.getInt(1));
        itemVideo.setThemeVideo(cursor.getString(2));
        itemVideo.setSongVideo(cursor.getString(3));
        itemVideo.setJQAndAVideo(cursor.getString(4));
        itemVideo.setSQAndAVideo(cursor.getString(5));
        itemVideo.setVideoVersion(cursor.getInt(6));
        itemVideo.setHasUpdate(cursor.getInt(7));
        return itemVideo;
    }

    public boolean isVideoDataDownloaded() {
        int result = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result > 0;
    }
}
