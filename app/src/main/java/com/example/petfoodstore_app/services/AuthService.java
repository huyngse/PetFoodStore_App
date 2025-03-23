package com.example.petfoodstore_app.services;



import com.example.petfoodstore_app.DTO.Login.LoginRequest;
import com.example.petfoodstore_app.DTO.Login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

}
