package com.fyp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.R;
import com.fyp.pojo.PetMock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardPetMockAdapter extends RecyclerView.Adapter<CardPetMockAdapter.ViewHolder> {
    private List<PetMock> petsList = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_pet_image);
            name = itemView.findViewById(R.id.card_pet_name);
        }

        public void bind(PetMock pet) {
            name.setText(pet.getName());
            image.setImageResource(pet.getResourceId());
        }

        @Override
        public void onClick(View view) {
            // TODO do something
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pet, parent, false);
        return new ViewHolder(view);
    }

    public CardPetMockAdapter() {}

    public CardPetMockAdapter(List<PetMock> petsList) {
        this.petsList = petsList;
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