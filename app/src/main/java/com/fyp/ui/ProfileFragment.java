package com.fyp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fyp.R;
import com.fyp.pojo.UserMock;
import com.fyp.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProfileFragment extends Fragment {
    private final String TAG = ProfileFragment.class.getSimpleName();
    private TextView name;
    private EditText newName;
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
        newName = view.findViewById(R.id.profile_fragment_new_name);
        saveButton = view.findViewById(R.id.profile_fragment_save_changes);
        editButton = view.findViewById(R.id.profile_fragment_edit_name);
        Button signOutButton = view.findViewById(R.id.profile_fragment_sign_out);
        Button sendFeedBack = view.findViewById(R.id.profile_fragment_send_feedback);

        newName.setVisibility(GONE);
        saveButton.setVisibility(GONE);

        editButton.setOnClickListener(editNameButtonOnClickListener());
        saveButton.setOnClickListener(saveNameButtonOnClickListener());
        signOutButton.setOnClickListener(signOutButtonOnClickListener());
        sendFeedBack.setOnClickListener(sendFeedBackButtonOnClickListener());

        // set up pet view model
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<UserMock>() {
            @Override
            public void onChanged(UserMock userMock) {
                name.setText(userMock.getName());
            }
        });
    }

    View.OnClickListener editNameButtonOnClickListener() {
        return view -> {
            name.setVisibility(GONE);
            editButton.setVisibility(GONE);

            newName.setVisibility(VISIBLE);
            saveButton.setVisibility(VISIBLE);
        };
    }

    View.OnClickListener saveNameButtonOnClickListener() {
        return view -> {
            name.setVisibility(VISIBLE);
            editButton.setVisibility(VISIBLE);

            newName.setVisibility(GONE);
            saveButton.setVisibility(GONE);

            userViewModel.updateFirebaseUserName(newName.getText().toString());
        };
    }

    View.OnClickListener signOutButtonOnClickListener() {
        return view -> {
            FirebaseAuth.getInstance().signOut();
            navigateToMainFragment();
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
}