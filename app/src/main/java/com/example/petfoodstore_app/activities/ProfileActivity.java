package com.example.petfoodstore_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.models.UserProfile;
import com.example.petfoodstore_app.services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvEmail, tvName, tvPhone, tvAddress, tvGender;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Khởi tạo các view
        TextView tvProfileTitle = findViewById(R.id.tvProfileTitle);
        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvGender = findViewById(R.id.tvGender);
        btnLogout = findViewById(R.id.btnLogout);

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://demo-api-uxjc.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Gọi API /api/profile
        Call<UserProfile> call = apiService.getUserProfile("Bearer " + token);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile userProfile = response.body();
                    // Hiển thị thông tin lên giao diện
                    tvEmail.setText(userProfile.getEmail() != null ? userProfile.getEmail() : "N/A");
                    tvName.setText(userProfile.getName() != null ? userProfile.getName() : "N/A");
                    tvPhone.setText(userProfile.getPhone() != null ? userProfile.getPhone() : "N/A");
                    tvAddress.setText(userProfile.getAddress() != null ? userProfile.getAddress() : "N/A");
                    tvGender.setText(userProfile.getGender() != null ? userProfile.getGender() : "N/A");
                } else {
                    Toast.makeText(ProfileActivity.this, "Lỗi tải thông tin profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });

        // Xử lý nút Logout
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().apply(); // Xóa token
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }
}