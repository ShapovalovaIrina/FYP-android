package com.fyp.ui;

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

import com.fyp.R;

public class SignInFragment extends Fragment {
    private NavController navController;
    private EditText emailInput;
    private EditText passwordInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                    navController.navigate(R.id.action_loginFragment_to_bottomNavFragment);
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
}