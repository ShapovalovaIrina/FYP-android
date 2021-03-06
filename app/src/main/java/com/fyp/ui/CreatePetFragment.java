package com.fyp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.R;
import com.fyp.adapter.PhotoAdapter;
import com.fyp.response.PetBody;
import com.fyp.response.Shelter;
import com.fyp.response.Type;
import com.fyp.viewmodel.PetViewModel;
import com.fyp.viewmodel.TypeShelterViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class CreatePetFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;

    private RecyclerView photoRecyclerView;
    private PhotoAdapter photoAdapter;

    private TextInputLayout sourceInputLayout;
    private TextInputLayout shelterInputLayout;
    private TextInputLayout nameInputLayout;
    private TextInputLayout typeInputLayout;
    private TextInputLayout photoInputLayout;

    private AutoCompleteTextView shelterAutoCompleteTextView;
    private TextInputEditText nameInputEditText;
    private TextInputEditText sourceInputEditText;
    private AutoCompleteTextView typeAutoCompleteTextView;
    private TextInputEditText breedInputEditText;
    private TextInputEditText birthInputEditText;
    private TextInputEditText heightInputEditText;
    private TextInputEditText descriptionInputEditText;
    private TextInputEditText photoInputEditText;

    private List<Type> types;
    private List<Shelter> shelters;

    @Override
    public void onPause() {
        super.onPause();
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.setVisibility(View.GONE);
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

        shelterInputLayout = view.findViewById(R.id.create_pet_fragment_shelter_text_input_layout);
        shelterAutoCompleteTextView = view.findViewById(R.id.create_pet_fragment_shelter_auto_complete_text_view);

        sourceInputLayout = view.findViewById(R.id.create_pet_fragment_source_text_input_layout);
        sourceInputEditText = view.findViewById(R.id.create_pet_fragment_source_text_input_edit_text);

        nameInputLayout = view.findViewById(R.id.create_pet_fragment_name_text_input_layout);
        nameInputEditText = view.findViewById(R.id.create_pet_fragment_name_text_input_edit_text);

        typeInputLayout = view.findViewById(R.id.create_pet_fragment_type_text_input_layout);
        typeAutoCompleteTextView = view.findViewById(R.id.create_pet_fragment_type_auto_complete_text_view);

        breedInputEditText = view.findViewById(R.id.create_pet_fragment_breed_text_input_edit_text);

        birthInputEditText = view.findViewById(R.id.create_pet_fragment_birth_text_input_edit_text);

        heightInputEditText = view.findViewById(R.id.create_pet_fragment_height_text_input_edit_text);

        descriptionInputEditText = view.findViewById(R.id.create_pet_fragment_description_text_input_edit_text);

        photoInputLayout = view.findViewById(R.id.create_pet_fragment_photo_text_input_layout);
        photoInputEditText = view.findViewById(R.id.create_pet_fragment_photo_text_input_edit_text);

        ImageView submitPhotoButton = view.findViewById(R.id.create_pet_fragment_submit_photo);
        Button createPetButton = view.findViewById(R.id.create_pet_fragment_create_pet);

        bottomNavigationView.setVisibility(View.GONE);

        TypeShelterViewModel typeShelterViewModel = new ViewModelProvider(requireActivity()).get(TypeShelterViewModel.class);
        typeShelterViewModel.getShelters().observe(getViewLifecycleOwner(), shelters -> {
            if (shelters != null) {
                this.shelters = shelters;
                List<String> shelterTitles = new ArrayList<>();
                for (Shelter s : shelters) shelterTitles.add(s.getTitle());

                ArrayAdapter<String> shelterArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.exposed_dropdown_menu, shelterTitles);
                shelterAutoCompleteTextView.setAdapter(shelterArrayAdapter);
            }
        });
        typeShelterViewModel.getTypes().observe(getViewLifecycleOwner(), types -> {
            if (types != null) {
                this.types = types;
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
        createPetButton.setOnClickListener(onCreatePetButtonClickListener());
    }

    View.OnClickListener onSubmitPhotoButtonClickListener() {
        return view -> {
            photoInputLayout.setError(null);
            if (!photoInputEditText.getText().toString().equals("")) {
                photoAdapter.addItem(photoInputEditText.getText().toString());
            } else {
                photoInputLayout.setError("?????? ???????????????????? ???????????????????? ???????????????????? ?????????????? URL");
            }
        };
    }

    View.OnClickListener onCreatePetButtonClickListener() {
        return view -> {
            shelterInputLayout.setError(null);
            nameInputLayout.setError(null);
            sourceInputLayout.setError(null);
            typeInputLayout.setError(null);
            photoInputLayout.setError(null);
            if ((validateInputShelter() & validateInputName() & validateInputType() & validateInputPhoto() & validateInputSource())) {
                validateInputOptional();
            }
        };
    }

    private boolean validateInputShelter() {
        if (shelterAutoCompleteTextView.getText().toString().equals("")) {
            shelterInputLayout.setError("???????????????????? ?????????????? ??????????");
            return false;
        }
        return  true;
    }

    private boolean validateInputSource() {
        if (sourceInputEditText.getText().toString().equals("")) {
            sourceInputLayout.setError("???????????????????? ?????????????? ???????????? ???? ???????????????? ?? ????????????????");
            return false;
        }
        return  true;
    }

    private boolean validateInputName() {
        if (nameInputEditText.getText().toString().equals("")) {
            nameInputLayout.setError("???????????????????? ?????????????? ??????");
            return false;
        }
        return  true;
    }

    private boolean validateInputType() {
        if (typeAutoCompleteTextView.getText().toString().equals("")) {
            typeInputLayout.setError("???????????????????? ?????????????? ??????");
            return false;
        }
        return  true;
    }

    private boolean validateInputPhoto() {
        if (photoAdapter.getItemCount() == 0) {
            photoInputLayout.setError("???????????????????? ???????????????? ????????????????????");
            return false;
        }
        return  true;
    }

    private void validateInputOptional() {
        String startString = "?????????????????? ???????? ???? ???????? ??????????????????:\n\n";
        String endString = "??????????????, ?????? ???????????? ?????????????? ?????????????? ?????? ???????? ?????????????";
        StringBuilder stringBuilder = new StringBuilder(startString);
        if (breedInputEditText.getText().toString().equals("")) stringBuilder.append("????????????\n");
        if (birthInputEditText.getText().toString().equals("")) stringBuilder.append("?????????????? / ???????? ????????????????\n");
        if (descriptionInputEditText.getText().toString().equals("")) stringBuilder.append("????????????????\n");
        if (heightInputEditText.getText().toString().equals("")) stringBuilder.append("???????????? ?? ??????????????\n\n");
        stringBuilder.append(endString);
        // gender

        if (!stringBuilder.toString().equals(startString + endString)) {
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("???????????????? ??????????????")
                    .setMessage(stringBuilder.toString())
                    .setNegativeButton("????", (dialogInterface, i) -> createPetAPI())
                    .setPositiveButton("??????", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        } else {
            createPetAPI();
        }
    }

    private void createPetAPI() {
        PetBody pet = createPetFromInputData();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String idToken = task.getResult().getToken();
                PetViewModel petViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
                petViewModel.createPet(idToken, pet).observe(getViewLifecycleOwner(), integer -> {
                    if (integer != null) {
                        if (integer == 201) {
                            Toast.makeText(getContext(), "?????????????? ?????????????? ????????????????", Toast.LENGTH_SHORT).show();
                            navigateBack();
                        } else {
                            Toast.makeText(getContext(), "???????????? ???? ?????????? ???????????????????? ??????????????. ?????? " + integer, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private PetBody createPetFromInputData() {
        String sourceLink = sourceInputEditText.getText().toString();
        String name = nameInputEditText.getText().toString();
        String breed = breedInputEditText.getText().toString();
        String birth = birthInputEditText.getText().toString();
        String description = descriptionInputEditText.getText().toString();
        String height = heightInputEditText.getText().toString();
        List<String> photos = photoAdapter.getPhotosUrl();

        int typeId = 0;
        int shelterId = 0;
        for (Type t : types) {
            if (typeAutoCompleteTextView.getText().toString().equals(t.getType())) {
                typeId = t.getId();
                break;
            }
        }
        for (Shelter s : shelters) {
            if (shelterAutoCompleteTextView.getText().toString().equals(s.getTitle())) {
                shelterId = s.getId();
                break;
            }
        }
        return new PetBody(
                name,
                sourceLink,
                typeId,
                breed,
                birth,
                description,
                null,
                height,
                photos,
                shelterId);
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(getView());
        navController.popBackStack();
    }
}