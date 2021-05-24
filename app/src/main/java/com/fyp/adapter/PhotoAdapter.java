package com.fyp.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fyp.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<String> photosUrl = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private ImageView delete;
        private int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.photo_gallery_image);
            delete = itemView.findViewById(R.id.photo_gallery_delete);
            delete.bringToFront();

            delete.setOnClickListener(onDeleteButtonClickListener());
        }

        public void bind(String photoUrl, int position) {
            this.position = position;
            Glide.with(image)
                    .load(photoUrl)
                    .centerCrop()
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Toast.makeText(image.getContext(), "Ошибка во время загрузки изображения. Проверьте ссылку", Toast.LENGTH_SHORT).show();
                            removeItemByUrl(position);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .error(R.drawable.ic_baseline_image_24)
                    .into(image);
        }

        View.OnClickListener onDeleteButtonClickListener() {
            return view -> new MaterialAlertDialogBuilder(view.getContext())
                    .setTitle("Удаление")
                    .setMessage("Вы уверены, что хотите удалить эту фотографию?")
                    .setNegativeButton("Да", (dialogInterface, i) -> removeItemByUrl(position))
                    .setPositiveButton("Нет", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        }
    }

    @NonNull
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_photo, parent, false);
        return new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, int position) {
        holder.bind(photosUrl.get(position), position);
    }

    @Override
    public int getItemCount() {
        return photosUrl.size();
    }

    public List<String> getPhotosUrl() {
        return photosUrl;
    }

    public void addItem(String item) {
        photosUrl.add(item);
        notifyDataSetChanged();
    }

    public void removeItemByUrl(int position) {
        photosUrl.remove(position);
        notifyDataSetChanged();
    }
}
