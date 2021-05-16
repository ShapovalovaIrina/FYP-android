package com.fyp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.fyp.viewmodel.SearchFragmentViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import static com.fyp.constant.Constants.SERVER_ENABLED;

public class SearchFragment extends Fragment {
    private final String TAG = SearchFragment.class.getSimpleName();

    private DiscreteScrollView cardPetRecycleView;

    private CardPetMockAdapter cardPetMockAdapter;
    private CardPetAdapter cardPetAdapter;

    private PetMockViewModel petMockViewModel;
    private PetViewModel petViewModel;

    private Button startSearch;

    private FilterView filterView;
    private SearchFragmentViewModel searchFragmentViewModel;
    private boolean firstSet = true;

    @Override
    public void onPause() {
        super.onPause();
        searchFragmentViewModel.setRecycleViewItemPosition(cardPetRecycleView.getCurrentItem());

        searchFragmentViewModel.setShelterFilterParentCheckBox(filterView.getShelterParentCheckBoxState());
        searchFragmentViewModel.setShelterFilterChildrenCheckBoxes(filterView.getShelterChildrenCheckBoxesState());

        searchFragmentViewModel.setTypeFilterParentCheckBox(filterView.getTypeParentCheckBoxState());
        searchFragmentViewModel.setTypeFilterChildrenCheckBoxes(filterView.getTypeChildrenCheckBoxesState());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        cardPetRecycleView = view.findViewById(R.id.search_fragment_recycle_view);
        startSearch = view.findViewById(R.id.search_fragment_start_search);

        startSearch.setOnClickListener(startSearchButtonOnClickListener());

        searchFragmentViewModel = new ViewModelProvider(requireActivity()).get(SearchFragmentViewModel.class);

        /* Create views for filtering */
        filterView = new FilterView(
                view,
                requireActivity(),
                getViewLifecycleOwner(),
                searchFragmentViewModel.getShelterFilterParentCheckBox(),
                searchFragmentViewModel.getTypeFilterParentCheckBox(),
                searchFragmentViewModel.getShelterFilterChildrenCheckBoxes(),
                searchFragmentViewModel.getTypeFilterChildrenCheckBoxes()
        );

        /* set up recycle view */
        cardPetRecycleView.addItemDecoration(new LinearHorizontalSpacingDecoration(32));
        cardPetRecycleView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build());

        /* set up card adapter and set it for recycler view,
           Pet view model, favourite view model */
        if (SERVER_ENABLED) {
            petViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
            setPetViewModelPetsObserver(petViewModel);
            FavouriteViewModel favouriteViewModel = new ViewModelProvider(requireActivity()).get(FavouriteViewModel.class);

            /* Init adapter with Firebase id token */
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
                    if (searchFragmentViewModel.getRecycleViewItemPosition() != null) cardPetRecycleView.scrollToPosition(searchFragmentViewModel.getRecycleViewItemPosition());
                } else {
                    Toast.makeText(getContext(), "Ошибка во время получения токена", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            petMockViewModel = new ViewModelProvider(requireActivity()).get(PetMockViewModel.class);
            setPetMockViewModelPetsObserver(petMockViewModel);
            FavouriteMockViewModel favouriteMockViewModel = new ViewModelProvider(requireActivity()).get(FavouriteMockViewModel.class);

            cardPetMockAdapter = new CardPetMockAdapter(
                    NavigationDirection.FROM_SEARCH_TO_PET,
                    favouriteMockViewModel,
                    getViewLifecycleOwner());
            cardPetRecycleView.setAdapter(cardPetMockAdapter);
            if (searchFragmentViewModel.getRecycleViewItemPosition() != null) cardPetRecycleView.scrollToPosition(searchFragmentViewModel.getRecycleViewItemPosition());
        }

        return view;
    }

    private void setPetViewModelPetsObserver(PetViewModel petViewModel) {
        petViewModel.getPets().observe(getViewLifecycleOwner(), petResponse -> {
            if (petResponse != null) {
                if (!firstSet) {
                    cardPetAdapter.clearItems();
                    cardPetAdapter.setItems(petResponse);
                    cardPetRecycleView.scrollToPosition(0);
                } else {
                    cardPetAdapter.setItems(petResponse);
                    firstSet = false;
                }
            }
        });
    }

    private void setPetMockViewModelPetsObserver(PetMockViewModel petMockViewModel) {
        petMockViewModel.getPets().observe(getViewLifecycleOwner(), petMocks -> {
            if (!firstSet) {
                cardPetMockAdapter.clearItems();
                cardPetMockAdapter.setItems(petMocks);
                cardPetRecycleView.scrollToPosition(0);
            } else {
                cardPetMockAdapter.setItems(petMocks);
                firstSet = false;
            }
        });
    }

    View.OnClickListener startSearchButtonOnClickListener() {
        return view -> loadPets();
    }

    private void loadPets() {
        if (SERVER_ENABLED) {
            petViewModel.loadAllPets(null, null);
        } else {
            petMockViewModel.loadAllPets();
        }
    }
}
