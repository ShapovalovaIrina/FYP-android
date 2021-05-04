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
    private MutableLiveData<List<Pet>> favouriteResponse;

    private final static String TAG = FavouriteViewModel.class.getSimpleName();

    public LiveData<List<Pet>> getFavouritePets(String JWTToken) {
        Log.d(TAG, "FavouriteViewModel get pets");
        if (favouriteResponse == null) {
            Log.d(TAG, "Create new mutable live data with pets list");
            favouriteResponse = new MutableLiveData<>();
            loadAllFavourite(JWTToken);
        }
        return favouriteResponse;
    }

    public void loadAllFavourite(String JWTToken) {
        if (favouriteRepository == null) {
            favouriteRepository = new FavouriteRepository();
        }
        favouriteRepository.getAllFavouritePets(favouriteResponse, JWTToken);
    }

    public void clearFavourite() {
        favouriteResponse.setValue(null);
    }
}
