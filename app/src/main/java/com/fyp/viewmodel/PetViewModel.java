package com.fyp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.repository.PetRepository;
import com.fyp.response.PetBody;


public class PetViewModel extends ViewModel {
    private PetRepository petRepository;

    private MutableLiveData<Integer> codeResponse;

    public PetViewModel() {
        petRepository = new PetRepository();
        codeResponse = new MutableLiveData<>();
    }

    public LiveData<Integer> createPet(String JWTToken, PetBody pet) {
        petRepository.addPet(JWTToken, pet, codeResponse);
        return codeResponse;
    }
}
