package com.fyp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpFragment extends Fragment {
    private static final String TAG = SignUpFragment.class.getSimpleName();

    private NavController navController;
    private TextInputLayout emailInput;
    private TextInputLayout nameInput;
    private TextInputLayout passwordInput;
    private TextInputLayout passwordRepeatInput;

    private TextInputEditText emailInputEdit;
    private TextInputEditText nameInputEdit;
    private TextInputEditText passwordInputEdit;
    private TextInputEditText passwordRepeatInputEdit;

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

        emailInput = view.findViewById(R.id.sign_up_fragment_email_input_layout);
        nameInput = view.findViewById(R.id.sign_up_fragment_name_input_layout);
        passwordInput = view.findViewById(R.id.sign_up_fragment_password_input_layout);
        passwordRepeatInput = view.findViewById(R.id.sign_up_fragment_repeat_password_input_layout);

        emailInputEdit = view.findViewById(R.id.sign_up_fragment_email_input_edit_text);
        nameInputEdit = view.findViewById(R.id.sign_up_fragment_name_input_edit_text);
        passwordInputEdit = view.findViewById(R.id.sign_up_fragment_password_input_edit_text);
        passwordRepeatInputEdit = view.findViewById(R.id.sign_up_fragment_repeat_password_input_edit_text);

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
            emailInput.setError(null);
            nameInput.setError(null);
            passwordInput.setError(null);
            if (validateEmailInput() && validateNameInput() && validatePasswordInput()) {
                circularProgressIndicator.setVisibility(View.VISIBLE);
                createFirebasePasswordAccount(emailInputEdit.getText().toString(), passwordInputEdit.getText().toString());
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
        if (emailInputEdit.getText().toString().equals("")) {
            emailInput.setError("Для регистрации необходимо указать почту");
            return false;
        }
        return true;
    }

    private boolean validateNameInput() {
        if (nameInputEdit.getText().toString().equals("")) {
            nameInput.setError("Для регистрации необходимо указать Ваше имя");
            return false;
        }
        return true;
    }

    private boolean validatePasswordInput() {
        if (passwordInputEdit.getText().toString().equals("") || passwordRepeatInputEdit.getText().toString().equals("")) {
            passwordInput.setError("Для регистрации необходимо указать пароль и повторить его");
            return false;
        }
        if (passwordInputEdit.getText().toString().length() < 6 || passwordRepeatInputEdit.getText().toString().length() < 6) {
            passwordInput.setError("Длина пароля должна быть не меньше 6 символов");
            return false;
        }
        if (!passwordInputEdit.getText().toString().equals(passwordRepeatInputEdit.getText().toString())) {
            passwordInput.setError("Введенные пароли не совпадают");
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
                            updateFirebaseUserName(user, nameInputEdit.getText().toString());
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