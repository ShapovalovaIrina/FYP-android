package com.fyp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainFragment extends Fragment {
    private NavController navController;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            if (currentUser.isEmailVerified()) {
                Toast.makeText(getContext(), "Firebase user is signed", Toast.LENGTH_SHORT).show();
                navigateToSearchFragment();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(view);

        view.findViewById(R.id.google_sing_in_button).setOnClickListener(googleSignInOnClickListener());
        view.findViewById(R.id.email_sing_in_button).setOnClickListener(emailSignInOnClickListener());
        view.findViewById(R.id.no_account_button).setOnClickListener(noAccountOnClickListener());
    }

    View.OnClickListener googleSignInOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSearchFragment();
            }
        };
    }

    View.OnClickListener emailSignInOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignInFragment();
            }
        };
    }

    View.OnClickListener noAccountOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignUpFragment();
            }
        };
    }

    private void navigateToSearchFragment() {
        navController.navigate(R.id.action_mainFragment_to_bottomNavFragment);
    }

    private void navigateToSignInFragment() {
        navController.navigate(R.id.action_mainFragment_to_signInFragment);
    }

    private void navigateToSignUpFragment() {
        navController.navigate(R.id.action_mainFragment_to_signUpFragment);
    }
}