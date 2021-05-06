package com.fyp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.repository.FavouriteRepository;
import com.fyp.response.Pet;

import java.util.List;

public class FavouriteViewModel extends ViewModel {
    private FavouriteRepository favouriteRepository;
    private MutableLiveData<List<Pet>> favouritePets;
    private MutableLiveData<Integer> codeResponse;

    private final static String TAG = FavouriteViewModel.class.getSimpleName();

    public LiveData<List<Pet>> getFavouritePets(String JWTToken) {
        Log.d(TAG, "FavouriteViewModel getFavouritePets");
        if (favouritePets == null) {
            Log.d(TAG, "Create NEW mutable live data with pets");
            favouritePets = new MutableLiveData<>();
            loadAllFavourite(JWTToken);
        }
        return favouritePets;
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
        if (favouriteRepository == null) {
            favouriteRepository = new FavouriteRepository();
        }
        favouriteRepository.getAllFavouritePets(favouritePets, codeResponse, JWTToken);
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
        if (favouriteRepository == null) {
            favouriteRepository = new FavouriteRepository();
        }
        favouriteRepository.addFavourite(favouritePets, codeResponse, JWTToken, pet);
    }

    public void removeFavourite(String JWTToken, Pet pet) {
        if (favouriteRepository == null) {
            favouriteRepository = new FavouriteRepository();
        }
        favouriteRepository.removeFavourite(favouritePets, codeResponse, JWTToken, pet);
    }

    public void clearFavourite() {
        favouritePets.setValue(null);
    }
}
