package com.which.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tomeramir on 28/08/2016.
 */
public class WhichDbHelper extends SQLiteOpenHelper {
    private final String LOG_TAG = getClass().getSimpleName();

    public static final String DATABASE_NAME = "which.db";
    public static final int DATABASE_VERSION = 1;

    public WhichDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG, "Creating the DataBase");

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + WhichContract.UserEntry.TABLE_NAME + " (" +
                WhichContract.UserEntry._ID + " INTEGER PRIMARY KEY, " +
                WhichContract.UserEntry.COLUMN_USER_ID + " TEXT UNIQUE NULL, " + // TODO: Not null
                WhichContract.UserEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                WhichContract.UserEntry.COLUMN_PASSWORD + " TEXT NULL, " +
                WhichContract.UserEntry.COLUMN_ACCESS_TOKEN + " TEXT NULL" +
                ")";

        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(LOG_TAG, "Dropping Tables and recreating them");

        db.execSQL("DROP TABLE IF EXISTS " + WhichContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }
}
