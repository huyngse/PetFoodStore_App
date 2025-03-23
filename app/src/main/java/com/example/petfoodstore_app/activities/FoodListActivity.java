package com.example.petfoodstore_app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.Service.ApiService;
import com.example.petfoodstore_app.adapters.FoodAdapter;
import com.example.petfoodstore_app.models.Food;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        listView = findViewById(R.id.foodListView);
        foodAdapter = new FoodAdapter(this, foodList);  // ✅ Đúng constructor
        listView.setAdapter(foodAdapter);  // ✅ Không còn lỗi

        loadFoodList(); // Gọi API để lấy danh sách thức ăn
    }

    private void loadFoodList() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://demo-api-uxjc.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Food>> call = apiService.getAllFood("Bearer " + token);

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
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
                Toast.makeText(FoodListActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }
}
