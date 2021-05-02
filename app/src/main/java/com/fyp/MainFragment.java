package com.fyp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.google_sing_in_button).setOnClickListener(googleSignInOnClickListener());
        view.findViewById(R.id.email_sing_in_button).setOnClickListener(emailSignInOnClickListener());
        view.findViewById(R.id.no_account_button).setOnClickListener(noAccountOnClickListener());
    }

    View.OnClickListener googleSignInOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_signInFragment_to_bottomNavFragment);
            }
        };
    }

    View.OnClickListener emailSignInOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_signInFragment_to_loginFragment);
            }
        };
    }

    View.OnClickListener noAccountOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_signInFragment_to_singUpFragment);
            }
        };
    }
}