package com.which.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.which.R;
import com.which.data.dao.UserDao;
import com.which.data.db.WhichContract;
import com.which.data.entitties.User;
import com.which.tasks.GetAsksAsyncTask;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    private static final int USER_DATA_LOADER = 0;

    private TextView mWelcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mWelcomeText = (TextView) findViewById(R.id.welcome_text_view);

        getLoaderManager().initLoader(USER_DATA_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                // Clear the user data
                UserDao.logout(this);

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();

                return true;
        }

        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Loader<Cursor> userDataLoader = null;

        try {
            userDataLoader = new CursorLoader(
                    this,
                    WhichContract.UserEntry.CONTENT_URI.buildUpon()
                            .appendPath("current").build(),
                    null, null, null, null);
        } catch (UnsupportedOperationException e) {
            Log.d(LOG_TAG, e.getLocalizedMessage());
        }

        return userDataLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i(LOG_TAG, "Home Loader Finished");

        if (cursor.moveToNext()) {

            User user = new User();
            user.fromCursor(cursor);

            (new GetAsksAsyncTask(this, user.getAccess_token())).execute();

            mWelcomeText.setText("Welcome " + user.getUsername());
        } else {
            Log.i(LOG_TAG, "Home Loader does not have data");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.reset();
    }
}
