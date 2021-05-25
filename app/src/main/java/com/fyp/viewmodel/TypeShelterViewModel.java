package com.fyp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.repository.TypeShelterRepository;
import com.fyp.response.Shelter;
import com.fyp.response.Type;

import java.util.ArrayList;
import java.util.List;

import static com.fyp.constant.Constants.SERVER_ENABLED;

public class TypeShelterViewModel extends ViewModel {
    private static final String TAG = TypeShelterViewModel.class.getSimpleName();

    private TypeShelterRepository typeShelterRepository;
    private MutableLiveData<List<Shelter>> shelterLiveData;
    private MutableLiveData<List<Type>> typeLiveData;
    private MutableLiveData<Integer> codeResponse;

    public LiveData<List<Shelter>> getShelters() {
        Log.d(TAG, "Get shelters");
        if (shelterLiveData == null) {
            shelterLiveData = new MutableLiveData<>();
            Log.d(TAG, "shelterLiveData = NULL");
            loadShelters();
        }
        return shelterLiveData;
    }

    public MutableLiveData<List<Type>> getTypes() {
        Log.d(TAG, "Get types");
        if (typeLiveData == null) {
            typeLiveData = new MutableLiveData<>();
            Log.d(TAG, "typeLiveData = NULL");
            loadTypes();
        }
        return typeLiveData;
    }

    private void loadShelters() {
        if (typeShelterRepository == null) typeShelterRepository = new TypeShelterRepository();
        if (SERVER_ENABLED) {
            typeShelterRepository.getAllShelters(shelterLiveData);
        } else {
            loadFakeDataShelter(shelterLiveData);
        }
    }

    public LiveData<Integer> createShelter(String JWTToken, Shelter newShelter) {
        if (typeShelterRepository == null) typeShelterRepository = new TypeShelterRepository();
        if (codeResponse == null) codeResponse = new MutableLiveData<>();
        if (SERVER_ENABLED) {
            typeShelterRepository.createShelter(JWTToken, newShelter, shelterLiveData, codeResponse);
        } else {
            addFakeShelterData(newShelter);
        }
        return codeResponse;
    }

    private void loadTypes() {
        if (typeShelterRepository == null) typeShelterRepository = new TypeShelterRepository();
        if (SERVER_ENABLED) {
            typeShelterRepository.getAllTypes(typeLiveData);
        } else {
            loadFakeDataTypes(typeLiveData);
        }
    }

    public void loadFakeDataShelter(MutableLiveData<List<Shelter>> data) {
        List<Shelter> list = new ArrayList<>();
        list.add(new Shelter(1, "Test shelter 1", "https://paperpaper.ru/", "https://vk.com/paperpaper_ru"));
        list.add(new Shelter(2, "Test shelter 2", null, "https://vk.com/paperpaper_ru"));
        list.add(new Shelter(3, "Test shelter 3", "https://paperpaper.ru/", null));
        list.add(new Shelter(4, "Test shelter 4", "https://paperpaper.ru/", "https://vk.com/paperpaper_ru"));
        data.setValue(list);
    }

    private void addFakeShelterData(Shelter newShelter) {
        List<Shelter> shelters = shelterLiveData.getValue();
        if (shelters == null) shelters = new ArrayList<>();
        shelters.add(newShelter);
        shelterLiveData.setValue(shelters);
        codeResponse.setValue(201);
    }

    public void loadFakeDataTypes(MutableLiveData<List<Type>> data) {
        List<Type> list = new ArrayList<>();
        list.add(new Type(1, "Котик"));
        list.add(new Type(2, "Собака"));
        data.setValue(list);
    }
}
