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
        setName(petMock.getName());
        setImage(petMock.getResourceId());
        setBreed(null);
        setBirth(null);
        setGender(null);
        setDescription(null);
        setShelter(null);
    }

    private void setPetInformation(Pet pet) {
        setName(pet.getName());
        setImage(pet.getFirstPhoto());
        setBreed(pet.getBreed());
        setBirth(pet.getBirth());
        setGender(pet.getGender());
        setDescription(pet.getDescription());
        setShelter(pet.getShelter());
    }

    private void setName(@NonNull String name) {
        ((TextView) rootView.findViewById(R.id.fragment_pet_information_name)).setText(name);
    }

    private void setImage(@NonNull Integer resourceId) {
        ((ImageView) rootView.findViewById(R.id.fragment_pet_image)).setImageResource(resourceId);
    }

    private void setImage(@NonNull String imageURL) {
        ImageView imageView = rootView.findViewById(R.id.fragment_pet_image);
        Glide.with(imageView)
                .load(imageURL)
                .centerCrop()
                .error(R.drawable.ic_baseline_image_24)
                .into(imageView);
    }

    private void setBreed(String breed) {
        LinearLayout breedLayout = rootView.findViewById(R.id.fragment_pet_information_breed_layout);
        if (breed == null) {
            breedLayout.setVisibility(View.GONE);
        } else {
            TextView breedTextView = breedLayout.findViewById(R.id.fragment_pet_information_breed);
            breedTextView.setText(breed);
        }
    }

    private void setBirth(String birth) {
        LinearLayout birthLayout = rootView.findViewById(R.id.fragment_pet_information_age_layout);
        if (birth == null) {
            birthLayout.setVisibility(View.GONE);
        } else {
            TextView birthTextView = birthLayout.findViewById(R.id.fragment_pet_information_age);
            birthTextView.setText(birth);
        }
    }

    private void setGender(String gender) {
        LinearLayout genderLayout = rootView.findViewById(R.id.fragment_pet_information_gender_layout);
        if (gender == null) {
            genderLayout.setVisibility(View.GONE);
        } else {
            TextView genderTextView = genderLayout.findViewById(R.id.fragment_pet_information_gender);
            genderTextView.setText(gender);
        }
    }

    private void setDescription(String description) {
        TextView descriptionTextView = rootView.findViewById(R.id.fragment_pet_information_description);
        if (description == null) {
            descriptionTextView.setVisibility(View.GONE);
        } else {
            descriptionTextView.setText(description);
        }
    }

    private void setShelter(Shelter shelter) {
        LinearLayout shelterLayout = rootView.findViewById(R.id.fragment_pet_information_shelter_linear_layout);

        if (shelter == null || shelter.getTitle() == null) {
            shelterLayout.setVisibility(View.GONE);
        } else {
            TextView shelterTitle = rootView.findViewById(R.id.fragment_pet_information_shelter_title);
            Button shelterVKLink = rootView.findViewById(R.id.fragment_pet_information_shelter_vk_link);
            Button shelterSiteLink = rootView.findViewById(R.id.fragment_pet_information_shelter_site_link);

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