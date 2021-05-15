package com.fyp.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.response.Shelter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShelterRepository {
    private final String TAG = ShelterRepository.class.getSimpleName();
    private ServerAPI serverAPI;

    public ShelterRepository() {
        serverAPI = RetrofitClient.getRetrofitInstance().create(ServerAPI.class);
    }

    public void getAllShelters(final MutableLiveData<List<Shelter>> data) {
        serverAPI.getAllShelters().enqueue(new Callback<List<Shelter>>() {
            @Override
            public void onResponse(Call<List<Shelter>> call, Response<List<Shelter>> response) {
                Log.d(TAG, "getAllShelters onResponse response:");
                if (response.body() != null) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Shelter>> call, Throwable t) {
                Log.d(TAG, "getAllShelters onFailure response: " + t.getMessage());
                data.setValue(null);
            }
        });
    }
}
