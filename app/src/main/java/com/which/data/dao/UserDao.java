package com.which.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.which.data.db.WhichContract;
import com.which.data.entitties.User;

/**
 * Created by tomeramir on 29/08/2016.
 */
public class UserDao {
    private static final String LOG_TAG = UserDao.class.getSimpleName();

    public static boolean isLoggedIn(Context context) {
        return getCurrentUser(context) != null;
    }

    public static void logout(Context context) {
        context.getContentResolver().delete(
                WhichContract.UserEntry.CONTENT_URI.buildUpon().appendPath("current").build(),
                null, null);
    }

    public static Uri insertUser(Context mContext, User user) {
        return mContext.getContentResolver().insert(WhichContract.UserEntry.CONTENT_URI, user.getContentValues());
    }

    public static User getCurrentUser(Context context) {
        Cursor cursor = null;
        User res = null;

        try {
            cursor = context.getContentResolver().query(
                    WhichContract.UserEntry.CONTENT_URI,
                    null,
                    WhichContract.UserEntry.COLUMN_ACCESS_TOKEN + "!= NULL",
                    null, null);

            if (cursor != null && cursor.moveToNext()) {
                User user = new User();
                user.fromCursor(cursor);

                res = user;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return res;
    }
}
