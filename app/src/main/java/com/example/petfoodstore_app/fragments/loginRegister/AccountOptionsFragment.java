package com.example.petfoodstore_app.fragments.loginRegister;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.activities.LoginRegisterActivity;

public class AccountOptionsFragment extends Fragment {
    public AccountOptionsFragment() {
        super(R.layout.fragment_account_options);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatButton buttonLogin = view.findViewById(R.id.buttonLoginAccountOptions);
        AppCompatButton buttonRegister = view.findViewById(R.id.buttonRegisterAccountOptions);
        buttonLogin.setOnClickListener(v -> {
            ((LoginRegisterActivity) getActivity()).switchToLogin();
        });
        buttonRegister.setOnClickListener(v -> {
            ((LoginRegisterActivity) getActivity()).switchToRegister();
        });
    }
}
