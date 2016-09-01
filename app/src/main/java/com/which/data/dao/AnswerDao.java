package com.which.data.dao;

import android.content.Context;
import android.net.Uri;

import com.which.data.db.WhichContract;
import com.which.utils.resources.Answer;

/**
 * Created by tomeramir on 01/09/2016.
 */
public class AnswerDao {
    public static int saveAnswer(Context context, Answer answer) {
        Uri res = context.getContentResolver().insert(WhichContract.Answer.CONTENT_URI, answer.getContentValues());

        if (res != null)
            return Integer.parseInt(res.getLastPathSegment());
        else
            return -1;
    }

    public static void deleteAll(Context context) {
        context.getContentResolver().delete(WhichContract.Answer.CONTENT_URI, null, null);
    }
}
