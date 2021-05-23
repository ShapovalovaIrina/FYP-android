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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyp.R;
import com.fyp.response.Pet;
import com.fyp.viewmodel.FavouriteViewModel;

import java.util.HashSet;

public class PagedCardPetAdapter extends PagedListAdapter<Pet, PagedCardPetAdapter.ViewHolder> {
    private final String TAG = PagedCardPetAdapter.class.getSimpleName();

    private HashSet<String> favouritePetsIds = new HashSet<>();
    private NavigationDirection navigationDirection;
    private FavouriteViewModel favouriteViewModel;
    private String idToken;
    private boolean isAuthenticated;

    private static DiffUtil.ItemCallback<Pet> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Pet>() {
                @Override
                public boolean areItemsTheSame(@NonNull Pet oldItem, @NonNull Pet newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Pet oldItem, @NonNull Pet newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class ViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = PagedCardPetAdapter.ViewHolder.class.getSimpleName();

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
            if (isAuthenticated) {
                isFavourite.setOnClickListener(onFavouriteButtonClickListener());
            } else {
                isFavourite.setVisibility(View.GONE);
            }
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
            if (isAuthenticated) {
                if (navigationDirection == NavigationDirection.FROM_SEARCH_TO_PET) {
                    isFavourite.setChecked(favouritePetsIds.contains(pet.getId()));
                } else {
                    isFavourite.setChecked(true);
                }
            }
        }

        View.OnClickListener onImageClickListener() {
            return this::navigateToPetFragment;
        }

        View.OnClickListener onFavouriteButtonClickListener() {
            return view -> {
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
            };
        }

        private void navigateToPetFragment(View view) {
            Bundle bundle = new Bundle();
            bundle.putInt("AbsoluteAdapterPosition", getAbsoluteAdapterPosition());
            bundle.putSerializable("NavigationDirection", navigationDirection);
            bundle.putBoolean("IsFavourite", isFavourite.isChecked());
            bundle.putBoolean("IsAuthenticated", isAuthenticated);

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

    public PagedCardPetAdapter(
            NavigationDirection navigationDirection,
            FavouriteViewModel favouriteViewModel,
            LifecycleOwner lifecycleOwner,
            String idToken) {
        super(DIFF_CALLBACK);
        this.navigationDirection = navigationDirection;
        this.favouriteViewModel = favouriteViewModel;
        this.idToken = idToken;
        favouriteViewModel.getFavouritePetsIds(idToken).observe(lifecycleOwner, strings -> favouritePetsIds = strings);
        isAuthenticated = true;
    }

    public PagedCardPetAdapter(NavigationDirection navigationDirection) {
        super(DIFF_CALLBACK);
        this.navigationDirection = navigationDirection;
        isAuthenticated = false;
    }

    @NonNull
    @Override
    public PagedCardPetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pet, parent, false);
        return new PagedCardPetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagedCardPetAdapter.ViewHolder holder, int position) {
        Pet pet = getItem(position);
        if (pet != null) holder.bind(pet);
    }
}
