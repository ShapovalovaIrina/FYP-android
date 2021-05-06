package com.fyp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.fyp.R;
import com.fyp.adapter.NavigationDirection;
import com.fyp.pojo.PetMock;
import com.fyp.response.Pet;
import com.fyp.response.Shelter;
import com.fyp.viewmodel.FavouriteMockViewModel;
import com.fyp.viewmodel.PetMockViewModel;
import com.fyp.viewmodel.PetViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.fyp.constant.Constants.SERVER_ENABLED;

public class PetFragment extends Fragment {
    private View rootView;
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = view;

        bottomNavigationView = view.getRootView().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);

        NavigationDirection navigationDirection;
        int absoluteAdapterPosition;
        if (getArguments() != null) {
            navigationDirection = (NavigationDirection) getArguments().getSerializable("NavigationDirection");
            absoluteAdapterPosition = getArguments().getInt("AbsoluteAdapterPosition");

            if (navigationDirection == NavigationDirection.FROM_SEARCH_TO_PET) {
                if (SERVER_ENABLED) {
                    initSearchViewModel(absoluteAdapterPosition);
                } else {
                    initSearchMockViewModel(absoluteAdapterPosition);
                }
            } else if (navigationDirection == NavigationDirection.FROM_FAVOURITE_TO_PET) {
                initFavouriteMockViewModel(absoluteAdapterPosition);
            } else {
                Toast.makeText(getContext(), "Ops, navigationDirection value problems", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void initSearchViewModel(int absoluteAdapterPosition) {
        PetViewModel petViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
        Pet pet = petViewModel.getPet(absoluteAdapterPosition);
        setPetInformation(pet);
    }

    private void initSearchMockViewModel(int absoluteAdapterPosition) {
        PetMockViewModel petMockViewModel = new ViewModelProvider(requireActivity()).get(PetMockViewModel.class);
        PetMock mockPet = petMockViewModel.getPet(absoluteAdapterPosition);
        setPetMockInformation(mockPet);
    }

    private void initFavouriteMockViewModel(int absoluteAdapterPosition) {
        FavouriteMockViewModel favouriteMockViewModel = new ViewModelProvider(requireActivity()).get(FavouriteMockViewModel.class);
        PetMock mockPet = favouriteMockViewModel.getPet(absoluteAdapterPosition);
        setPetMockInformation(mockPet);
    }

    private void setPetMockInformation(PetMock petMock) {
        ((TextView) rootView.findViewById(R.id.fragment_pet_information_name)).setText(petMock.getName());
        ((ImageView) rootView.findViewById(R.id.fragment_pet_image)).setImageResource(petMock.getResourceId());

        rootView.findViewById(R.id.fragment_pet_information_breed_layout).setVisibility(View.GONE);
        rootView.findViewById(R.id.fragment_pet_information_age_layout).setVisibility(View.GONE);
        rootView.findViewById(R.id.fragment_pet_information_gender_layout).setVisibility(View.GONE);
        rootView.findViewById(R.id.fragment_pet_information_description).setVisibility(View.GONE);

        rootView.findViewById(R.id.fragment_pet_information_shelter_title).setVisibility(View.GONE);
        rootView.findViewById(R.id.fragment_pet_information_shelter_vk_link).setVisibility(View.GONE);
        rootView.findViewById(R.id.fragment_pet_information_shelter_site_link).setVisibility(View.GONE);
    }

    private void setPetInformation(Pet pet) {
        TextView name = rootView.findViewById(R.id.fragment_pet_information_name);
        ImageView image = rootView.findViewById(R.id.fragment_pet_image);
        LinearLayout breedLayout = rootView.findViewById(R.id.fragment_pet_information_breed_layout);
        LinearLayout ageLayout = rootView.findViewById(R.id.fragment_pet_information_age_layout);
        LinearLayout genderLayout = rootView.findViewById(R.id.fragment_pet_information_gender_layout);
        TextView description = rootView.findViewById(R.id.fragment_pet_information_description);

        TextView shelterTitle = rootView.findViewById(R.id.fragment_pet_information_shelter_title);
        Button shelterVKLink = rootView.findViewById(R.id.fragment_pet_information_shelter_vk_link);
        Button shelterSiteLink = rootView.findViewById(R.id.fragment_pet_information_shelter_site_link);

        name.setText(pet.getName());
        Glide.with(image)
                .load(pet.getFirstPhoto())
                .centerCrop()
                .error(R.drawable.ic_baseline_image_24)
                .into(image);

        if (pet.getBreed() == null) {
            breedLayout.setVisibility(View.GONE);
        } else {
            TextView breed = breedLayout.findViewById(R.id.fragment_pet_information_breed);
            breed.setText(pet.getBreed());
        }

        if (pet.getBirth() == null) {
            ageLayout.setVisibility(View.GONE);
        } else {
            TextView age = ageLayout.findViewById(R.id.fragment_pet_information_age);
            age.setText(pet.getBirth());
        }

        if (pet.getGender() == null) {
            genderLayout.setVisibility(View.GONE);
        } else {
            TextView gender = genderLayout.findViewById(R.id.fragment_pet_information_gender);
            gender.setText(pet.getGender());
        }

        if (pet.getDescription() == null) {
            description.setVisibility(View.GONE);
        } else {
            description.setText(pet.getDescription());
        }

        if (pet.getShelter() == null && pet.getShelter().getTitle() == null) {
            shelterTitle.setVisibility(View.GONE);
            shelterVKLink.setVisibility(View.GONE);
            shelterVKLink.setVisibility(View.GONE);
        } else {
            Shelter shelter = pet.getShelter();
            shelterTitle.setText(shelter.getTitle());
            if (shelter.getVk_link() == null) {
                shelterVKLink.setVisibility(View.GONE);
            }
            if (shelter.getSite_link() == null) {
                shelterSiteLink.setVisibility(View.GONE);
            }
        }
    }
}