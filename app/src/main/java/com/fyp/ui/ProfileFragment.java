package com.fyp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fyp.R;
import com.fyp.viewmodel.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProfileFragment extends Fragment {
    private final String TAG = ProfileFragment.class.getSimpleName();
    private TextView name;
    private TextInputLayout nameInputLayout;
    private TextInputEditText nameInputEditText;
    private Button saveButton;
    private Button editButton;

    private UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.profile_fragment_name);
        nameInputLayout = view.findViewById(R.id.profile_fragment_name_input_layout);
        nameInputEditText = view.findViewById(R.id.profile_fragment_name_input_edit_text);
        saveButton = view.findViewById(R.id.profile_fragment_save_changes);
        editButton = view.findViewById(R.id.profile_fragment_edit_name);
        Button signOutButton = view.findViewById(R.id.profile_fragment_sign_out);
        Button sendFeedBack = view.findViewById(R.id.profile_fragment_send_feedback);
        Button updatePassword = view.findViewById(R.id.profile_fragment_update_password);
        Button createPet = view.findViewById(R.id.profile_fragment_create_pet);
        Button createShelter = view.findViewById(R.id.profile_fragment_create_shelter);

        hideEditName();

        editButton.setOnClickListener(editNameButtonOnClickListener());
        saveButton.setOnClickListener(saveNameButtonOnClickListener());
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

        // set up pet view model
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> name.setText(user.getName()));

        ShelterRecyclerView shelterRecyclerView = new ShelterRecyclerView(view, requireActivity(), getViewLifecycleOwner());
    }

    View.OnClickListener editNameButtonOnClickListener() {
        return view -> showEditName();
    }

    View.OnClickListener saveNameButtonOnClickListener() {
        return view -> {
            if (validateName()) {
                hideEditName();
                userViewModel.updateFirebaseUserName(nameInputEditText.getText().toString());
            }
        };
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

    private boolean validateName() {
        if (nameInputEditText.getText().toString().equals("")) {
            nameInputLayout.setError("Имя не может быть пустым");
            return false;
        }
        return true;
    }

    private void hideEditName() {
        name.setVisibility(VISIBLE);
        editButton.setVisibility(VISIBLE);

        nameInputLayout.setVisibility(GONE);
        saveButton.setVisibility(GONE);
    }

    private void showEditName() {
        name.setVisibility(GONE);
        editButton.setVisibility(GONE);

        nameInputLayout.setVisibility(VISIBLE);
        saveButton.setVisibility(VISIBLE);
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