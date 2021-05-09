package com.fyp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpFragment extends Fragment {
    private static final String TAG = SignUpFragment.class.getSimpleName();

    private NavController navController;
    private EditText emailInput;
    private EditText nameInput;
    private EditText passwordInput;
    private EditText passwordRepeatInput;
    private CircularProgressIndicator circularProgressIndicator;

    private Button notReceiveMail;
    private Button alreadyHaveAccount;
    private Button signUpButton;
    private Button signInButton;

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
        notReceiveMail = view.findViewById(R.id.sign_up_fragment_not_receive_mail);
        alreadyHaveAccount = view.findViewById(R.id.sign_up_fragment_already_have_account);
        signUpButton = view.findViewById(R.id.sign_up_fragment_button);
        signInButton = view.findViewById(R.id.sign_up_fragment_sign_in_button);

        notReceiveMail.setVisibility(View.GONE);
        signInButton.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.GONE);

        navController = Navigation.findNavController(view);

        signUpButton.setOnClickListener(signUpButtonOnClickListener());
        signInButton.setOnClickListener(signInButtonOnClickListener());
        alreadyHaveAccount.setOnClickListener(signInButtonOnClickListener());
        notReceiveMail.setOnClickListener(notReceiveButtonOnClickListener());
    }

    View.OnClickListener signUpButtonOnClickListener() {
        return view -> {
            if (validateEmailInput() && validateNameInput() && validatePasswordInput()) {
                circularProgressIndicator.setVisibility(View.VISIBLE);
                createFirebasePasswordAccount(emailInput.getText().toString(), passwordInput.getText().toString());
            }
        };
    }

    View.OnClickListener signInButtonOnClickListener() {
        return view -> navigateToSignInFragment();
    }

    View.OnClickListener notReceiveButtonOnClickListener() {
        return view -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user!= null) {
                Toast.makeText(getContext(), "Письмо повторно отправлено на почту " + user.getEmail(), Toast.LENGTH_SHORT).show();
                sendVerificationEmail(user);
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
                        } else {
                            // If update name fails, display a message to the user.
                            Log.w(TAG, "updateProfile:failure", task.getException());
                            Toast.makeText(getContext(), "Не получилось сохранить имя", Toast.LENGTH_SHORT).show();
                            circularProgressIndicator.setVisibility(View.GONE);
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
                            Toast.makeText(getContext(), "Для завершения регистрации подтвердите, пожалуйста, почту", Toast.LENGTH_SHORT).show();
                            signUpButton.setVisibility(View.GONE);
                            notReceiveMail.setVisibility(View.VISIBLE);
                            signInButton.setVisibility(View.VISIBLE);
                        } else {
                            Log.w(TAG, "sendEmailVerification:failure", task.getException());
                            Toast.makeText(getContext(), "Не получилось отправить письмо для подтверждения аккаунта", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void navigateToSignInFragment() {
        navController.navigate(R.id.action_signUpFragment_to_signInFragment);
    }
}