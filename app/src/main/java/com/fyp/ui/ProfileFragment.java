package com.fyp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fyp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProfileFragment extends Fragment {
    private final String TAG = ProfileFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button signOutButton = view.findViewById(R.id.profile_fragment_sign_out);
        Button sendFeedBack = view.findViewById(R.id.profile_fragment_send_feedback);
        Button updatePassword = view.findViewById(R.id.profile_fragment_update_password);
        Button createPet = view.findViewById(R.id.profile_fragment_create_pet);
        Button createShelter = view.findViewById(R.id.profile_fragment_create_shelter);

        signOutButton.setOnClickListener(signOutButtonOnClickListener());
        sendFeedBack.setOnClickListener(sendFeedBackButtonOnClickListener());
        createPet.setOnClickListener(createPetButtonOnClickListener());
        createShelter.setOnClickListener(createShelterButtonOnClickListener());
        updatePassword.setVisibility(GONE);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        for (UserInfo userInfo : firebaseUser.getProviderData()) {
            if (userInfo.getProviderId().equals(EmailAuthProvider.PROVIDER_ID)) {
                updatePassword.setVisibility(VISIBLE);
                updatePassword.setOnClickListener(updatePasswordButtonOnClickListener());
            }
        }

        ShelterRecyclerView shelterRecyclerView = new ShelterRecyclerView(view, requireActivity(), getViewLifecycleOwner());
    }

    View.OnClickListener updatePasswordButtonOnClickListener() {
        return view -> navigateToUpdatePasswordFragment();
    }

    View.OnClickListener createPetButtonOnClickListener() {
        return view -> navigateToCreatePetFragment();
    }

    View.OnClickListener createShelterButtonOnClickListener() {
        return view -> navigateToCreateShelterFragment();
    }

    View.OnClickListener signOutButtonOnClickListener() {
        return view -> {
            FirebaseAuth.getInstance().signOut();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
            mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), task -> {
                if (task.isSuccessful()) {
                    navigateToMainFragment();
                }
            });
        };
    }

    View.OnClickListener sendFeedBackButtonOnClickListener() {
        return view -> {
            String[] addresses = {"TestMail@yandex.ru"};
            String subject = "Обратная связь по приложению Find your pet";

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            if (getContext() != null && intent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(intent);
            }
        };
    }

    private void navigateToMainFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.navigation_host_fragment);
        navController.navigate(R.id.action_global_mainFragment);
    }

    private void navigateToUpdatePasswordFragment() {
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_profileFragment_to_updatePasswordFragment);
    }

    private void navigateToCreatePetFragment() {
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_profileFragment_to_createPetFragment);
    }

    private void navigateToCreateShelterFragment() {
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_profileFragment_to_createShelterFragment);
    }
}