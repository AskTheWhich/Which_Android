package com.which.data.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.which.data.db.WhichContract;
import com.which.data.db.WhichDbHelper;

/**
 * Created by tomeramir on 01/09/2016.
 */
public class AnswerProvider extends ContentProvider {
    private WhichDbHelper dbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int ANSWER = 100;
    private static final int ANSWER_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WhichContract.Answer.CONTENT_AUTHORITY;

        matcher.addURI(authority, WhichContract.PATH_ANSWER, ANSWER);
        matcher.addURI(authority, WhichContract.PATH_ANSWER + "/#", ANSWER_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new WhichDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor resCursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match) {
            case ANSWER:
                resCursor = db.query(WhichContract.Answer.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ANSWER_ID:
                String[] args = { uri.getLastPathSegment() + "" };
                resCursor = db.query(WhichContract.Answer.TABLE_NAME,
                        null,
                        WhichContract.Answer._ID + " = ?",
                        args, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported operation: " + uri);
        }

        return resCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        if (uri.compareTo(WhichContract.AskEntry.CONTENT_URI) == 0) {
            return WhichContract.Answer.CONTENT_ITEM_TYPE;
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri resUri;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case ANSWER:
                id = db.insert(WhichContract.Answer.TABLE_NAME, null, contentValues);

                if (id > 0) {
                    resUri = WhichContract.Answer.buildAnswerUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return resUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int deleted_rows = 0;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case ANSWER:
                deleted_rows = db.delete(WhichContract.Answer.TABLE_NAME, s, strings);
                break;
            case ANSWER_ID:
                String[] args = { uri.getLastPathSegment() + "" };

                deleted_rows = db.delete(
                        WhichContract.Answer.TABLE_NAME,
                        WhichContract.Answer._ID + " = ?",
                        args);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return deleted_rows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
