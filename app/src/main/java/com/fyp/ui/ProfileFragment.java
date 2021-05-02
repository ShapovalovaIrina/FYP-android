package com.fyp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fyp.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProfileFragment extends Fragment {
    private TextView name;
    private EditText newName;
    private Button saveButton;
    private Button editButton;

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

        newName.setVisibility(GONE);
        saveButton.setVisibility(GONE);

        editButton.setOnClickListener(editNameButtonOnClickListener());
        saveButton.setOnClickListener(saveNameButtonOnClickListener());
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

                name.setText(newName.getText().toString());
            }
        };
    }
}