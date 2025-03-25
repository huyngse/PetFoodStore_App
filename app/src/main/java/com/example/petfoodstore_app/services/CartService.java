package com.example.petfoodstore_app.services;

import com.example.petfoodstore_app.models.Cart;
import com.example.petfoodstore_app.models.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartService {
    @PUT("cart/update")
    Call<String> updateItem(@Header("Authorization") String token,
                                    @Query("productId") int productId,
                                    @Query("quantity") int quantity);

    @POST("cart/add")
    Call<String> addItem(@Header("Authorization") String token,
                       @Query("productId") int productId,
                       @Query("quantity") int quantity);

    @GET("cart")
    Call<Cart> getCart(@Header("Authorization") String token);

    @DELETE("cart/remove")
    Call<Void> removeItem(@Header("Authorization") String token,
                          @Query("productId") int productId,
                          @Query("quantity") int quantity);
}
