package com.fyp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fyp.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment {
    private static final String TAG = "EmailPassword";

    private NavController navController;
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText emailInputEditText;
    private TextInputEditText passwordInputEditText;

    private CircularProgressIndicator circularProgressIndicator;
    private LinearLayout formLinearLayout;

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

        emailInputLayout = view.findViewById(R.id.sign_in_fragment_email_input_layout);
        emailInputEditText = view.findViewById(R.id.sign_in_fragment_email_input_edit_text);
        passwordInputLayout = view.findViewById(R.id.sign_in_fragment_password_input_layout);
        passwordInputEditText = view.findViewById(R.id.sign_in_fragment_password_input_edit_text);
        circularProgressIndicator = view.findViewById(R.id.sign_in_fragment_circular_progress_indicator);
        formLinearLayout = view.findViewById(R.id.sign_in_fragment_form_linear_layout);

        circularProgressIndicator.setVisibility(View.GONE);

        navController = Navigation.findNavController(view);

        view.findViewById(R.id.sign_in_fragment_button).setOnClickListener(loginButtonOnClickListener());
        view.findViewById(R.id.sign_in_fragment_reset_password).setOnClickListener(resetPasswordButtonOnClickListener());
    }

    View.OnClickListener resetPasswordButtonOnClickListener() {
        return view -> navigateToResetPasswordFragment();
    }

    View.OnClickListener loginButtonOnClickListener() {
        return view -> {
            emailInputLayout.setError(null);
            passwordInputLayout.setError(null);
            if (validateEmailInput() && validatePasswordInput()) {
                formLinearLayout.setVisibility(View.INVISIBLE);
                circularProgressIndicator.setVisibility(View.VISIBLE);
                signInWithPassword(emailInputEditText.getText().toString(), passwordInputEditText.getText().toString());
            }
        };
    }

    private boolean validateEmailInput() {
        if (emailInputEditText.getText().toString().equals("")) {
            emailInputLayout.setError("Для входа в аккаунт необходимо указать почту");
            return false;
        }
        return true;
    }

    private boolean validatePasswordInput() {
        if (passwordInputEditText.getText().toString().equals("")) {
            passwordInputLayout.setError("Для входа в аккаунт необходимо указать пароль");
            return false;
        }
        return true;
    }

    private void signInWithPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user.isEmailVerified()) {
                            circularProgressIndicator.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Sign in user " + user.getDisplayName() + ", email " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            navigateToSearchFragment();
                        } else {
                            formLinearLayout.setVisibility(View.VISIBLE);
                            circularProgressIndicator.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Для того, чтобы продолжить, подтвердите, пожалуйста, аккаунт", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        formLinearLayout.setVisibility(View.VISIBLE);
                        circularProgressIndicator.setVisibility(View.GONE);
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidUserException) {
                            emailInputLayout.setError("Пользователь с данной почтой не существует");
                        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            passwordInputLayout.setError("Неверный пароль");
                        } else {
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void navigateToSearchFragment() {
        navController.navigate(R.id.action_signInFragment_to_bottomNavFragment);
    }

    private void navigateToResetPasswordFragment() {
        navController.navigate(R.id.action_signInFragment_to_resetPasswordFragment);
    }
}