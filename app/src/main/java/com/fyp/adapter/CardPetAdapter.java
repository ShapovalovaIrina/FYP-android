package com.fyp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyp.R;
import com.fyp.response.Pet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardPetAdapter extends RecyclerView.Adapter<CardPetAdapter.ViewHolder> {
    private final String TAG = CardPetAdapter.class.getSimpleName();

    private List<Pet> petsList = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final String TAG = ViewHolder.class.getSimpleName();

        private ImageView image;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Log.d(TAG, "ViewHolder constructor. Init views");
            image = itemView.findViewById(R.id.card_pet_image);
            name = itemView.findViewById(R.id.card_pet_name);
        }

        public void bind(Pet pet) {
            Log.d(TAG, "ViewHolder bind pet with name " + pet.getName());
            name.setText(pet.getName());
            Glide.with(image)
                    .load(pet.getFirstPhoto())
                    .centerCrop()
                    .error(R.drawable.ic_baseline_image_24)
                    .into(image);
        }

        @Override
        public void onClick(View view) {
            // TODO do something
        }
    }

    @NonNull
    @Override
    public CardPetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pet, parent, false);
        return new CardPetAdapter.ViewHolder(view);
    }

    public CardPetAdapter() {
    }

    public CardPetAdapter(List<Pet> petsList) {
        this.petsList = petsList;
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
        Log.d(TAG, "CardPetAdapter setItems");
        petsList.addAll(items);
        notifyDataSetChanged();
    }

    public void clearItems() {
        petsList.clear();
        notifyDataSetChanged();
    }
}
