package com.fyp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;

import com.fyp.R;
import com.fyp.adapter.CardPetMockAdapter;
import com.fyp.adapter.NavigationDirection;
import com.fyp.adapter.PagedCardPetAdapter;
import com.fyp.response.Pet;
import com.fyp.utils.LinearHorizontalSpacingDecoration;
import com.fyp.viewmodel.FavouriteMockViewModel;
import com.fyp.viewmodel.FavouriteViewModel;
import com.fyp.viewmodel.PagedPetViewModel;
import com.fyp.viewmodel.PetMockViewModel;
import com.fyp.viewmodel.SearchFragmentViewModel;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;

import static com.fyp.constant.Constants.SERVER_ENABLED;

public class SearchFragment extends Fragment {
    private final String TAG = SearchFragment.class.getSimpleName();

    private DiscreteScrollView cardPetRecycleView;

    private CardPetMockAdapter cardPetMockAdapter;
    private PagedCardPetAdapter pagedCardPetAdapter;

    private PetMockViewModel petMockViewModel;
    private PagedPetViewModel pagedPetViewModel;

    private Button startSearch;
    private TextView nothingFoundTextView;
    private CircularProgressIndicator circularProgressIndicator;

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
        nothingFoundTextView = view.findViewById(R.id.search_fragment_nothing_found);
        circularProgressIndicator = view.findViewById(R.id.search_fragment_circular_progress_indicator);

        showRecyclerView();

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
            pagedPetViewModel = new ViewModelProvider(requireActivity()).get(PagedPetViewModel.class);
            FavouriteViewModel favouriteViewModel = new ViewModelProvider(requireActivity()).get(FavouriteViewModel.class);

            pagedPetViewModel.getZeroItemsLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isZero) {
                    if (isZero) {
                        showNothingFound();
                    }
                }
            });

            /* Init adapter with Firebase id token */
            FirebaseUser firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            firebaseCurrentUser.getIdToken(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();

                    pagedCardPetAdapter = new PagedCardPetAdapter(
                            NavigationDirection.FROM_SEARCH_TO_PET,
                            favouriteViewModel,
                            getViewLifecycleOwner(),
                            idToken);
                    cardPetRecycleView.setAdapter(pagedCardPetAdapter);
                    setPreviousPetData(searchFragmentViewModel.getRecycleViewItemPosition());
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

    private void setPreviousPetData(Integer position) {
        LiveData<PagedList<Pet>> pagedListLiveData = pagedPetViewModel.getPetPagedList();
        if (pagedListLiveData != null) {
            pagedListLiveData.observe(getViewLifecycleOwner(), new Observer<PagedList<Pet>>() {
                @Override
                public void onChanged(PagedList<Pet> pets) {
                    if (pets != null && pets.size() != 0) {
                        pagedCardPetAdapter.submitList(pets);
                        Log.d(TAG, "Show previous data");
                        if (position != null) {
                            Log.d(TAG, "Position " + position);
                            cardPetRecycleView.scrollToPosition(position);
                        } else {
                            Log.d(TAG, "New position 0");
                            cardPetRecycleView.scrollToPosition(0);
                        }
                    }
                }
            });
        }
    }

    private void setPagedPetViewModelPetsObserver(String typeFilter, String shelterFilter) {
        LiveData<PagedList<Pet>> pagedListLiveData = pagedPetViewModel.getPetPagedList();
        if (pagedListLiveData != null) pagedListLiveData.removeObservers(getViewLifecycleOwner());

        pagedPetViewModel.getPetPagedList(typeFilter, shelterFilter).observe(getViewLifecycleOwner(), new Observer<PagedList<Pet>>() {
            @Override
            public void onChanged(PagedList<Pet> pets) {
                if (pets != null) {
                    showRecyclerView();
                    pagedCardPetAdapter.submitList(pets);
                    cardPetRecycleView.scrollToPosition(0);
                }
            }
        });
    }

    private void setPetMockViewModelPetsObserver(PetMockViewModel petMockViewModel) {
        petMockViewModel.getPets().observe(getViewLifecycleOwner(), petMocks -> {
            if (petMocks != null) {
                if (petMocks.size() == 0) {
                    cardPetMockAdapter.clearItems();
                    showNothingFound();
                } else {
                    showRecyclerView();
                    if (!firstSet) {
                        cardPetMockAdapter.clearItems();
                        cardPetMockAdapter.setItems(petMocks);
                        cardPetRecycleView.scrollToPosition(0);
                    } else {
                        cardPetMockAdapter.setItems(petMocks);
                        firstSet = false;
                    }
                }
            }
        });
    }

    View.OnClickListener startSearchButtonOnClickListener() {
        return view -> loadPets();
    }

    private void loadPets() {
        String typeFilter = filterView.getTypeFilter();
        String shelterFilter = filterView.getShelterFilter();
        Log.d(TAG, "Type: " + typeFilter + ". Shelter: " + shelterFilter);

        showCircularProgressIndicator();
        if (SERVER_ENABLED) {
            setPagedPetViewModelPetsObserver(typeFilter, shelterFilter);
        } else {
            petMockViewModel.loadAllPets(typeFilter, shelterFilter);
        }
    }

    private void showRecyclerView() {
        cardPetRecycleView.setVisibility(View.VISIBLE);
        nothingFoundTextView.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.GONE);
    }

    private void showCircularProgressIndicator() {
        cardPetRecycleView.setVisibility(View.INVISIBLE);
        nothingFoundTextView.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);
    }

    private void showNothingFound() {
        cardPetRecycleView.setVisibility(View.INVISIBLE);
        nothingFoundTextView.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);
    }
}
