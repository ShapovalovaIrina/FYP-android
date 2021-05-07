package com.fyp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fyp.R;
import com.fyp.adapter.CardPetAdapter;
import com.fyp.adapter.CardPetMockAdapter;
import com.fyp.adapter.NavigationDirection;
import com.fyp.utils.LinearHorizontalSpacingDecoration;
import com.fyp.viewmodel.FavouriteMockViewModel;
import com.fyp.viewmodel.FavouriteViewModel;
import com.fyp.viewmodel.PetMockViewModel;
import com.fyp.viewmodel.PetViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

import static com.fyp.constant.Constants.SERVER_ENABLED;

public class SearchFragment extends Fragment {
    private final String TAG = SearchFragment.class.getSimpleName();

    private DiscreteScrollView cardPetRecycleView;

    private CardPetMockAdapter cardPetMockAdapter;
    private CardPetAdapter cardPetAdapter;

    private PetMockViewModel petMockViewModel;
    private PetViewModel petViewModel;

    private TextView startSearch;

    private FilterView filterView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        cardPetRecycleView = view.findViewById(R.id.search_fragment_recycle_view);
        startSearch = view.findViewById(R.id.search_fragment_start_search);
        MaterialButtonToggleGroup buttonToggleGroup = view.findViewById(R.id.search_fragment_button_group);

        filterView = new FilterView(view);

        // set up recycle view
        cardPetRecycleView.setVisibility(View.GONE);
        cardPetRecycleView.addItemDecoration(new LinearHorizontalSpacingDecoration(32));
        cardPetRecycleView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.2f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build());

        // set up card adapter, pet view model
        if (SERVER_ENABLED) {
            // set up pet mock view model
            petViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
            setPetViewModelPetsObserver(petViewModel);

            FavouriteViewModel favouriteViewModel = new ViewModelProvider(requireActivity()).get(FavouriteViewModel.class);

            // set up favourite mock view model
            FirebaseUser firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            firebaseCurrentUser.getIdToken(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();

                    cardPetAdapter = new CardPetAdapter(
                            NavigationDirection.FROM_SEARCH_TO_PET,
                            favouriteViewModel,
                            getViewLifecycleOwner(),
                            idToken);
                    cardPetRecycleView.setAdapter(cardPetAdapter);
                } else {
                    Toast.makeText(getContext(), "Ошибка во время получения токена", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // set up pet mock view model
            petMockViewModel = new ViewModelProvider(requireActivity()).get(PetMockViewModel.class);
            setPetMockViewModelPetsObserver(petMockViewModel);

            // set up favourite mock view model
            FavouriteMockViewModel favouriteMockViewModel = new ViewModelProvider(requireActivity()).get(FavouriteMockViewModel.class);
            cardPetMockAdapter = new CardPetMockAdapter(
                    NavigationDirection.FROM_SEARCH_TO_PET,
                    favouriteMockViewModel,
                    getViewLifecycleOwner());
            cardPetRecycleView.setAdapter(cardPetMockAdapter);
        }

        buttonToggleGroup.addOnButtonCheckedListener(petTypeButtonToggleGroupListener());

        return view;
    }

    private void setPetViewModelPetsObserver(PetViewModel petViewModel) {
        petViewModel.getPets().observe(getViewLifecycleOwner(), petResponse -> {
            if (petResponse != null) {
                Toast.makeText(getContext(), "Update pet data", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "petNetworkViewModel onChanged set up adapter data");

                cardPetAdapter.clearItems();
                cardPetAdapter.setItems(petResponse);
                cardPetRecycleView.scrollToPosition(0);
            }
        });
    }

    private void setPetMockViewModelPetsObserver(PetMockViewModel petMockViewModel) {
        petMockViewModel.getPets().observe(getViewLifecycleOwner(), petMocks -> {
            cardPetMockAdapter.clearItems();
            cardPetMockAdapter.setItems(petMocks);
            cardPetRecycleView.scrollToPosition(0);
        });
    }

    MaterialButtonToggleGroup.OnButtonCheckedListener petTypeButtonToggleGroupListener() {
        return new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                List<Integer> checkedIds = group.getCheckedButtonIds();
                switch (checkedIds.size()) {
                    case 2:
                        twoButtonsPressed();
                        break;
                    case 1:
                        oneButtonPressed(checkedIds.get(0));
                        break;
                    case 0:
                        zeroButtonPressed();
                        break;
                }
            }
        };
    }

    private void twoButtonsPressed() {
        if (SERVER_ENABLED) {
            petViewModel.loadAllPets();
        } else {
            petMockViewModel.loadAllPets();
        }
    }

    private void oneButtonPressed(int checkedId) {
        startSearch.setVisibility(View.GONE);
        cardPetRecycleView.setVisibility(View.VISIBLE);

        if (SERVER_ENABLED) {
            petViewModel.loadAllPets();
        } else {
            if (checkedId == R.id.search_fragment_cat_button) {
                petMockViewModel.loadCats();
            } else if (checkedId == R.id.search_fragment_dog_button) {
                petMockViewModel.loadDogs();
            }
        }
    }

    private void zeroButtonPressed() {
        startSearch.setVisibility(View.VISIBLE);
        cardPetRecycleView.setVisibility(View.GONE);

        if (SERVER_ENABLED) {
            petViewModel.clearPets();
        } else {
            petMockViewModel.clearPets();
        }
    }
}
