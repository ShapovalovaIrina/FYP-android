package com.fyp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fyp.R;
import com.fyp.response.Shelter;
import com.fyp.viewmodel.TypeShelterViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateShelterFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;

    private TextInputLayout titleInputLayout;
    private TextInputLayout vkInputLayout;
    private TextInputLayout siteInputLayout;

    private TextInputEditText titleInputEditText;
    private TextInputEditText vkInputEditText;
    private TextInputEditText siteInputEditText;

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
        return inflater.inflate(R.layout.fragment_create_shelter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView = view.getRootView().findViewById(R.id.bottom_navigation);
        Button createShelter = view.findViewById(R.id.create_shelter_fragment_submit_button);

        titleInputLayout = view.findViewById(R.id.create_shelter_fragment_title_text_input_layout);
        titleInputEditText = view.findViewById(R.id.create_shelter_fragment_title_text_input_edit_text);

        vkInputLayout = view.findViewById(R.id.create_shelter_fragment_vk_text_input_layout);
        vkInputEditText = view.findViewById(R.id.create_shelter_fragment_vk_text_input_edit_text);

        siteInputLayout = view.findViewById(R.id.create_shelter_fragment_site_text_input_layout);
        siteInputEditText = view.findViewById(R.id.create_shelter_fragment_site_text_input_edit_text);

        createShelter.setOnClickListener(onCreateShelterClickListener());

        bottomNavigationView.setVisibility(View.GONE);
    }

    View.OnClickListener onCreateShelterClickListener() {
        return view -> {
            titleInputLayout.setError(null);
            vkInputLayout.setError(null);
            siteInputLayout.setError(null);
            if (validateTitle() & validateLinks()) {
                createShelterAPI();
            }
        };
    }

    private boolean validateTitle() {
        if (titleInputEditText.getText().toString().equals("")) {
            titleInputLayout.setError("Необходимо указать название");
            return false;
        }
        return true;
    }

    private boolean validateLinks() {
        if (vkInputEditText.getText().toString().equals("") && siteInputEditText.getText().toString().equals("")) {
            vkInputLayout.setError("Необходимо указать ссылку либо на сообщество во ВКонтакте, либо на веб-сайт, либо оба варианта");
            siteInputLayout.setError("Необходимо указать ссылку либо на сообщество во ВКонтакте, либо на веб-сайт, либо оба варианта");
            return false;
        }
        return true;
    }

    private void createShelterAPI() {
        Shelter shelter = createShelterFromInputData();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String idToken = task.getResult().getToken();
                TypeShelterViewModel typeShelterViewModel = new ViewModelProvider(requireActivity()).get(TypeShelterViewModel.class);
                typeShelterViewModel.createShelter(idToken, shelter).observe(getViewLifecycleOwner(), integer -> {
                    if (integer != null) {
                        if (integer == 201) {
                            Toast.makeText(getContext(), "Приют успешно добавлен", Toast.LENGTH_SHORT).show();
                            navigateBack();
                        } else {
                            Toast.makeText(getContext(), "Ошибка во время добавления приюта. Код " + integer, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private Shelter createShelterFromInputData() {
        String title = titleInputEditText.getText().toString();
        String vk = vkInputEditText.getText().toString();
        String site = siteInputEditText.getText().toString();
        return new Shelter(0, title, site, vk);
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(getView());
        navController.popBackStack();
    }
}