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
 * Created by tomeramir on 28/08/2016.
 */
public class UserProvider extends ContentProvider {
    private WhichDbHelper mWhichDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int USER       = 100;
    private static final int USER_ID    = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WhichContract.UserEntry.CONTENT_AUTHORITY;

        matcher.addURI(authority, WhichContract.PATH_USER, USER);
        matcher.addURI(authority, WhichContract.PATH_USER + "/#", USER_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mWhichDbHelper = new WhichDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        SQLiteDatabase readableDatabase = mWhichDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match) {
            case USER:
                retCursor = readableDatabase.query(
                        WhichContract.UserEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case USER_ID:
                retCursor = readableDatabase.query(
                        WhichContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: + " + uri);
        }

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        if (uri.compareTo(WhichContract.UserEntry.CONTENT_URI) == 0) {
            return WhichContract.UserEntry.CONTENT_ITEM_TYPE;
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mWhichDbHelper.getWritableDatabase();
        long id;

        Uri resUri;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case USER:
                id = db.insert(WhichContract.UserEntry.TABLE_NAME, null, contentValues);

                if (id > 0) {
                    resUri = WhichContract.UserEntry.buildUserUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;
            case USER_ID:
                // TODO: Decide if needed
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return resUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mWhichDbHelper.getWritableDatabase();
        int rowsDeleted;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case USER:

            case USER_ID:
                rowsDeleted = db.delete(WhichContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mWhichDbHelper.getWritableDatabase();
        int rowsUpdated;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case USER:
                // Do nothing
                rowsUpdated = 0;
                break;
            case USER_ID:
                rowsUpdated = db.update(WhichContract.UserEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rowsUpdated;
    }
}
