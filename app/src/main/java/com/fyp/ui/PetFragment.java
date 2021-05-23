package com.fyp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;

import com.bumptech.glide.Glide;
import com.fyp.R;
import com.fyp.adapter.NavigationDirection;
import com.fyp.response.Pet;
import com.fyp.response.Shelter;
import com.fyp.viewmodel.FavouriteViewModel;
import com.fyp.viewmodel.PagedPetViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class PetFragment extends Fragment {
    private final String TAG = PetFragment.class.getSimpleName();

    private View rootView;
    private BottomNavigationView bottomNavigationView;
    private CheckBox favouriteCheckBox;

    private Pet pet;
    private FavouriteViewModel favouriteViewModel;

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
        favouriteCheckBox = view.findViewById(R.id.fragment_pet_information_favourite);

        bottomNavigationView.setVisibility(View.GONE);

        Boolean isFavourite = getArguments().getBoolean("IsFavourite");
        Boolean isAuthenticated = getArguments().getBoolean("IsAuthenticated");
        NavigationDirection navigationDirection = (NavigationDirection) getArguments().getSerializable("NavigationDirection");
        int absoluteAdapterPosition = getArguments().getInt("AbsoluteAdapterPosition");

        favouriteCheckBox.setChecked(isFavourite);
        switch (navigationDirection) {
            case FROM_SEARCH_TO_PET:
                initSearchViewModel(absoluteAdapterPosition, isAuthenticated);
                break;
            case FROM_FAVOURITE_TO_PET:
                favouriteCheckBox.setChecked(true);
                initFavouriteViewModel(absoluteAdapterPosition, isAuthenticated);
                break;
            case NONE:
                Toast.makeText(getContext(), "NONE navigationDirection", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void initSearchViewModel(int absoluteAdapterPosition, boolean isAuthenticated) {
        // set up pet info from search adapter position
        PagedPetViewModel pagedPetViewModel = new ViewModelProvider(requireActivity()).get(PagedPetViewModel.class);
        PagedList<Pet> pagedList = pagedPetViewModel.getPetPagedList().getValue();
        if (pagedList != null) {
            pet = pagedList.get(absoluteAdapterPosition);
            setPetInformation(pet);
        }

        // set up favourite view model for add/remove favourite pet
        if (isAuthenticated) {
            favouriteViewModel = new ViewModelProvider(requireActivity()).get(FavouriteViewModel.class);
            favouriteViewModel.getCodeResponse().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    Toast.makeText(getContext(), "Favourite code response " + integer, Toast.LENGTH_SHORT).show();
                }
            });
            favouriteCheckBox.setOnCheckedChangeListener(favouriteButtonOnCheckedChangeListener());
        } else {
            favouriteCheckBox.setVisibility(View.GONE);
        }
    }

    private void initFavouriteViewModel(int absoluteAdapterPosition, boolean isAuthenticated) {
        favouriteViewModel = new ViewModelProvider(requireActivity()).get(FavouriteViewModel.class);
        pet = favouriteViewModel.getPet(absoluteAdapterPosition);
        setPetInformation(pet);

        if (isAuthenticated) {
            favouriteCheckBox.setOnCheckedChangeListener(favouriteButtonOnCheckedChangeListener());
        } else {
            favouriteCheckBox.setVisibility(View.GONE);
        }
    }

    CompoundButton.OnCheckedChangeListener favouriteButtonOnCheckedChangeListener() {
        return (compoundButton, isChecked) -> {
            if (favouriteViewModel != null) {
                FirebaseUser firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseCurrentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            if (isChecked) {
                                favouriteViewModel.addFavourite(token, pet);
                                favouriteViewModel.addFavouriteId(pet.getId());
                            } else {
                                favouriteViewModel.removeFavourite(token, pet);
                                favouriteViewModel.removeFavouriteId(pet.getId());
                            }
                        } else {
                            Log.d(TAG, "Error in firebaseCurrentUser.getIdToken. task.isSuccessful false");
                        }
                    }
                });
            }
        };
    }

    private void setPetInformation(Pet pet) {
        if (pet != null) {
            setName(pet.getName());
            setImage(pet.getFirstPhoto());
            setBreed(pet.getBreed());
            setBirth(pet.getBirth());
            setGender(pet.getGender());
            setDescription(pet.getDescription());
            setShelter(pet.getShelter());
        }
    }

    private void setName(@NonNull String name) {
        ((TextView) rootView.findViewById(R.id.fragment_pet_information_name)).setText(name);
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
            } else {
                shelterVKLink.setOnClickListener(linkButtonOnClickListener(shelter.getVk_link()));
            }
            if (shelter.getSite_link() == null) {
                shelterSiteLink.setVisibility(View.GONE);
            } else {
                shelterSiteLink.setOnClickListener(linkButtonOnClickListener(shelter.getSite_link()));
            }
        }
    }

    private View.OnClickListener linkButtonOnClickListener(String uri) {
        return view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            if (getContext() != null && intent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(intent);
            }
        };
    }
}