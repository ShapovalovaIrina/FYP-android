package com.fyp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fyp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePasswordFragment extends Fragment {
    private final String TAG = UpdatePasswordFragment.class.getSimpleName();

    private TextInputLayout currentPasswordInput;
    private TextInputEditText currentPasswordInputEdit;

    private TextInputLayout passwordInput;
    private TextInputEditText passwordInputEdit;

    private TextInputLayout passwordRepeatInput;
    private TextInputEditText passwordRepeatInputEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentPasswordInput = view.findViewById(R.id.update_password_fragment_current_password_input_layout);
        currentPasswordInputEdit = view.findViewById(R.id.update_password_fragment_current_password_input_edit_text);
        passwordInput = view.findViewById(R.id.update_password_fragment_new_password_input_layout);
        passwordInputEdit = view.findViewById(R.id.update_password_fragment_new_password_input_edit_text);
        passwordRepeatInput = view.findViewById(R.id.update_password_fragment_repeat_new_password_input_layout);
        passwordRepeatInputEdit = view.findViewById(R.id.update_password_fragment_repeat_new_password_input_edit_text);

        view.findViewById(R.id.update_password_fragment_update_password).setOnClickListener(updatePasswordButtonOnClickListener());
    }

    View.OnClickListener updatePasswordButtonOnClickListener() {
        return view -> {
            currentPasswordInput.setError(null);
            passwordInput.setError(null);
            passwordRepeatInput.setError(null);
            if (validateCurrentPassword() && validateNewPasswordInput()) {
                reauthenticateFirebaseUser();
            }
        };
    }

    private boolean validateCurrentPassword() {
        if (currentPasswordInputEdit.getText().toString().equals("")) {
            currentPasswordInput.setError("Для изменения введите текущий пароль");
            return false;
        }
        return true;
    }

    private boolean validateNewPasswordInput() {
        if (passwordInputEdit.getText().toString().equals("") || passwordRepeatInputEdit.getText().toString().equals("")) {
            passwordInput.setError("Для изменения пароля необходимо указать пароль и повторить его");
            return false;
        }
        if (passwordInputEdit.getText().toString().length() < 6) {
            passwordInput.setError("Длина пароля должна быть не меньше 6 символов");
            return false;
        }
        if (passwordRepeatInputEdit.getText().toString().length() < 6) {
            passwordRepeatInput.setError("Длина пароля должна быть не меньше 6 символов");
            return false;
        }
        if (!passwordInputEdit.getText().toString().equals(passwordRepeatInputEdit.getText().toString())) {
            passwordInput.setError("Введенные пароли не совпадают");
            return false;
        }
        return true;
    }

    private void reauthenticateFirebaseUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPasswordInputEdit.getText().toString());
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateFirebaseAccountPassword(passwordInputEdit.getText().toString());
            } else {
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    currentPasswordInput.setError("Текущий пароль введен не верно");
                } else {
                    Toast.makeText(getContext(), "Не удалось провести аутентификацию", Toast.LENGTH_SHORT).show();
                }
                Log.w(TAG, "reauthenticate:failure ", task.getException());
            }
        });
    }

    private void updateFirebaseAccountPassword(String newPassword)  {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Пароль успешно обновлен", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Ошибка во время обновления пароля", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "updatePassword:failure", task.getException());
            }
        });
    }
}