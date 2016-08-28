package com.which.data.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tomeramir on 28/08/2016.
 */
public class WhichContract implements BaseColumns {
    public static final String PATH_USER = "user";


    public static final class UserEntry implements BaseColumns {
        public static final String CONTENT_AUTHORITY = "com.which.user";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        // A string that identifies the entry data structure for a single item, since
        // it returns the user data row from the DB
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME           = "user";
        public static final String COLUMN_USER_ID       = "user_id";
        public static final String COLUMN_EMAIL         = "email";
        public static final String COLUMN_PASSWORD      = "password";
        public static final String COLUMN_ACCESS_TOKEN  = "access_token";

        public static Uri buildUserUri(long userId) {
            return ContentUris.withAppendedId(CONTENT_URI, userId);
        }
    }
}
