package com.example.petfoodstore_app.fragments.loginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.activities.LoginRegisterActivity;
import com.example.petfoodstore_app.activities.ShoppingActivity;

public class LoginFragment extends Fragment {
    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatButton loginButton = view.findViewById(R.id.buttonLoginLogin);
        TextView tvDontHaveAnAccount = view.findViewById(R.id.tvDontHaveAnAccount);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), ShoppingActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        tvDontHaveAnAccount.setOnClickListener(v -> {
            ((LoginRegisterActivity) getActivity()).switchToRegister();
        });
    }
}
