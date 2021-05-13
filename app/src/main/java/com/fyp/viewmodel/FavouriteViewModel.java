package com.fyp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.repository.FavouriteRepository;
import com.fyp.response.Pet;

import java.util.HashSet;
import java.util.List;

public class FavouriteViewModel extends ViewModel {
    private FavouriteRepository favouriteRepository;
    private MutableLiveData<List<Pet>> favouritePets;
    private MutableLiveData<HashSet<String>> favouritePetsIds;
    private MutableLiveData<Integer> codeResponse;

    private final static String TAG = FavouriteViewModel.class.getSimpleName();

    public LiveData<List<Pet>> getFavouritePets(String JWTToken) {
        Log.d(TAG, "FavouriteViewModel getFavouritePets");
        if (favouritePets == null) {
            Log.d(TAG, "Create NEW mutable live data with pets");
            loadAllFavourite(JWTToken);
        }
        return favouritePets;
    }

    public LiveData<HashSet<String>> getFavouritePetsIds(String JWTToken) {
        Log.d(TAG, "FavouriteViewModel getFavouritePetsIds");
        if (favouritePetsIds == null) {
            Log.d(TAG, "Create NEW mutable live data with ids");
            loadAllFavourite(JWTToken);
        }
        return favouritePetsIds;
    }

    public LiveData<Integer> getCodeResponse() {
        Log.d(TAG, "FavouriteViewModel getCodeResponse");
        if (codeResponse == null) {
            Log.d(TAG, "Create NEW mutable live data with code");
            codeResponse = new MutableLiveData<>();
        }
        return codeResponse;
    }

    public void loadAllFavourite(String JWTToken) {
        if (favouriteRepository == null) favouriteRepository = new FavouriteRepository();
        if (favouritePets == null) favouritePets = new MutableLiveData<>();
        if (favouritePetsIds == null) favouritePetsIds = new MutableLiveData<>();
        if (codeResponse == null) codeResponse = new MutableLiveData<>();

        favouriteRepository.getAllFavouritePets(favouritePets, favouritePetsIds, codeResponse, JWTToken);
    }

    public Pet getPet(int position) {
        Log.d(TAG, "Get pet at position " + position);
        if (favouritePets != null && favouritePets.getValue() != null) {
            return favouritePets.getValue().get(position);
        } else {
            return null;
        }
    }

    public void addFavourite(String JWTToken, Pet pet) {
        if (favouriteRepository == null) favouriteRepository = new FavouriteRepository();
        if (favouritePets == null) favouritePets = new MutableLiveData<>();
        if (codeResponse == null) codeResponse = new MutableLiveData<>();

        favouriteRepository.addFavourite(favouritePets, codeResponse, JWTToken, pet);
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

    public void removeFavourite(String JWTToken, Pet pet) {
        if (favouriteRepository == null) favouriteRepository = new FavouriteRepository();
        if (favouritePets == null) favouritePets = new MutableLiveData<>();
        if (codeResponse == null) codeResponse = new MutableLiveData<>();

        favouriteRepository.removeFavourite(favouritePets, codeResponse, JWTToken, pet);
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

    public void clearFavourite() {
        favouritePets.setValue(null);
    }
}
