package com.example.petfoodstore_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.RetrofitClient;
import com.example.petfoodstore_app.adapters.CartItemAdapter;
import com.example.petfoodstore_app.models.Cart;
import com.example.petfoodstore_app.models.CartItem;
import com.example.petfoodstore_app.services.CartService;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private Button btnCheckout;
    private ListView lvCartList;
    private ProgressBar progressBar;
    private TextView tvCartTotal, tvEmptyCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bindView();
        btnCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Proceed to checkout", Toast.LENGTH_SHORT).show();
        });
        fetchCart();
    }

    public void fetchCart() {
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CartActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        CartService apiService = RetrofitClient.getClient()
                .create(CartService.class);
        Call<Cart> call = apiService.getCart("Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Cart> call, @NonNull Response<Cart> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Cart cart = response.body();
                    CartItemAdapter cartItemAdapter = new CartItemAdapter(CartActivity.this, cart.getItems());
                    String strTotal = "Total: " + formatCurrencyVND(cart.getTotalAmount());
                    tvCartTotal.setText(strTotal);
                    lvCartList.setAdapter(cartItemAdapter);
                    if (!cart.getItems().isEmpty()) {
                        tvEmptyCart.setVisibility(View.GONE);
                    } else {
                        tvEmptyCart.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        String message = null;
                        if (response.errorBody() != null) {
                            message = response.errorBody().string();
                        }
                        Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(CartActivity.this, "Failed to fetch cart", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cart> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CartActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeCartItem(int productId, int quantity) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CartActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        CartService cartService = RetrofitClient.getClient().create(CartService.class);
        Call<Void> call = cartService.removeItem("Bearer " + token, productId, quantity);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                fetchCart();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateCartItem(int productId, int quantity) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CartActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        CartService cartService = RetrofitClient.getClient().create(CartService.class);
        Call<Void> call = cartService.updateItem("Bearer " + token, productId, quantity);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                fetchCart();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindView() {
        btnCheckout = findViewById(R.id.btn_cart_checkout);
        lvCartList = findViewById(R.id.lv_cart_list);
        progressBar = findViewById(R.id.progress_bar);
        tvCartTotal = findViewById(R.id.tv_cart_total);
        tvEmptyCart = findViewById(R.id.empty_cart_message);
    }

    public String formatCurrencyVND(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount);
    }

}