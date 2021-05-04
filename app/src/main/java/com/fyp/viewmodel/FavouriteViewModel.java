package com.fyp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.repository.FavouriteRepository;
import com.fyp.response.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
//        if (favouriteRepository == null) {
//            favouriteRepository = new FavouriteRepository();
//        }
//        favouriteResponse.setValue(favouriteRepository.getAllFavouritePets().getValue());

        ServerAPI serverAPI = RetrofitClient.getRetrofitInstance().create(ServerAPI.class);
        serverAPI.getAllFavourite(JWTToken).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                Log.d(TAG, "loadAllFavouritePets onResponse response:");
                if (response.code() == 401) {
                    Log.d(TAG, "loadAllFavouritePets onResponse 401 Unauthenticated");
                } else if (response.code() == 403) {
                    Log.d(TAG, "loadAllFavouritePets onResponse 403 Access forbidden");
                }
                if (response.body() != null) {
                    for (Pet pet : response.body()) {
                        Log.d(TAG, pet.toString());
                    }
                }
                favouriteResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Log.d(TAG, "loadAllFavouritePets onFailure response: " + t.getMessage());
                favouriteResponse.setValue(null);
            }
        });
    }

    public void clearFavourite() {
        favouriteResponse.setValue(null);
    }
}
