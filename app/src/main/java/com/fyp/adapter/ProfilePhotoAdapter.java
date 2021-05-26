package com.fyp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyp.R;

import java.util.List;

public class ProfilePhotoAdapter extends RecyclerView.Adapter<ProfilePhotoAdapter.ViewHolder> {
    private List<String> photosUrl;

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_pet_photo);
        }

        public void bind(String photoUrl) {
            Glide.with(image)
                    .load(photoUrl)
                    .centerCrop()
                    .error(R.drawable.ic_baseline_image_24)
                    .into(image);
        }
    }

    public ProfilePhotoAdapter(List<String> photosUrl) {
        this.photosUrl = photosUrl;
    }

    @NonNull
    @Override
    public ProfilePhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_profile_pet_photo, parent, false);
        return new ProfilePhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePhotoAdapter.ViewHolder holder, int position) {
        holder.bind(photosUrl.get(position));
    }

    @Override
    public int getItemCount() {
        return photosUrl.size();
    }
}
