package com.fyp.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.response.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetRepository {
    private final String TAG = PetRepository.class.getSimpleName();
    private ServerAPI serverAPI;

    public PetRepository() {
        serverAPI = RetrofitClient.getRetrofitInstance().create(ServerAPI.class);
    }

    public LiveData<List<Pet>> getAllPets() {
        final MutableLiveData<List<Pet>> data = new MutableLiveData<>();
        serverAPI.getAllPets().enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                Log.d(TAG, "getAllPets onResponse response:");
                if (response.body() != null) {
                    for (Pet pet : response.body()) {
                        Log.d(TAG, pet.toString());
                    }
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Log.d(TAG, "getAllPets onFailure response: " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }
}
