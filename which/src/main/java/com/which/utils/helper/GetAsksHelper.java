package com.which.utils.helper;

import android.content.Context;

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
public class GetAsksHelper {

    public static void getTasks(Token token, Context context) {
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
    }
}
