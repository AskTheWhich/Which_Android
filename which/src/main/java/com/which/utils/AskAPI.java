package com.which.utils;

import com.which.utils.resources.AskList;
import com.which.utils.resources.AskRequest;
import com.which.utils.resources.AskResponse;
import com.which.utils.resources.RequestAnswer;
import com.which.utils.resources.Token;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tomeramir on 01/09/2016.
 */
public interface AskAPI {
    @POST("/api/unanswered")
    Call<AskList> getAsks(@Body Token token);

    @POST("/api/answer")
    Call<Response<Void>> doAnswer(@Body RequestAnswer answer);

    @POST("/api/ask")
    Call<AskResponse> doPostAsk(@Body AskRequest askRequest);
}
