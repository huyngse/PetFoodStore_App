package com.example.petfoodstore_app.fragments.loginRegister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.petfoodstore_app.DTO.Login.LoginRequest;
import com.example.petfoodstore_app.DTO.Login.LoginResponse;
import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.services.ApiService;
import com.example.petfoodstore_app.activities.FoodListActivity;
import com.example.petfoodstore_app.activities.LoginRegisterActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {
    private EditText etEmail, etPassword;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo các view
        etEmail = view.findViewById(R.id.etEmailLogin);
        etPassword = view.findViewById(R.id.etPasswordLogin);
        AppCompatButton loginButton = view.findViewById(R.id.buttonLoginLogin);
        TextView tvDontHaveAnAccount = view.findViewById(R.id.tvDontHaveAnAccount);

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://demo-api-uxjc.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        // Sự kiện nhấn nút Login
        loginButton.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo request body
            LoginRequest loginRequest = new LoginRequest(email, password);

            // Gọi API đăng nhập
            Call<LoginResponse> call = apiService.login(loginRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        Toast.makeText(getContext(), "Đăng nhập thành công!", Toast.LENGTH_LONG).show();

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", loginResponse.getToken()); // Lấy token từ response
                        editor.apply();

                        Intent intent = new Intent(getContext(), FoodListActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Đăng nhập thất bại. Kiểm tra email/mật khẩu.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Sự kiện nhấn "Don't have an account?"
        tvDontHaveAnAccount.setOnClickListener(v -> {
            ((LoginRegisterActivity) getActivity()).switchToRegister();
        });
    }
}