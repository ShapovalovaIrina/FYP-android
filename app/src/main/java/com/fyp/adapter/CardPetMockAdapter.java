package com.fyp.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.R;
import com.fyp.pojo.PetMock;
import com.fyp.viewmodel.FavouriteMockViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CardPetMockAdapter extends RecyclerView.Adapter<CardPetMockAdapter.ViewHolder> {
    private final String TAG = CardPetMockAdapter.class.getSimpleName();

    private List<PetMock> petsList = new ArrayList<>();
    private HashSet<String> favouritePetsIds = new HashSet<>();
    private NavigationDirection navigationDirection;
    private FavouriteMockViewModel favouriteMockViewModel;

    class ViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = CardPetMockAdapter.ViewHolder.class.getName();

        private PetMock petMock;
        private ImageView image;
        private TextView name;
        private CheckBox isFavourite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_pet_image);
            name = itemView.findViewById(R.id.card_pet_name);
            isFavourite = itemView.findViewById(R.id.card_pet_favourite);

            image.setOnClickListener(onImageClickListener());
            isFavourite.setOnClickListener(onFavouriteButtonClickListener());
        }

        public void bind(PetMock pet) {
            petMock = pet;
            name.setText(pet.getName());
            image.setImageResource(pet.getResourceId());
            isFavourite.setChecked(favouritePetsIds.contains(pet.getId()));
        }

        View.OnClickListener onImageClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateToPetFragment(view);
                }
            };
        }

        View.OnClickListener onFavouriteButtonClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view instanceof CheckBox) {
                        CheckBox favouriteCheckBox = (CheckBox) view;
                        if (favouriteCheckBox.isChecked()) {
                            Log.d(TAG, "Add favourite pet with id " + petMock.getId());
                            favouriteMockViewModel.addFavourite(petMock);
                            favouriteMockViewModel.addFavouriteId(petMock.getId());
                        } else {
                            Log.d(TAG, "Remove favourite pet with id " + petMock.getId());
                            favouriteMockViewModel.removeFavourite(petMock);
                            favouriteMockViewModel.removeFavouriteId(petMock.getId());
                        }
                    }
                }
            };
        }

        private void navigateToPetFragment(View view) {
            Log.d(TAG, "ViewHolder, navigateToPetFragment. navigationDirection: " + navigationDirection);

            Bundle bundle = new Bundle();
            bundle.putInt("AbsoluteAdapterPosition", getAbsoluteAdapterPosition());
            bundle.putSerializable("NavigationDirection", navigationDirection);
            bundle.putBoolean("IsFavourite", isFavourite.isChecked());
            NavController navController = Navigation.findNavController(view);
            switch (navigationDirection) {
                case FROM_SEARCH_TO_PET:
                    navController.navigate(R.id.action_searchFragment_to_petFragment, bundle);
                    break;
                case FROM_FAVOURITE_TO_PET:
                    navController.navigate(R.id.action_favouriteFragment_to_petFragment, bundle);
                    break;
                default:
                    break;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pet, parent, false);
        return new ViewHolder(view);
    }

    public CardPetMockAdapter(NavigationDirection navigationDirection) {
        this.navigationDirection = navigationDirection;
    }

    public CardPetMockAdapter(List<PetMock> petsList, NavigationDirection navigationDirection) {
        this.petsList = petsList;
        this.navigationDirection = navigationDirection;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(petsList.get(position));
    }

    @Override
    public int getItemCount() {
        return petsList.size();
    }

    public void setItems(Collection<PetMock> items) {
        petsList.addAll(items);
        notifyDataSetChanged();
    }

    public void setFavouriteMockViewModel(FavouriteMockViewModel favouriteMockViewModel, LifecycleOwner lifecycleOwner) {
        this.favouriteMockViewModel = favouriteMockViewModel;
        favouriteMockViewModel.getFavouritePetsIds().observe(lifecycleOwner, new Observer<HashSet<String>>() {
            @Override
            public void onChanged(HashSet<String> strings) {
                favouritePetsIds = strings;
            }
        });
    }

    public void clearItems() {
        petsList.clear();
        notifyDataSetChanged();
    }
}
