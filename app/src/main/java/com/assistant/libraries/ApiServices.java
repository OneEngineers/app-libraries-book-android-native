package com.assistant.libraries;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiServices {
    @POST("/api/register")
    Call<ResponseBody> registerUser(
            @Body User user
    );
    @POST("/api/login")
    Call<ResponseBody> loginUser(
            @Body User user
    );
    @GET("/api/user")
    Call<ResponseBody> getUser(
            @Header("Authorization") String token
    );
    @GET("/api/logout")
    Call<ResponseBody> logoutUser(
            @Header("Authorization") String token
    );

}