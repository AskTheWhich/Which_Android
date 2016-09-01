package com.which.activities.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.which.AskAdapter;
import com.which.R;
import com.which.data.db.WhichContract;
import com.which.utils.resources.AskEntity;

/**
 * Created by tomeramir on 01/09/2016.
 */
public class AskFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = AskFragment.class.getSimpleName();
    private static final int ASK_DATA_LOADER = 1;

    private AskEntity currAsk;
    private CursorAdapter mAskAdapter;
    private ListView mAskLayout;

    public AskFragment() {
        // Requires Empty Constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View fragmentLayout =
                inflater.inflate(R.layout.fragment_ask, container, false);

        mAskAdapter = new AskAdapter(getActivity(), null, 0);

        mAskLayout = (ListView) fragmentLayout.findViewById(R.id.ask_layout);
        mAskLayout.setAdapter(mAskAdapter);

        getLoaderManager().initLoader(ASK_DATA_LOADER, null, this);

        return fragmentLayout;
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
        mAskAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.reset();
    }
}
