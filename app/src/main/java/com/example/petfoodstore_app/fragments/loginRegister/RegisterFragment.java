package com.example.petfoodstore_app.fragments.loginRegister;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.petfoodstore_app.DTO.Register.RegisterRequest;
import com.example.petfoodstore_app.DTO.Register.RegisterResponse;
import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.Service.ApiService;
import com.example.petfoodstore_app.activities.LoginRegisterActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterFragment extends Fragment {
    private EditText etName, etEmail, etPassword, etConfirmPassword;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo các view
        etName = view.findViewById(R.id.etNameRegister); // Thêm EditText cho name trong layout nếu chưa có
        etEmail = view.findViewById(R.id.etEmailRegister);
        etPassword = view.findViewById(R.id.etPasswordRegister);
        etConfirmPassword = view.findViewById(R.id.etConfirmPasswordRegister);
        AppCompatButton registerButton = view.findViewById(R.id.buttonRegisterRegister);
        TextView tvDoYouHaveAccount = view.findViewById(R.id.tvDoYouHaveAccount);

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://demo-api-uxjc.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        // Sự kiện nhấn nút Register
        registerButton.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Kiểm tra dữ liệu nhập
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo request body
            RegisterRequest registerRequest = new RegisterRequest(name, email, password);

            // Gọi API đăng ký
            Call<RegisterResponse> call = apiService.register(registerRequest);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RegisterResponse registerResponse = response.body();
                        Toast.makeText(getContext(), "Đăng ký thành công! Email: " + registerResponse.getEmail(), Toast.LENGTH_SHORT).show();

                        // Chuyển sang LoginFragment sau khi đăng ký thành công
                        ((LoginRegisterActivity) getActivity()).switchToLogin();
                    } else {
                        Toast.makeText(getContext(), "Đăng ký thất bại. Email có thể đã tồn tại.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Sự kiện nhấn "Do you have an account?"
        tvDoYouHaveAccount.setOnClickListener(v -> {
            ((LoginRegisterActivity) getActivity()).switchToLogin();
        });
    }
}