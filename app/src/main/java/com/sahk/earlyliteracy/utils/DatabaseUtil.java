package com.sahk.earlyliteracy.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sahk.earlyliteracy.database.TableGA;
import com.sahk.earlyliteracy.database.TableGame;
import com.sahk.earlyliteracy.database.TableGamePlay;
import com.sahk.earlyliteracy.database.TableReward;
import com.sahk.earlyliteracy.database.TableScore;
import com.sahk.earlyliteracy.database.TableStage;
import com.sahk.earlyliteracy.database.TableTheme;
import com.sahk.earlyliteracy.database.TableVideo;

public class DatabaseUtil extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "gamedata.db";
    public static final int VERSION = 4;
    private static SQLiteDatabase sqLiteDatabase;

    //Add Version By Rex
    // 3 -> 4 Add TableGA

    public DatabaseUtil(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            sqLiteDatabase = new DatabaseUtil(context, DATABASE_NAME, null, VERSION).getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TableGame.CREATE_TABLE);
        sqLiteDatabase.execSQL(TableTheme.CREATE_TABLE);
        sqLiteDatabase.execSQL(TableStage.CREATE_TABLE);
        sqLiteDatabase.execSQL(TableVideo.CREATE_TABLE);
        sqLiteDatabase.execSQL(TableGamePlay.CREATE_TABLE);
        sqLiteDatabase.execSQL(TableReward.CREATE_TABLE);
        sqLiteDatabase.execSQL(TableScore.CREATE_TABLE);
        sqLiteDatabase.execSQL(TableGA.CREATE_TABLE); // Add By Rex
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableGame.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableTheme.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableStage.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableVideo.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableGamePlay.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableReward.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableScore.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableGA.TABLE_NAME); // Add By Rex
        onCreate(sqLiteDatabase);
    }
}