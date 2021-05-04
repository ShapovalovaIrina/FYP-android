package com.fyp.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.response.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteRepository {
    private final String TAG = FavouriteRepository.class.getSimpleName();
    private ServerAPI serverAPI;

    public FavouriteRepository() {
        serverAPI = RetrofitClient.getRetrofitInstance().create(ServerAPI.class);
    }

    public void getAllFavouritePets(final MutableLiveData<List<Pet>> data, String JWTToken) {
        serverAPI.getAllFavourite(JWTToken).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                Log.d(TAG, "getAllFavouritePets onResponse response");
                switch (response.code()) {
                    case 401:
                        Log.d(TAG, "getAllFavouritePets onResponse 401 Unauthenticated");
                        break;
                    case 403:
                        Log.d(TAG, "getAllFavouritePets onResponse 403 Access forbidden");
                        break;
                    case 200:
                        if (response.body() != null) {
                            for (Pet pet : response.body()) {
                                Log.d(TAG, pet.toString());
                            }
                        }
                        data.setValue(response.body());
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Log.d(TAG, "getAllFavouritePets onFailure response: " + t.getMessage());
                data.setValue(null);
            }
        });
    }
}
