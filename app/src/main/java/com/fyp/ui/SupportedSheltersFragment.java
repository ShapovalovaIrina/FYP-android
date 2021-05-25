package com.fyp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.R;
import com.fyp.adapter.CardShelterAdapter;
import com.fyp.viewmodel.TypeShelterViewModel;

public class SupportedSheltersFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_supported_shelters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView shelterRecyclerView = view.findViewById(R.id.supported_shelters_fragment_shelter_recycler_view);

        CardShelterAdapter cardShelterAdapter = new CardShelterAdapter();
        shelterRecyclerView.setAdapter(cardShelterAdapter);
        TypeShelterViewModel typeShelterViewModel = new ViewModelProvider(requireActivity()).get(TypeShelterViewModel.class);
        typeShelterViewModel.getShelters().observe(getViewLifecycleOwner(), shelters -> {
            if (shelters != null) {
                cardShelterAdapter.setItems(shelters);
            }
        });
    }
}