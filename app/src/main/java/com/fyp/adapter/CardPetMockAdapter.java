package com.fyp.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.R;
import com.fyp.pojo.PetMock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardPetMockAdapter extends RecyclerView.Adapter<CardPetMockAdapter.ViewHolder> {
    private List<PetMock> petsList = new ArrayList<>();
    private NavigationDirection navigationDirection;

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_pet_image);
            name = itemView.findViewById(R.id.card_pet_name);

            image.setOnClickListener(onImageClickListener());
        }

        public void bind(PetMock pet) {
            name.setText(pet.getName());
            image.setImageResource(pet.getResourceId());
        }

        View.OnClickListener onImageClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateToPetFragment(view);
                }
            };
        }

        private void navigateToPetFragment(View view) {
            Bundle bundle = new Bundle();
            bundle.putInt("AbsoluteAdapterPosition", getAbsoluteAdapterPosition());
            bundle.putSerializable("NavigationDirection", navigationDirection);
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

    public void clearItems() {
        petsList.clear();
        notifyDataSetChanged();
    }
}
