package com.fyp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.repository.PetRepository;
import com.fyp.response.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetViewModel extends ViewModel {
    private PetRepository petRepository;
    private MutableLiveData<List<Pet>> petResponse;

    private final static String TAG = PetViewModel.class.getSimpleName();

    public LiveData<List<Pet>> getPets() {
        Log.d(TAG, "PetNetworkViewModel get pets");
        if (petResponse == null) {
            Log.d(TAG, "Create new mutable live data with pets list");
            petResponse = new MutableLiveData<>();
        }
        return petResponse;
    }

    public void loadAllPets() {
//        if (petRepository == null) {
//            petRepository = new PetRepository();
//        }
//        petResponse.setValue(petRepository.getAllPets().getValue());

        ServerAPI serverAPI = RetrofitClient.getRetrofitInstance().create(ServerAPI.class);
        serverAPI.getAllPets().enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                Log.d(TAG, "loadAllPets onResponse response:");
                if (response.body() != null) {
                    for (Pet pet : response.body()) {
                        Log.d(TAG, pet.toString());
                    }
                    petResponse.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Log.d(TAG, "loadAllPets onFailure response: " + t.getMessage());
                petResponse.setValue(null);
            }
        });
    }

    public void clearPets() {
        petResponse.setValue(null);
    }
}
