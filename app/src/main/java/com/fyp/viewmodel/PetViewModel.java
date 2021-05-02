package com.fyp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.R;
import com.fyp.pojo.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetViewModel extends ViewModel {
    private MutableLiveData<List<Pet>> pets;
    private MutableLiveData<List<Pet>> favouritePets;

    public LiveData<List<Pet>> getPets() {
        System.out.println("View model get pets");
        if (pets == null) {
            pets = new MutableLiveData<List<Pet>>();
            loadPets();
        }
        return pets;
    }

    public LiveData<List<Pet>> getFavouritePets() {
        System.out.println("View model get favourite pets");
        if (favouritePets == null) {
            favouritePets = new MutableLiveData<List<Pet>>();
            loadFavouritePets();
        }
        return favouritePets;
    }

    private void loadPets() {
        // TODO an asynchronous operation to fetch pets.
        System.out.println("Create new list with pets");
        List<Pet> petsList = new ArrayList<>();
        petsList.add(new Pet("First pet", R.drawable.pet_mock_image));
        petsList.add(new Pet("Second pet", R.drawable.pet_mock_image));
        petsList.add(new Pet("Third pet", R.drawable.pet_mock_image));
        pets.setValue(petsList);
    }

    private void loadFavouritePets() {
        // TODO an asynchronous operation to fetch pets.
        System.out.println("Create new list with favourite pets");
        List<Pet> petsList = new ArrayList<>();
        petsList.add(new Pet("First favourite pet", R.drawable.pet_mock_image));
        petsList.add(new Pet("Second favourite pet", R.drawable.pet_mock_image));
        petsList.add(new Pet("Third favourite pet", R.drawable.pet_mock_image));
        favouritePets.setValue(petsList);
    }
}
