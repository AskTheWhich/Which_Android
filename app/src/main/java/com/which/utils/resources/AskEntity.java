package com.which.utils.resources;

import android.content.ContentValues;

import com.which.data.db.WhichContract;

/**
 * Created by tomeramir on 01/09/2016.
 */
public class AskEntity {
    private int ask_id;
    private String text;
    private Answer left;
    private Answer right;
    private String type;
    private boolean owned;

    public int getAsk_id() {
        return ask_id;
    }

    public void setAsk_id(int ask_id) {
        this.ask_id = ask_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Answer getLeft() {
        return left;
    }

    public void setLeft(Answer left) {
        this.left = left;
    }

    public Answer getRight() {
        return right;
    }

    public void setRight(Answer right) {
        this.right = right;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(WhichContract.AskEntry.COLUMN_ASK_ID, this.ask_id);
        contentValues.put(WhichContract.AskEntry.COLUMN_TEXT, this.text);
        contentValues.put(WhichContract.AskEntry.FKEY_LEFT, this.left.getId());
        contentValues.put(WhichContract.AskEntry.FKEY_RIGHT, this.right.getId());
//        contentValues.put(WhichContract.AskEntry.TY, this.type);
        contentValues.put(WhichContract.AskEntry.COLUMN_OWNED, this.owned ? 1 : 0);

        return contentValues;
    }
}
