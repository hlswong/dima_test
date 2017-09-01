package com.sahk.earlyliteracy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sahk.earlyliteracy.utils.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rex.leung on 25/1/2017.
 */

public class TableGA {
    public static final String TABLE_NAME = "tableGA";
    private static final String KEY_ID = "_id";
    private static final String COLUMN_gaID = "gaID";
    private static final String COLUMN_category = "category";
    private static final String COLUMN_action = "action";
    private static final String COLUMN_label = "label";
    private static final String COLUMN_value = "value";
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_gaID + " INTEGER, " +
                    COLUMN_category + " TEXT, " +
                    COLUMN_action + " TEXT, " +
                    COLUMN_label + " TEXT, " +
                    COLUMN_value + " INTEGER)";

    private SQLiteDatabase sqLiteDatabase;

    public TableGA(Context context) {
        sqLiteDatabase = DatabaseUtil.getDatabase(context);
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public boolean insert(ItemGA itemGA) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_gaID, itemGA.getGaID());
        contentValues.put(COLUMN_category, itemGA.getCategory());
        contentValues.put(COLUMN_action, itemGA.getAction());
        contentValues.put(COLUMN_label, itemGA.getLabel());
        contentValues.put(COLUMN_value, itemGA.getValue());
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) > 0;
    }

    public boolean update(ItemGA itemGA) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_gaID, itemGA.getGaID());
        contentValues.put(COLUMN_category, itemGA.getCategory());
        contentValues.put(COLUMN_action, itemGA.getAction());
        contentValues.put(COLUMN_label, itemGA.getLabel());
        contentValues.put(COLUMN_value, itemGA.getValue());
        String where = KEY_ID + "=" + itemGA.getGaID();
        return sqLiteDatabase.update(TABLE_NAME, contentValues, where, null) > 0;
    }

    // Not Tested
    public boolean deleteAll() {
        String where = " 1 = 1 " ;
        return sqLiteDatabase.delete(TABLE_NAME, where, null) > 0;
    }


    public boolean delete(int id) {
        String where = KEY_ID + "=" + id;
        return sqLiteDatabase.delete(TABLE_NAME, where, null) > 0;
    }


    public List<ItemGA> getAll() {
        List<ItemGA> result = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }
        cursor.close();
        return result;
    }

    public ItemGA get(int id) {
        ItemGA itemGA = null;
        String where = KEY_ID + "=" + id;
        Cursor result = sqLiteDatabase.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (result.moveToFirst()) {
            itemGA = getRecord(result);
        }
        result.close();
        return itemGA;
    }

    public ItemGA getRecord(Cursor cursor) {
        ItemGA itemGA = new ItemGA();
        itemGA.setGaID(cursor.getInt(1));
        itemGA.setCategory(cursor.getString(2));
        itemGA.setAction(cursor.getString(3));
        itemGA.setLabel(cursor.getString(4));
        itemGA.setValue(cursor.getInt(5));
        return itemGA;
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
}
