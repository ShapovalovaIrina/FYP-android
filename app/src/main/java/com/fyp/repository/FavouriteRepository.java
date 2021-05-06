package com.fyp.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.response.Pet;

import java.util.ArrayList;
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

    public void getAllFavouritePets(
            final MutableLiveData<List<Pet>> data,
            final MutableLiveData<Integer> codeResponse,
            String JWTToken) {
        serverAPI.getAllFavourite(JWTToken).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                Log.d(TAG, "getAllFavouritePets onResponse response");
                switch (response.code()) {
                    case 401:
                        Log.d(TAG, "getAllFavouritePets onResponse 401 Unauthenticated");
                        codeResponse.setValue(401);
                        break;
                    case 403:
                        Log.d(TAG, "getAllFavouritePets onResponse 403 Access forbidden");
                        codeResponse.setValue(403);
                        break;
                    case 200:
                        if (response.body() != null) {
                            for (Pet pet : response.body()) {
                                Log.d(TAG, pet.toString());
                            }
                        }
                        codeResponse.setValue(200);
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

    public void addFavourite(
            final MutableLiveData<List<Pet>> data,
            final MutableLiveData<Integer> codeResponse,
            final String JWTToken,
            final Pet pet) {
        serverAPI.addFavourite(JWTToken, pet.getId()).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                Log.d(TAG, "addFavourite onResponse response");
                switch (response.code()) {
                    case 400:
                        Log.d(TAG, "addFavourite onResponse 400 Bad request");
                        codeResponse.setValue(400);
                        break;
                    case 401:
                        Log.d(TAG, "addFavourite onResponse 401 Unauthenticated");
                        codeResponse.setValue(401);
                        break;
                    case 403:
                        Log.d(TAG, "addFavourite onResponse 403 Access forbidden");
                        codeResponse.setValue(403);
                        break;
                    case 404:
                        Log.d(TAG, "addFavourite onResponse 404 Not found");
                        codeResponse.setValue(404);
                        break;
                    case 200:
                        List<Pet> petList = data.getValue();
                        if (petList == null) petList = new ArrayList<>();
                        petList.add(pet);

                        data.setValue(petList);
                        codeResponse.setValue(200);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Log.d(TAG, "addFavourite onFailure response: " + t.getMessage());
            }
        });
    }

    public void removeFavourite(
            final MutableLiveData<List<Pet>> data,
            final MutableLiveData<Integer> codeResponse,
            final String JWTToken,
            final Pet pet) {
        serverAPI.removeFavourite(JWTToken, pet.getId()).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                Log.d(TAG, "removeFavourite onResponse response");
                switch (response.code()) {
                    case 400:
                        Log.d(TAG, "removeFavourite onResponse 400 Bad request");
                        codeResponse.setValue(400);
                        break;
                    case 401:
                        Log.d(TAG, "removeFavourite onResponse 401 Unauthenticated");
                        codeResponse.setValue(401);
                        break;
                    case 403:
                        Log.d(TAG, "removeFavourite onResponse 403 Access forbidden");
                        codeResponse.setValue(403);
                        break;
                    case 404:
                        Log.d(TAG, "removeFavourite onResponse 404 Not found");
                        codeResponse.setValue(404);
                        break;
                    case 200:
                        List<Pet> petList = data.getValue();
                        if (petList == null) petList = new ArrayList<>();
                        petList.remove(pet);

                        data.setValue(petList);
                        codeResponse.setValue(200);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Log.d(TAG, "removeFavourite onFailure response: " + t.getMessage());
            }
        });
    }
}
