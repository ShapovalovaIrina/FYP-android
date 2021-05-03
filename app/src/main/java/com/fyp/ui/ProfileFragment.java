package com.fyp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fyp.R;
import com.fyp.pojo.User;
import com.fyp.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProfileFragment extends Fragment {
    private TextView name;
    private EditText newName;
    private Button saveButton;
    private Button editButton;

    private UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Profile fragment is created");
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.profile_fragment_name);
        newName = view.findViewById(R.id.profile_fragment_new_name);
        saveButton = view.findViewById(R.id.profile_fragment_save_changes);
        editButton = view.findViewById(R.id.profile_fragment_edit_name);
        Button signOutButton = view.findViewById(R.id.profile_fragment_sign_out);

        newName.setVisibility(GONE);
        saveButton.setVisibility(GONE);

        editButton.setOnClickListener(editNameButtonOnClickListener());
        saveButton.setOnClickListener(saveNameButtonOnClickListener());
        signOutButton.setOnClickListener(signOutButtonOnClickListener());

        // set up pet view model
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                name.setText(user.getName());
            }
        });
    }

    View.OnClickListener editNameButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setVisibility(GONE);
                editButton.setVisibility(GONE);

                newName.setVisibility(VISIBLE);
                saveButton.setVisibility(VISIBLE);
            }
        };
    }

    View.OnClickListener saveNameButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setVisibility(VISIBLE);
                editButton.setVisibility(VISIBLE);

                newName.setVisibility(GONE);
                saveButton.setVisibility(GONE);

                userViewModel.updateFirebaseUserName(newName.getText().toString());
            }
        };
    }

    View.OnClickListener signOutButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                navigateToMainFragment();
            }
        };
    }

    private void navigateToMainFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.navigation_host_fragment);
        navController.navigate(R.id.action_global_mainFragment);
    }
}