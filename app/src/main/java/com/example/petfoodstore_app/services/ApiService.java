package com.example.petfoodstore_app.services;

import com.example.petfoodstore_app.DTO.Login.LoginRequest;
import com.example.petfoodstore_app.DTO.Login.LoginResponse;
import com.example.petfoodstore_app.DTO.Register.RegisterRequest;
import com.example.petfoodstore_app.DTO.Register.RegisterResponse;
import com.example.petfoodstore_app.models.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({
            "accept: */*",
            "Content-Type: application/json"
    })
    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @Headers({
            "accept: */*",
            "Content-Type: application/json"
    })
    @POST("api/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("api/food/all")
    Call<List<Food>> getAllFood(@Header("Authorization") String token);
}