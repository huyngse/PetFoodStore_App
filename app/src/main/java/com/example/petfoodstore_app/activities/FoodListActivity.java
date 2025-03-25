package com.example.petfoodstore_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.RetrofitClient;
import com.example.petfoodstore_app.services.ApiService;
import com.example.petfoodstore_app.activities.Map.MapActivity;
import com.example.petfoodstore_app.adapters.FoodAdapter;
import com.example.petfoodstore_app.models.Food;
import com.example.petfoodstore_app.services.CartService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodListActivity extends AppCompatActivity {
    private ListView listView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        listView = findViewById(R.id.foodListView);
        foodAdapter = new FoodAdapter(this, foodList);
        listView.setAdapter(foodAdapter);
        progressBar = findViewById(R.id.progress_bar);

        loadFoodList(); // Gọi API để lấy danh sách thức ăn

        // Thiết lập BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home); // Mặc định chọn Home
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_cart) {
                Intent intent = new Intent(FoodListActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_map) {
                Intent intent = new Intent(FoodListActivity.this, MapActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(FoodListActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_orders) {
                Intent intent = new Intent(FoodListActivity.this, OrderActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void loadFoodList() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FoodListActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish(); // Đóng FoodListActivity để không quay lại
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://demo-api-uxjc.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Food>> call = apiService.getAllFood("Bearer " + token);

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    foodList.clear();
                    foodList.addAll(response.body());
                    foodAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(FoodListActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FoodListActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    public void addCartItem(Food food, int quantity) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FoodListActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        CartService cartService = RetrofitClient.getClient().create(CartService.class);
        Call<Void> call = cartService.addItem("Bearer " + token, food.getId(), quantity);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FoodListActivity.this, "Add to cart successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("DEBUG", t.getMessage());
                Toast.makeText(FoodListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}