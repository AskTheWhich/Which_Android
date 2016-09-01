package com.which.utils.resources;

import android.content.ContentValues;

import com.which.data.db.WhichContract;

/**
 * Created by tomeramir on 01/09/2016.
 */
public class Answer {
    private int id;
    private String type;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(WhichContract.Answer.COLUMN_VALUE, this.type);
        contentValues.put(WhichContract.Answer.COLUMN_TYPE, this.value);

        return contentValues;
    }
}
