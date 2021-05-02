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

    public void loadAllPets() {
        // TODO an asynchronous operation to fetch pets.
        System.out.println("Create new list with pets");
        List<Pet> petsList = new ArrayList<>();
        petsList.add(new Pet("First cat", R.drawable.pet_mock_image));
        petsList.add(new Pet("Second cat", R.drawable.pet_mock_image));
        petsList.add(new Pet("Third cat", R.drawable.pet_mock_image));
        petsList.add(new Pet("First dog", R.drawable.pet_mock_image));
        petsList.add(new Pet("Second dog", R.drawable.pet_mock_image));
        petsList.add(new Pet("Third dog", R.drawable.pet_mock_image));

        pets.setValue(petsList);
    }

    public void loadCats() {
        // TODO an asynchronous operation to fetch pets.
        System.out.println("Create new list with cats");
        List<Pet> petsList = new ArrayList<>();
        petsList.add(new Pet("First cat", R.drawable.pet_mock_image));
        petsList.add(new Pet("Second cat", R.drawable.pet_mock_image));
        petsList.add(new Pet("Third cat", R.drawable.pet_mock_image));

        pets.setValue(petsList);
    }

    public void loadDogs() {
        // TODO an asynchronous operation to fetch pets.
        System.out.println("Create new list with dogs");
        List<Pet> petsList = new ArrayList<>();
        petsList.add(new Pet("First dog", R.drawable.pet_mock_image));
        petsList.add(new Pet("Second dog", R.drawable.pet_mock_image));
        petsList.add(new Pet("Third dog", R.drawable.pet_mock_image));
        pets.setValue(petsList);
    }

    public void clearPets() {
        // TODO an asynchronous operation to fetch pets.
        System.out.println("Clear pet list");
        List<Pet> petsList = new ArrayList<>();
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
