package com.fyp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpFragment extends Fragment {
    private static final String TAG = "EmailPassword";

    private NavController navController;
    private EditText emailInput;
    private EditText nameInput;
    private EditText passwordInput;
    private EditText passwordRepeatInput;
    private CircularProgressIndicator circularProgressIndicator;
    private ScrollView scrollView;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailInput = view.findViewById(R.id.sign_up_fragment_email);
        nameInput = view.findViewById(R.id.sign_up_fragment_username);
        passwordInput = view.findViewById(R.id.sign_up_fragment_password);
        passwordRepeatInput = view.findViewById(R.id.sign_up_fragment_repeat_password);
        circularProgressIndicator = view.findViewById(R.id.sign_up_fragment_circular_progress_indicator);
        scrollView = view.findViewById(R.id.sign_in_fragment_scroll_view);

        circularProgressIndicator.setVisibility(View.GONE);

        navController = Navigation.findNavController(view);

        view.findViewById(R.id.sign_up_fragment_button).setOnClickListener(signUpButtonOnClickListener());
    }

    View.OnClickListener signUpButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEmailInput() && validateNameInput() && validatePasswordInput())
                    circularProgressIndicator.setVisibility(View.VISIBLE);
                    disableBackground();
                    createFirebasePasswordAccount(emailInput.getText().toString(), passwordInput.getText().toString());
            }
        };
    }

    private boolean validateEmailInput() {
        if (emailInput.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Для регистрации необходимо указать почту", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateNameInput() {
        if (nameInput.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Для регистрации необходимо указать Ваше имя", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validatePasswordInput() {
        if (passwordInput.getText().toString().equals("") || passwordRepeatInput.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Для регистрации необходимо указать пароль и повторить его", Toast.LENGTH_LONG).show();
            return false;
        }
        if (passwordInput.getText().toString().length() < 6 || passwordRepeatInput.getText().toString().length() < 6) {
            Toast.makeText(getActivity(), "Длина пароля должна быть не меньше 6 символов", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!passwordInput.getText().toString().equals(passwordRepeatInput.getText().toString())) {
            Toast.makeText(getActivity(), "Введенные пароли не совпадают", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void createFirebasePasswordAccount(String email, String password)  {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateFirebaseUserName(user, nameInput.getText().toString());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            circularProgressIndicator.setVisibility(View.GONE);
                            enableBackground();
                        }
                    }
                });
    }

    private void updateFirebaseUserName(@NonNull final FirebaseUser user, final String name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        final String userEmail = user.getEmail();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            Toast.makeText(getContext(), "Create user " + name + ", email " + userEmail, Toast.LENGTH_SHORT).show();
                            sendVerificationEmail(user);
//                            navigateToSearchFragment();
                        } else {
                            // If update name fails, display a message to the user.
                            Log.w(TAG, "updateProfile:failure", task.getException());
                            Toast.makeText(getContext(), "Не получилось сохранить имя", Toast.LENGTH_SHORT).show();
                            circularProgressIndicator.setVisibility(View.GONE);
                            enableBackground();
                        }
                    }
                });
    }


    private void sendVerificationEmail(@NonNull FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        circularProgressIndicator.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            final Snackbar snackbar = Snackbar
                                    .make(getView(), "Для завершения регистрации необходимо подтвердить почту", BaseTransientBottomBar.LENGTH_INDEFINITE)
                                    .setTextColor(getResources().getColor(R.color.colorWhite))
                                    .setActionTextColor(getResources().getColor(R.color.colorWhite));
                            snackbar
                                    .setAction("Ok", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                            navigateToSignInFragment();
                                        }
                                    })
                                    .show();

                        } else {
                            Log.w(TAG, "sendEmailVerification:failure", task.getException());
                            Toast.makeText(getContext(), "Не получилось отправить письмо для подтверждения аккаунта", Toast.LENGTH_SHORT).show();
                            enableBackground();
                        }
                    }
                });
    }

    private void navigateToSearchFragment() {
        navController.navigate(R.id.action_signUpFragment_to_bottomNavFragment);
    }

    private void navigateToSignInFragment() {
        navController.navigate(R.id.action_signUpFragment_to_signInFragment);
    }

    private void disableBackground() {
        scrollView.setEnabled(false);
        scrollView.setBackgroundColor(getResources().getColor(R.color.colorGray));
    }

    private void enableBackground() {
        scrollView.setEnabled(true);
        scrollView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
    }
}