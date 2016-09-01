package com.which.data.dao;

import android.content.ContentValues;
import android.content.Context;

import com.which.data.db.WhichContract;
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
            ask.getLeft().setId(AnswerDao.saveAnswer(context, ask.getLeft()));
            ask.getRight().setId(AnswerDao.saveAnswer(context, ask.getRight()));

            cVVector.add(ask.getContentValues());
        }

        if (cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            context.getContentResolver().bulkInsert(WhichContract.AskEntry.CONTENT_URI, cvArray);
        }
    }

    public static void deleteAll(Context context) {
        context.getContentResolver().delete(WhichContract.AskEntry.CONTENT_URI, null, null);
        AnswerDao.deleteAll(context);
    }
}
