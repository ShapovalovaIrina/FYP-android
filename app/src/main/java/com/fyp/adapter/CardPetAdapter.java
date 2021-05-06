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

import com.bumptech.glide.Glide;
import com.fyp.R;
import com.fyp.response.Pet;
import com.fyp.viewmodel.FavouriteViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CardPetAdapter extends RecyclerView.Adapter<CardPetAdapter.ViewHolder> {
    private final String TAG = CardPetAdapter.class.getSimpleName();

    private List<Pet> petsList = new ArrayList<>();
    private HashSet<String> favouritePetsIds = new HashSet<>();
    private NavigationDirection navigationDirection;
    private FavouriteViewModel favouriteViewModel;
    private String idToken;

    class ViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = ViewHolder.class.getSimpleName();

        private Pet pet;
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

        public void bind(Pet pet) {
            Log.d(TAG, "ViewHolder bind pet with name " + pet.getName());

            this.pet = pet;
            name.setText(pet.getName());
            Glide.with(image)
                    .load(pet.getFirstPhoto())
                    .centerCrop()
                    .error(R.drawable.ic_baseline_image_24)
                    .into(image);
            if (navigationDirection == NavigationDirection.FROM_SEARCH_TO_PET) {
                isFavourite.setChecked(favouritePetsIds.contains(pet.getId()));
            } else {
                isFavourite.setChecked(true);
            }
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
                            Log.d(TAG, "Add favourite pet with id " + pet.getId());
                            favouriteViewModel.addFavourite(idToken, pet);
                            favouriteViewModel.addFavouriteId(pet.getId());
                        } else {
                            Log.d(TAG, "Remove favourite pet with id " + pet.getId());
                            favouriteViewModel.removeFavourite(idToken, pet);
                            favouriteViewModel.removeFavouriteId(pet.getId());
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
    public CardPetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pet, parent, false);
        return new CardPetAdapter.ViewHolder(view);
    }

    public CardPetAdapter(NavigationDirection navigationDirection) {
        this.navigationDirection = navigationDirection;
    }

    public CardPetAdapter(List<Pet> petsList, NavigationDirection navigationDirection) {
        this.petsList = petsList;
        this.navigationDirection = navigationDirection;
    }

    @Override
    public void onBindViewHolder(@NonNull CardPetAdapter.ViewHolder holder, int position) {
        holder.bind(petsList.get(position));
    }

    @Override
    public int getItemCount() {
        return petsList.size();
    }

    public void setItems(Collection<Pet> items) {
        petsList.addAll(items);
        notifyDataSetChanged();
    }

    public void setFavouriteViewModel(FavouriteViewModel favouriteViewModel, LifecycleOwner lifecycleOwner, String idToken) {
        this.favouriteViewModel = favouriteViewModel;
        this.idToken = idToken;
        favouriteViewModel.getFavouritePetsIds(idToken).observe(lifecycleOwner, new Observer<HashSet<String>>() {
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
