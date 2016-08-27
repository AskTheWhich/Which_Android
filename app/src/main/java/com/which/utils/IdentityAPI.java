package com.which.utils;

import com.which.utils.resources.LoginData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tomeramir on 27/08/2016.
 */
public interface IdentityAPI {
    @POST("/login")
    Call<ResponseBody> doLogin(@Body LoginData loginData);
}
