package com.which.utils;

import com.which.utils.resources.AskList;
import com.which.utils.resources.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tomeramir on 01/09/2016.
 */
public interface AskAPI {
    @POST("/api/asks")
    Call<AskList> getAsks(@Body Token token);
}
