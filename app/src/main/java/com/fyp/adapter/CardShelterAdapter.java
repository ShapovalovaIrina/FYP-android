package com.fyp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.R;
import com.fyp.response.Shelter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardShelterAdapter extends RecyclerView.Adapter<CardShelterAdapter.ViewHolder> {
    private List<Shelter> shelterList = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private LinearLayout contactsLayout;
        private CheckBox showContacts;
        private Button vkLink;
        private Button siteLink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_shelter_title);
            contactsLayout = itemView.findViewById(R.id.card_shelter_contacts_layout);
            showContacts = itemView.findViewById(R.id.card_shelter_contacts_button);
            vkLink = itemView.findViewById(R.id.card_shelter_vk_link);
            siteLink = itemView.findViewById(R.id.card_shelter_site_link);

            contactsLayout.setVisibility(View.GONE);
            showContacts.setOnCheckedChangeListener(showContactsOnCheckedChangeListener());
        }

        public void bind(Shelter shelter) {
            title.setText(shelter.getTitle());
            vkLink.setOnClickListener(onLinkClickListener(shelter.getVk_link()));
            siteLink.setOnClickListener(onLinkClickListener(shelter.getSite_link()));
        }

        CompoundButton.OnCheckedChangeListener showContactsOnCheckedChangeListener() {
            return (compoundButton, isChecked) -> {
                if (isChecked) {
                    onExpandContacts();
                } else {
                    onCollapseContacts();
                }
            };
        }

        private void onExpandContacts() {
            contactsLayout.setVisibility(View.VISIBLE);
        }

        private void onCollapseContacts() {
            contactsLayout.setVisibility(View.GONE);
        }

        View.OnClickListener onLinkClickListener(String link) {
            return view -> {};
        }
    }

    @NonNull
    @Override
    public CardShelterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_shelter, parent, false);
        return new CardShelterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardShelterAdapter.ViewHolder holder, int position) {
        holder.bind(shelterList.get(position));
    }

    @Override
    public int getItemCount() {
        return shelterList.size();
    }

    public void setItems(Collection<Shelter> items) {
        shelterList.addAll(items);
        notifyDataSetChanged();
    }
}
