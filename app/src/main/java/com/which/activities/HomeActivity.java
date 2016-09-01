package com.which.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.which.R;
import com.which.activities.fragments.AskFragment;
import com.which.data.dao.AskDao;
import com.which.data.dao.UserDao;
import com.which.data.db.WhichContract;
import com.which.data.entitties.User;
import com.which.utils.AskAPI;
import com.which.utils.ServerConnection;
import com.which.utils.resources.AskList;
import com.which.utils.resources.Token;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    private static final int USER_DATA_LOADER = 0;

    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        container = findViewById(R.id.container);

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

        } else {
            Log.i(LOG_TAG, "Home Loader does not have data");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.reset();
    }

    public class GetAsksAsyncTask extends AsyncTask<Void, Void, Void> {
        private Token token;
        private Context context;

        public GetAsksAsyncTask(Context context, String access_token) {
            this.context = context;
            this.token = new Token(access_token);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AskAPI api = ServerConnection.createAskAPI();

            Call<AskList> askListCall = api.getAsks(token);

            try {
                Response<AskList> response = askListCall.execute();

                if (response.isSuccessful()) {
                    AskDao.bulkSaveAsks(context, response.body().getAsks());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new AskFragment(), "")
                    .addToBackStack(null)
                    .commit();
        }
    }
}
