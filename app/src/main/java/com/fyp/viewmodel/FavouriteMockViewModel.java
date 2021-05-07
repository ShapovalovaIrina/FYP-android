package com.fyp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.pojo.PetMock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FavouriteMockViewModel extends ViewModel {
    private MutableLiveData<List<PetMock>> favouritePets;
    private MutableLiveData<HashSet<String>> favouritePetsIds;

    private final String TAG = FavouriteMockViewModel.class.getSimpleName();

    public LiveData<List<PetMock>> getFavouritePets() {
        Log.d(TAG, "FavouriteMockViewModel getFavouritePets");
        if (favouritePets == null) {
            Log.d(TAG, "Create NEW mutable live data with pets");
            favouritePets = new MutableLiveData<>();
        }
        return favouritePets;
    }

    public LiveData<HashSet<String>> getFavouritePetsIds() {
        Log.d(TAG, "FavouriteMockViewModel getFavouritePetsIds");
        if (favouritePetsIds == null) {
            Log.d(TAG, "Create NEW mutable live data with ids");
            favouritePetsIds = new MutableLiveData<>();
            favouritePetsIds.setValue(new HashSet<>());
        }
        return favouritePetsIds;
    }

    public PetMock getPet(int position) {
        Log.d(TAG, "Get pet at position " + position);
        if (favouritePets != null && favouritePets.getValue() != null) {
            return favouritePets.getValue().get(position);
        } else {
            return null;
        }
    }

    public void addFavourite(PetMock mockPet) {
        if (favouritePets == null) {
            favouritePets = new MutableLiveData<>();
        }
        List<PetMock> list = favouritePets.getValue();
        if (list == null) list = new ArrayList<>();

        Log.d(TAG, "Add pet, before");
        for (PetMock pet : list) Log.d(TAG, pet.toString());

        list.add(mockPet);

        Log.d(TAG, "Add pet, after");
        for (PetMock pet : list) Log.d(TAG, pet.toString());
        favouritePets.setValue(list);
    }

    public void addFavouriteId(String petId) {
        if (favouritePetsIds == null) {
            favouritePetsIds = new MutableLiveData<>();
        }
        HashSet<String> list = favouritePetsIds.getValue();
        if (list == null) list = new HashSet<>();
        list.add(petId);
        favouritePetsIds.setValue(list);
    }

    public void removeFavourite(PetMock mockPet) {
        if (favouritePets == null) {
            favouritePets = new MutableLiveData<>();
        }
        List<PetMock> list = favouritePets.getValue();
        if (list == null) list = new ArrayList<>();

        Log.d(TAG, "Remove pet, before");
        for (PetMock pet : list) Log.d(TAG, pet.toString());

        list.removeIf(pet -> pet.getId().equals(mockPet.getId()));

        Log.d(TAG, "Remove pet, after");
        for (PetMock pet : list) Log.d(TAG, pet.toString());

        favouritePets.setValue(list);
    }

    public void removeFavouriteId(String petId) {
        if (favouritePetsIds == null) {
            favouritePetsIds = new MutableLiveData<>();
        }
        HashSet<String> list = favouritePetsIds.getValue();
        if (list == null) list = new HashSet<>();
        list.remove(petId);
        favouritePetsIds.setValue(list);
    }
}
