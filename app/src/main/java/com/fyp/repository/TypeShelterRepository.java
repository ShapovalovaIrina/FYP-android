package com.fyp.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.response.Shelter;
import com.fyp.response.Status;
import com.fyp.response.Type;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypeShelterRepository {
    private final String TAG = TypeShelterRepository.class.getSimpleName();
    private ServerAPI serverAPI;

    public TypeShelterRepository() {
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

    public void getAllTypes(final MutableLiveData<List<Type>> data) {
        serverAPI.getAllTypes().enqueue(new Callback<List<Type>>() {
            @Override
            public void onResponse(Call<List<Type>> call, Response<List<Type>> response) {
                Log.d(TAG, "getAllTypes onResponse response:");
                if (response.body() != null) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Type>> call, Throwable t) {
                Log.d(TAG, "getAllTypes onFailure response: " + t.getMessage());
                data.setValue(null);
            }
        });
    }

    public void createShelter(
            String JWTToken,
            Shelter shelter,
            MutableLiveData<List<Shelter>> data,
            MutableLiveData<Integer> codeResponse) {
        serverAPI.createShelter(JWTToken, shelter).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Log.d(TAG, "createShelter onResponse response:");
                switch (response.code()) {
                    case 400:
                        Log.d(TAG, "createShelter onResponse 400 Bad status");
                        codeResponse.setValue(400);
                        break;
                    case 401:
                        Log.d(TAG, "createShelter onResponse 401 Unauthenticated");
                        codeResponse.setValue(401);
                        break;
                    case 403:
                        Log.d(TAG, "createShelter onResponse 403 Access forbidden");
                        codeResponse.setValue(403);
                        break;
                    case 201:
                        List<Shelter> shelters = data.getValue();
                        if (shelters == null) shelters = new ArrayList<>();
                        shelters.add(shelter);

                        data.setValue(shelters);
                        codeResponse.setValue(201);
                        break;
                    default:
                        Log.d(TAG, "createShelter onResponse other code " + response.code());
                        codeResponse.setValue(500);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "createShelter onFailure response: " + t.getMessage());
                codeResponse.setValue(500);
            }
        });
    }
}
