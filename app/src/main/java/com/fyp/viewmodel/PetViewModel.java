package com.fyp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.repository.PetRepository;
import com.fyp.response.Pet;

import java.util.List;

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
        if (petRepository == null) {
            petRepository = new PetRepository();
        }
        petRepository.getAllPets(petResponse);
    }

    public void clearPets() {
        petResponse.setValue(null);
    }
}
