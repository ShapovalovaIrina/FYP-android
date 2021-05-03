package com.fyp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fyp.R;
import com.fyp.adapter.CardPetAdapter;
import com.fyp.pojo.Pet;
import com.fyp.utils.LinearHorizontalSpacingDecoration;
import com.fyp.viewmodel.PetViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

public class SearchFragment extends Fragment {
    private DiscreteScrollView cardPetRecycleView;
    private CardPetAdapter cardPetAdapter;
    private PetViewModel petModel;
    private TextView startSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Search fragment is recreated");
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        cardPetRecycleView = view.findViewById(R.id.search_fragment_recycle_view);
        cardPetRecycleView.setVisibility(View.GONE);
        cardPetRecycleView.addItemDecoration(new LinearHorizontalSpacingDecoration(32));
        cardPetRecycleView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.2f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build());
        cardPetAdapter = new CardPetAdapter();
        cardPetRecycleView.setAdapter(cardPetAdapter);

        // set up pet view model
        petModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
        petModel.getPets().observe(getViewLifecycleOwner(), new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> pets) {
                cardPetAdapter.clearItems();
                cardPetAdapter.setItems(pets);
                cardPetRecycleView.scrollToPosition(0);
            }
        });

        // handle buttons checks
        MaterialButtonToggleGroup buttonToggleGroup = view.findViewById(R.id.search_fragment_button_group);
        buttonToggleGroup.addOnButtonCheckedListener(petTypeButtonToggleGroupListener());

        startSearch = view.findViewById(R.id.search_fragment_start_search);
        return view;
    }

    MaterialButtonToggleGroup.OnButtonCheckedListener petTypeButtonToggleGroupListener() {
        return new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                List<Integer> checkedIds = group.getCheckedButtonIds();
                switch (checkedIds.size()) {
                    case 2:
                        petModel.loadAllPets();
                        break;
                    case 1:
                        startSearch.setVisibility(View.GONE);
                        cardPetRecycleView.setVisibility(View.VISIBLE);
                        if (checkedIds.get(0) == R.id.search_fragment_cat_button) {
                            petModel.loadCats();
                        } else if (checkedIds.get(0) == R.id.search_fragment_dog_button) {
                            petModel.loadDogs();
                        }
                        break;
                    case 0:
                        startSearch.setVisibility(View.VISIBLE);
                        cardPetRecycleView.setVisibility(View.GONE);
                        petModel.clearPets();
                        break;
                }
            }
        };
    }
}
