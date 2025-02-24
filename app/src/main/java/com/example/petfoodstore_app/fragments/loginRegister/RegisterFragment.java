package com.example.petfoodstore_app.fragments.loginRegister;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.activities.LoginRegisterActivity;

public class RegisterFragment extends Fragment {
    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvDoYouHaveAccount = view.findViewById(R.id.tvDoYouHaveAccount);
        tvDoYouHaveAccount.setOnClickListener(v -> {
            ((LoginRegisterActivity) getActivity()).switchToLogin();
        });
    }
}
