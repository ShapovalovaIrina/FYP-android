package com.fyp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment {
    private static final String TAG = "EmailPassword";

    private NavController navController;
    private EditText emailInput;
    private EditText passwordInput;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailInput = view.findViewById(R.id.login_fragment_email);
        passwordInput = view.findViewById(R.id.login_fragment_password);

        navController = Navigation.findNavController(view);

        view.findViewById(R.id.login_fragment_button).setOnClickListener(loginButtonOnClickListener());
    }

    View.OnClickListener loginButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEmailInput() && validatePasswordInput())
                    signInWithPassword(emailInput.getText().toString(), passwordInput.getText().toString());
            }
        };
    }

    private boolean validateEmailInput() {
        if (emailInput.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Для входа в аккаунт необходимо указать почту", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validatePasswordInput() {
        if (passwordInput.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Для входа в аккаунт необходимо указать пароль", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void signInWithPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getContext(), "Sign in user " + user.getDisplayName() + ", email " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            navigateToSearchFragment();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void navigateToSearchFragment() {
        navController.navigate(R.id.action_signInFragment_to_bottomNavFragment);
    }
}