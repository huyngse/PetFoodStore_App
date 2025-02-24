package com.example.petfoodstore_app.fragments.loginRegister;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.activities.LoginRegisterActivity;

public class IntroductionFragment extends Fragment {
    public IntroductionFragment() {
        super(R.layout.fragment_introduction);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatButton buttonStart = view.findViewById(R.id.buttonStartIntroduction);
        buttonStart.setOnClickListener(v -> {
            ((LoginRegisterActivity) getActivity()).switchToAccountOptions();
        });
    }
}
