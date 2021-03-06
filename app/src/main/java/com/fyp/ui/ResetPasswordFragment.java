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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fyp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {
    private final String TAG = ResetPasswordFragment.class.getSimpleName();

    private FirebaseAuth mAuth;

    private TextInputLayout emailInputLayout;
    private TextInputEditText emailInputEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailInputLayout = view.findViewById(R.id.reset_password_fragment_email_input_layout);
        emailInputEditText = view.findViewById(R.id.reset_password_fragment_email_input_edit_text);

        view.findViewById(R.id.reset_password_fragment_button).setOnClickListener(resetPasswordButtonOnClickListener());
    }

    View.OnClickListener resetPasswordButtonOnClickListener() {
        return view -> {
            emailInputLayout.setError(null);
            if (validateEmailInput()) {
                String emailAddress = emailInputEditText.getText().toString();
                mAuth.fetchSignInMethodsForEmail(emailAddress).addOnCompleteListener(task -> {
                    boolean isProviderListEmpty = task.getResult().getSignInMethods().isEmpty();
                    if (isProviderListEmpty) {
                        noSuchEmailErrorMessage();
                        return;
                    }
                    sendResetPasswordMail(emailAddress);
                });
            }
        };
    }

    private void sendResetPasswordMail(String emailAddress) {
        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "???????????? ?????????????? ????????????????????", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Email sent.");

                        navigateBack();
                    } else {
                        Toast.makeText(getContext(), "???????????? ???? ?????????? ???????????????? ????????????. ???????????????????? ??????????", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Email sent error.");
                    }
                });
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(getView());
        navController.popBackStack();
    }

    private boolean validateEmailInput() {
        if (emailInputEditText.getText().toString().equals("")) {
            emptyEmailErrorMessage();
            return false;
        }
        return true;
    }

    private void emptyEmailErrorMessage() {
        emailInputLayout.setError("?????? ???????????????????????????? ???????????? ???????????????????? ?????????????? ??????????");
    }

    private void noSuchEmailErrorMessage() {
        emailInputLayout.setError("???????????????????????? ?? ?????????????????? ???????????? ???? ??????????????????????????????");
    }
}