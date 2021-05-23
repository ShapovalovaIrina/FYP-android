package com.fyp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.R;
import com.fyp.adapter.PhotoAdapter;
import com.fyp.response.Shelter;
import com.fyp.response.Type;
import com.fyp.viewmodel.TypeShelterViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CreatePetFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;

    private RecyclerView photoRecyclerView;
    private PhotoAdapter photoAdapter;

    private TextInputEditText photoInputEditText;

    @Override
    public void onPause() {
        super.onPause();
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView = view.getRootView().findViewById(R.id.bottom_navigation);
        photoRecyclerView = view.findViewById(R.id.create_pet_fragment_photo_recycler_view);
        photoInputEditText = view.findViewById(R.id.create_pet_fragment_photo_text_input_edit_text);
        AutoCompleteTextView shelterAutoCompleteTextView = view.findViewById(R.id.create_pet_fragment_shelter_auto_complete_text_view);
        AutoCompleteTextView typeAutoCompleteTextView = view.findViewById(R.id.create_pet_fragment_type_auto_complete_text_view);
        ImageView submitPhotoButton = view.findViewById(R.id.create_pet_fragment_submit_photo);

        bottomNavigationView.setVisibility(View.GONE);

        TypeShelterViewModel typeShelterViewModel = new ViewModelProvider(requireActivity()).get(TypeShelterViewModel.class);
        typeShelterViewModel.getShelters().observe(getViewLifecycleOwner(), shelters -> {
            if (shelters != null) {
                List<String> shelterTitles = new ArrayList<>();
                for (Shelter s : shelters) shelterTitles.add(s.getTitle());

                ArrayAdapter<String> shelterArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.exposed_dropdown_menu, shelterTitles);
                shelterAutoCompleteTextView.setAdapter(shelterArrayAdapter);
            }
        });
        typeShelterViewModel.getTypes().observe(getViewLifecycleOwner(), types -> {
            if (types != null) {
                List<String> typeTitle = new ArrayList<>();
                for (Type t : types) typeTitle.add(t.getType());

                ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.exposed_dropdown_menu, typeTitle);
                typeAutoCompleteTextView.setAdapter(typeArrayAdapter);
            }
        });

        photoAdapter = new PhotoAdapter();
        photoRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        photoRecyclerView.setAdapter(photoAdapter);

        submitPhotoButton.setOnClickListener(onSubmitPhotoButtonClickListener());
    }

    View.OnClickListener onSubmitPhotoButtonClickListener() {
        return view -> {
            if (!photoInputEditText.getText().toString().equals("")) {
                photoAdapter.addItem(photoInputEditText.getText().toString());
            }
        };
    }
}