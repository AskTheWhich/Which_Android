package com.which.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.which.data.dao.AskDao;
import com.which.utils.AskAPI;
import com.which.utils.ServerConnection;
import com.which.utils.resources.AskList;
import com.which.utils.resources.Token;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by tomeramir on 01/09/2016.
 */
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


    }
}
