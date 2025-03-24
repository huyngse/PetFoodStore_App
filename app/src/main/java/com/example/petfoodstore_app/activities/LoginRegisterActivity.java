package com.example.petfoodstore_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.fragments.loginRegister.AccountOptionsFragment;
import com.example.petfoodstore_app.fragments.loginRegister.IntroductionFragment;
import com.example.petfoodstore_app.fragments.loginRegister.LoginFragment;
import com.example.petfoodstore_app.fragments.loginRegister.RegisterFragment;

public class LoginRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_register);

        // Kiểm tra token trong SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Nếu token tồn tại, chuyển thẳng đến FoodListActivity
        if (!token.isEmpty()) {
            Intent intent = new Intent(LoginRegisterActivity.this, FoodListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa stack để không quay lại
            startActivity(intent);
            finish(); // Đóng LoginRegisterActivity
            return;
        }

        // Xử lý padding cho thanh hệ thống (system bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hiển thị IntroductionFragment nếu không có trạng thái lưu
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, new IntroductionFragment())
                    .commit();
        }
    }

    public void switchToAccountOptions() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new AccountOptionsFragment())
                .addToBackStack(null)
                .commit();
    }

    public void switchToLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    public void switchToRegister() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new RegisterFragment())
                .addToBackStack(null)
                .commit();
    }
}