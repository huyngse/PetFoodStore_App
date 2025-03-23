package com.example.petfoodstore_app.Service;

import com.example.petfoodstore_app.DTO.Login.LoginRequest;
import com.example.petfoodstore_app.DTO.Login.LoginResponse;
import com.example.petfoodstore_app.DTO.Register.RegisterRequest;
import com.example.petfoodstore_app.DTO.Register.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
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
}