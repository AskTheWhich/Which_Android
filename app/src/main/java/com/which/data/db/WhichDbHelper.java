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
    public static final int DATABASE_VERSION = 4;

    public WhichDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG, "***** Creating the DataBase *****");

        Log.i(LOG_TAG, "***** Creating User Table *****");
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + WhichContract.UserEntry.TABLE_NAME + " (" +
                WhichContract.UserEntry._ID                 + " INTEGER PRIMARY KEY, " +
                WhichContract.UserEntry.COLUMN_ID           + " TEXT UNIQUE NULL, " + // TODO: Not null
                WhichContract.UserEntry.COLUMN_USERNAME     + " TEXT NOT NULL, " +
                WhichContract.UserEntry.COLUMN_PASSWORD     + " TEXT NULL, " +
                WhichContract.UserEntry.COLUMN_ACCESS_TOKEN + " TEXT NULL" +
                ")";

        Log.d(LOG_TAG, SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_USER_TABLE);

        Log.i(LOG_TAG, "***** User Table Created *****");

        Log.i(LOG_TAG, "***** Creating Answer Table *****");

        final String SQL_CREATE_ANSWER_TABLE = "CREATE TABLE " + WhichContract.Answer.TABLE_NAME + " (" +
                WhichContract.Answer._ID            + " INTEGER PRIMARY KEY, " +
                WhichContract.Answer.COLUMN_ID      + " TEXT UNIQUE NULL, " +
                WhichContract.Answer.COLUMN_TYPE    + " TEXT NOT NULL, " +
                WhichContract.Answer.COLUMN_VALUE   + " TEXT NOT NULL" +
                ")";

        Log.d(LOG_TAG, SQL_CREATE_ANSWER_TABLE);
        db.execSQL(SQL_CREATE_ANSWER_TABLE);

        Log.i(LOG_TAG, "***** Answer Table Created *****");

        Log.i(LOG_TAG, "***** Created Ask Table *****");

        final String SQL_CREATE_ASK_TABLE = "CREATE TABLE " + WhichContract.AskEntry.TABLE_NAME + " (" +
                WhichContract.AskEntry._ID              + " INTEGER PRIMARY KEY, " +
                WhichContract.AskEntry.COLUMN_ASK_ID    + " TEXT UNIQUE NULL, " +
                WhichContract.AskEntry.COLUMN_TEXT      + " TEXT NOT NULL, " +
                WhichContract.AskEntry.FKEY_LEFT        + " INTEGER NOT NULL, " +
                WhichContract.AskEntry.FKEY_RIGHT       + " INTEGER NOT NULL, " +

                "FOREIGN KEY(" + WhichContract.AskEntry.FKEY_LEFT + ") REFERENCES "
                    + WhichContract.Answer.TABLE_NAME + "(" + WhichContract.Answer._ID + "), " +
                "FOREIGN KEY(" + WhichContract.AskEntry.FKEY_RIGHT + ") REFERENCES "
                    + WhichContract.Answer.TABLE_NAME + "(" + WhichContract.Answer._ID + ")" +
                ")";

        Log.d(LOG_TAG, SQL_CREATE_ASK_TABLE);
        db.execSQL(SQL_CREATE_ASK_TABLE);

        Log.i(LOG_TAG, "***** Ask Table Created *****");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(LOG_TAG, "Dropping Tables and recreating them");

        db.execSQL("DROP TABLE IF EXISTS " + WhichContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WhichContract.AskEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WhichContract.Answer.TABLE_NAME);
        onCreate(db);
    }
}
