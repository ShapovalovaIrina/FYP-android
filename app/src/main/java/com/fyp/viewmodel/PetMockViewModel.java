package com.fyp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.R;
import com.fyp.pojo.PetMock;
import com.fyp.response.Shelter;
import com.fyp.response.Type;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void loadAllPets(String typeFilter, String shelterFilter) {
        Log.d(TAG,"Create NEW list with pets");
        List<PetMock> petsList = new ArrayList<>();
        Shelter shelter = new Shelter(1, "Тестовый приют", "https://vk.com/habr", "https://yandex.ru/");
        Type cat = new Type(1, "Котик");
        Type dog = new Type(2, "Собака");

        petsList.add(new PetMock("1", "First cat", R.drawable.cat_mock_image, cat, shelter));
        petsList.add(new PetMock("2", "Second cat", R.drawable.cat_mock_image, cat, shelter));
        petsList.add(new PetMock("3", "Third cat", R.drawable.cat_mock_image, cat, shelter));
        petsList.add(new PetMock("4", "First dog", R.drawable.dog_mock_image, dog, shelter));
        petsList.add(new PetMock("5", "Second dog", R.drawable.dog_mock_image, dog, shelter));
        petsList.add(new PetMock("6", "Third dog", R.drawable.dog_mock_image, dog, shelter));

        if (typeFilter != null) {
            List<String> typeFilterArray = Arrays.asList(typeFilter.split(","));
            petsList.removeIf(p -> !typeFilterArray.contains(Integer.toString(p.getType().getId())));
        }
        if (shelterFilter != null) {
            List<String> shelterFilterArray = Arrays.asList(shelterFilter.split(","));
            petsList.removeIf(p -> !shelterFilterArray.contains(Integer.toString(p.getShelter().getId())));
        }

        pets.setValue(petsList);
    }

    public void clearPets() {
        Log.d(TAG,"Clear pet list");
        List<PetMock> petsList = new ArrayList<>();
        pets.setValue(petsList);
    }
}
