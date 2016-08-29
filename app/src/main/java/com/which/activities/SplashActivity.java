package com.which.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.which.R;
import com.which.data.dao.UserDao;
import com.which.data.entitties.User;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final SplashActivity curr = this;

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // check if logged in

                LoadAppTask appTask = new LoadAppTask(curr);
                appTask.execute();

                finish();
            }
        }, 3000);
    }

    private class LoadAppTask extends AsyncTask<Void, Void, User> {
        private String LOG_TAG = LoadAppTask.class.getSimpleName();
        private final Context mContext;

        public LoadAppTask(Context context) {
            mContext = context;
        }

        @Override
        protected User doInBackground(Void... voids) {
            Log.i(LOG_TAG, "Checking if user is logged in");

            return UserDao.isLoggedIn(mContext);
        }

        @Override
        protected void onPostExecute(User user) {
            Intent intent;
            if (user != null) {
                Log.d(LOG_TAG, "Logged in, going home");

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("access_token", user.getAccess_token());
                editor.putInt("user_id", user.getId());

                editor.apply();

                intent = new Intent(mContext, HomeActivity.class);
            } else {
                Log.d(LOG_TAG, "Not Logged in, going to Login");
                intent = new Intent(mContext, LoginActivity.class);
            }
            startActivity(intent);
        }
    }
}
