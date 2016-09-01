package com.which.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.which.data.db.WhichContract;
import com.which.utils.resources.Answer;
import com.which.utils.resources.AskEntity;

import java.util.List;
import java.util.Vector;

/**
 * Created by tomeramir on 01/09/2016.
 */
public class AskDao {
    private static final String LOG_TAG = AskDao.class.getSimpleName();

    public static void bulkSaveAsks(Context context, List<AskEntity> asks) {
        if (asks == null) {
            return;
        }

        Vector<ContentValues> cVVector = new Vector<>(asks.size());

        for (AskEntity ask: asks) {
            ask.getLeft().setId(saveAnswer(context, ask.getLeft()));
            ask.getRight().setId(saveAnswer(context, ask.getRight()));

            cVVector.add(ask.getContentValues());
        }

        if (cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            context.getContentResolver().bulkInsert(WhichContract.AskEntry.CONTENT_URI, cvArray);
        }
    }

    public static int saveAnswer(Context context, Answer answer) {
        Uri res = context.getContentResolver().insert(WhichContract.Answer.CONTENT_URI, answer.getContentValues());

        if (res != null)
            return Integer.parseInt(res.getLastPathSegment());
        else
            return -1;
    }
}
