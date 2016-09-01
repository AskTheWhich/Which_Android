package com.which.utils;

import com.which.utils.resources.LoginData;
import com.which.utils.resources.LoginResponse;
import com.which.utils.resources.RegisterData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * API for the server identity handling
 *
 * Created by tomeramir on 27/08/2016.
 */
public interface IdentityAPI {
    @POST("/api/login")
    Call<LoginResponse> doLogin(@Body LoginData loginData);

    @POST("/api/register")
    Call<LoginResponse> doRegister(@Body RegisterData registerData);
}
