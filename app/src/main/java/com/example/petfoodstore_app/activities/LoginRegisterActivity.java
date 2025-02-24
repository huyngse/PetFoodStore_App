package com.example.petfoodstore_app.activities;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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