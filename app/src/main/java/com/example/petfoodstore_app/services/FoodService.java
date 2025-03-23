package com.example.petfoodstore_app.services;

import com.example.petfoodstore_app.models.Food;
import com.example.petfoodstore_app.requests.LoginRequest;
import com.example.petfoodstore_app.responses.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FoodService {
    @GET("food/all")
    Call<List<Food>> getAllFoods(@Header("Authorization") String token);

    @GET("food/{id}")
    Call<Food> getFoodById(@Header("Authorization") String token, @Path("id") int id);

    @POST("food")
    Call<Food> createFood(@Header("Authorization") String token, @Body Food food);

    @PUT("food/{id}")
    Call<Food> updateFood(@Header("Authorization") String token, @Path("id") int id, @Body Food food);

    @DELETE("food/{id}")
    Call<Void> deleteFood(@Header("Authorization") String token, @Path("id") int id);
}
