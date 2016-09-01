package com.which.activities.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.which.data.db.WhichContract;

/**
 * Created by tomeramir on 01/09/2016.
 */
public class AskFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = AskFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Loader<Cursor> asks = null;

        try {
            asks = new CursorLoader(
                    getActivity(),
                    WhichContract.AskEntry.CONTENT_URI,
                    null,
                    WhichContract.AskEntry.COLUMN_OWNED + " = 0",
                    null, null);
        } catch (UnsupportedOperationException e) {
            Log.d(LOG_TAG, e.getLocalizedMessage());
        }

        return asks;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // TODO: Show data
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
