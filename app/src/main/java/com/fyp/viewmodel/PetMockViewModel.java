package com.fyp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.R;
import com.fyp.pojo.PetMock;

import java.util.ArrayList;
import java.util.List;

public class PetMockViewModel extends ViewModel {
    private MutableLiveData<List<PetMock>> pets;
    private MutableLiveData<List<PetMock>> favouritePets;

    private final String TAG = PetMockViewModel.class.getSimpleName();

    public LiveData<List<PetMock>> getPets() {
        System.out.println("View model get pets");
        if (pets == null) {
            System.out.println("Create new mutable live data with pets list");
            pets = new MutableLiveData<List<PetMock>>();
        }
        return pets;
    }

    public LiveData<List<PetMock>> getFavouritePets() {
        System.out.println("View model get favourite pets");
        if (favouritePets == null) {
            favouritePets = new MutableLiveData<List<PetMock>>();
            loadFavouritePets();
        }
        return favouritePets;
    }

    public void loadAllPets() {
        System.out.println("Create new list with pets");
        List<PetMock> petsList = new ArrayList<>();
        petsList.add(new PetMock("First cat", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Second cat", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Third cat", R.drawable.pet_mock_image));
        petsList.add(new PetMock("First dog", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Second dog", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Third dog", R.drawable.pet_mock_image));

        pets.setValue(petsList);
    }

    public void loadCats() {
        System.out.println("Create new list with cats");
        List<PetMock> petsList = new ArrayList<>();
        petsList.add(new PetMock("First cat", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Second cat", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Third cat", R.drawable.pet_mock_image));

        pets.setValue(petsList);
    }

    public void loadDogs() {
        System.out.println("Create new list with dogs");
        List<PetMock> petsList = new ArrayList<>();
        petsList.add(new PetMock("First dog", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Second dog", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Third dog", R.drawable.pet_mock_image));
        pets.setValue(petsList);
    }

    public void clearPets() {
        System.out.println("Clear pet list");
        List<PetMock> petsList = new ArrayList<>();
        pets.setValue(petsList);
    }

    private void loadFavouritePets() {
        System.out.println("Create new list with favourite pets");
        List<PetMock> petsList = new ArrayList<>();
        petsList.add(new PetMock("First favourite pet", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Second favourite pet", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Third favourite pet", R.drawable.pet_mock_image));
        favouritePets.setValue(petsList);
    }
}
