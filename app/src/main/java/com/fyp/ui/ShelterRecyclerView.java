package com.fyp.ui;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.R;
import com.fyp.adapter.CardShelterAdapter;
import com.fyp.response.Shelter;
import com.fyp.viewmodel.TypeShelterViewModel;

public class ShelterRecyclerView {
    private final String TAG = ShelterRecyclerView.class.getSimpleName();

    private RecyclerView shelterRecyclerView;
    private CheckBox showMoreButton;

    public ShelterRecyclerView(
            View view,
            ViewModelStoreOwner requiredActivity,
            LifecycleOwner lifecycleOwner) {
        shelterRecyclerView = view.findViewById(R.id.profile_fragment_shelter_recycler_view);
        showMoreButton = view.findViewById(R.id.profile_fragment_shelter_show_more_button);

        onCollapseView();

        showMoreButton.setOnCheckedChangeListener(showMoreButtonOnCheckedChangeListener());

        CardShelterAdapter cardShelterAdapter = new CardShelterAdapter();
        shelterRecyclerView.setAdapter(cardShelterAdapter);
        TypeShelterViewModel typeShelterViewModel = new ViewModelProvider(requiredActivity).get(TypeShelterViewModel.class);
        typeShelterViewModel.getShelters().observe(lifecycleOwner, shelters -> {
            if (shelters != null) {
                cardShelterAdapter.setItems(shelters);
            }
        });
    }

    CompoundButton.OnCheckedChangeListener showMoreButtonOnCheckedChangeListener() {
        return (compoundButton, isChecked) -> {
            if (isChecked) {
                onExpandView();
            } else {
                onCollapseView();
            }
        };
    }

    public void onExpandView() {
        shelterRecyclerView.setVisibility(View.VISIBLE);
    }

    public void onCollapseView() {
        shelterRecyclerView.setVisibility(View.GONE);
    }
}
