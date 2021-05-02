package com.fyp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyp.R;
import com.fyp.adapter.CardPetAdapter;
import com.fyp.pojo.Pet;
import com.fyp.utils.LinearHorizontalSpacingDecoration;
import com.fyp.viewmodel.PetViewModel;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;


public class FavouriteFragment extends Fragment {
    private DiscreteScrollView cardPetRecycleView;
    private CardPetAdapter cardPetAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Favourite fragment is recreated");
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        cardPetRecycleView = view.findViewById(R.id.favourite_fragment_recycle_view);
        cardPetRecycleView.addItemDecoration(new LinearHorizontalSpacingDecoration(32));
        cardPetRecycleView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.2f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build());
        cardPetAdapter = new CardPetAdapter();
        cardPetRecycleView.setAdapter(cardPetAdapter);

        PetViewModel petModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
        petModel.getFavouritePets().observe(getViewLifecycleOwner(), new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> pets) {
                System.out.println("Get pets from view model & set them");
                cardPetAdapter.setItems(pets);
            }
        });

        return view;
    }
}