package com.fyp.ui;

import android.content.Intent;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private NavController navController;

    private GoogleSignInClient mGoogleSignInClient;
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

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(view);

        view.findViewById(R.id.google_sing_in_button).setOnClickListener(googleSignInOnClickListener());
        view.findViewById(R.id.email_sing_in_button).setOnClickListener(emailSignInOnClickListener());
        view.findViewById(R.id.no_account_button).setOnClickListener(noAccountOnClickListener());
        view.findViewById(R.id.without_registration).setOnClickListener(withoutRegistrationOnClickListener());
    }

    View.OnClickListener googleSignInOnClickListener() {
        return view -> signIn();
    }

    View.OnClickListener emailSignInOnClickListener() {
        return view -> navigateToSignInFragment();
    }

    View.OnClickListener noAccountOnClickListener() {
        return view -> navigateToSignUpFragment();
    }

    View.OnClickListener withoutRegistrationOnClickListener() {
        return view -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isAuthenticated", false);
            navigateToSearchFragment(bundle);
        };
    }

    private void navigateToSearchFragment() {
        navController.navigate(R.id.action_mainFragment_to_bottomNavFragment);
    }

    private void navigateToSearchFragment(Bundle bundle) {
        navController.navigate(R.id.action_mainFragment_to_bottomNavFragment, bundle);
    }

    private void navigateToSignInFragment() {
        navController.navigate(R.id.action_mainFragment_to_signInFragment);
    }

    private void navigateToSignUpFragment() {
        navController.navigate(R.id.action_mainFragment_to_signUpFragment);
    }

    private void authenticationFailedMessage() {
        Toast.makeText(getContext(), "Ошибка во время аутентификации", Toast.LENGTH_SHORT).show();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            navigateToSearchFragment();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            authenticationFailedMessage();
                        }
                    }
                });
    }
}