package com.which.data.entitties;

import android.content.ContentValues;
import android.database.Cursor;

import com.which.data.db.WhichContract.UserEntry;

/**
 * Created by tomeramir on 28/08/2016.
 */
public class User {
    private int id = -1;
    private int user_id;
    private String email;
    private String password;
    private String access_token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        if (this.id > -1) {
            contentValues.put(UserEntry.COLUMN_ID, this.id);
        }
        contentValues.put(UserEntry.COLUMN_USERNAME, this.email);
        contentValues.put(UserEntry.COLUMN_PASSWORD, this.password);
        contentValues.put(UserEntry.COLUMN_ACCESS_TOKEN, this.access_token);

        return contentValues;
    }

    public void fromCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(UserEntry._ID));
        this.user_id = cursor.getInt(cursor.getColumnIndex(UserEntry.COLUMN_ID));
        this.email = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_USERNAME));
        this.password = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PASSWORD));
        this.access_token = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_ACCESS_TOKEN));
    }
}
