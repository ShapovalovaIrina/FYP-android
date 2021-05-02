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
import android.widget.EditText;
import android.widget.Toast;

public class SignUpFragment extends Fragment {
    private NavController navController;
    private EditText emailInput;
    private EditText nameInput;
    private EditText passwordInput;
    private EditText passwordRepeatInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailInput = view.findViewById(R.id.sign_up_fragment_email);
        nameInput = view.findViewById(R.id.sign_up_fragment_username);
        passwordInput = view.findViewById(R.id.sign_up_fragment_password);
        passwordRepeatInput = view.findViewById(R.id.sign_up_fragment_repeat_password);

        navController = Navigation.findNavController(view);

        view.findViewById(R.id.sign_up_fragment_button).setOnClickListener(signUpButtonOnClickListener());
    }

    View.OnClickListener signUpButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEmailInput() && validateNameInput() && validatePasswordInput())
                    navController.navigate(R.id.action_singUpFragment_to_bottomNavFragment);
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
}