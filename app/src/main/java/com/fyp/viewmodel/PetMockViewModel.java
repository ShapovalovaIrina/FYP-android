package com.fyp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.R;
import com.fyp.pojo.PetMock;
import com.fyp.response.Shelter;

import java.util.ArrayList;
import java.util.List;

public class PetMockViewModel extends ViewModel {
    private MutableLiveData<List<PetMock>> pets;

    private final String TAG = PetMockViewModel.class.getSimpleName();

    public LiveData<List<PetMock>> getPets() {
        Log.d(TAG, "View model get pets");
        if (pets == null) {
            Log.d(TAG, "Create NEW mutable live data with pets list");
            pets = new MutableLiveData<>();
        }
        return pets;
    }

    public PetMock getPet(int position) {
        Log.d(TAG, "View model get pet at position " + position);
        if (pets != null && pets.getValue() != null) {
            return pets.getValue().get(position);
        } else {
            return null;
        }
    }

    public void loadAllPets() {
        Log.d(TAG,"Create NEW list with pets");
        List<PetMock> petsList = new ArrayList<>();
        Shelter shelter = new Shelter();
        shelter.setId(1);
        shelter.setTitle("Тестовый приют");
        shelter.setVk_link("https://vk.com/habr");
        shelter.setSite_link("https://yandex.ru/");
        petsList.add(new PetMock("1", "First cat", R.drawable.pet_mock_image, shelter));
        petsList.add(new PetMock("2", "Second cat", R.drawable.pet_mock_image, shelter));
        petsList.add(new PetMock("3", "Third cat", R.drawable.pet_mock_image, shelter));
        petsList.add(new PetMock("4", "First dog", R.drawable.pet_mock_image, shelter));
        petsList.add(new PetMock("5", "Second dog", R.drawable.pet_mock_image, shelter));
        petsList.add(new PetMock("6", "Third dog", R.drawable.pet_mock_image, shelter));

        pets.setValue(petsList);
    }

    public void loadCats() {
        Log.d(TAG, "Create NEW list with cats");
        List<PetMock> petsList = new ArrayList<>();
        Shelter shelter = new Shelter();
        shelter.setId(1);
        shelter.setTitle("Тестовый приют");
        shelter.setVk_link("https://vk.com/habr");
        shelter.setSite_link("https://yandex.ru/");
        petsList.add(new PetMock("1", "First cat", R.drawable.pet_mock_image, shelter));
        petsList.add(new PetMock("2", "Second cat", R.drawable.pet_mock_image, shelter));
        petsList.add(new PetMock("3", "Third cat", R.drawable.pet_mock_image, shelter));

        pets.setValue(petsList);
    }

    public void loadDogs() {
        Log.d(TAG,"Create NEW list with dogs");
        List<PetMock> petsList = new ArrayList<>();
        Shelter shelter = new Shelter();
        shelter.setId(1);
        shelter.setTitle("Тестовый приют");
        shelter.setVk_link("https://vk.com/habr");
        shelter.setSite_link("https://yandex.ru/");
        petsList.add(new PetMock("4", "First dog", R.drawable.pet_mock_image, shelter));
        petsList.add(new PetMock("5", "Second dog", R.drawable.pet_mock_image, shelter));
        petsList.add(new PetMock("6", "Third dog", R.drawable.pet_mock_image, shelter));
        pets.setValue(petsList);
    }

    public void clearPets() {
        Log.d(TAG,"Clear pet list");
        List<PetMock> petsList = new ArrayList<>();
        pets.setValue(petsList);
    }
}
