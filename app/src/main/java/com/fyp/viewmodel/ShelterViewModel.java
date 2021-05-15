package com.fyp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.repository.ShelterRepository;
import com.fyp.response.Shelter;

import java.util.ArrayList;
import java.util.List;

import static com.fyp.constant.Constants.SERVER_ENABLED;

public class ShelterViewModel extends ViewModel {
    private static final String TAG = ShelterViewModel.class.getSimpleName();

    private ShelterRepository shelterRepository;
    private MutableLiveData<List<Shelter>> shelterLiveData;

    public LiveData<List<Shelter>> getShelters() {
        Log.d(TAG, "Get shelters");
        if (shelterLiveData == null) {
            shelterLiveData = new MutableLiveData<>();
            Log.d(TAG, "shelterLiveData = NULL");
            loadShelters();
        }
        return shelterLiveData;
    }

    private void loadShelters() {
        if (shelterRepository == null) shelterRepository = new ShelterRepository();
        if (SERVER_ENABLED) {
            shelterRepository.getAllShelters(shelterLiveData);
        } else {
            loadFakeData(shelterLiveData);
        }
    }

    public void loadFakeData(MutableLiveData<List<Shelter>> data) {
        List<Shelter> list = new ArrayList<>();
        list.add(new Shelter(2, "Test shelter 2", "", ""));
        list.add(new Shelter(3, "Test shelter 3", "", ""));
        list.add(new Shelter(4, "Test shelter 4", "", ""));
        list.add(new Shelter(5, "Test shelter 5", "", ""));
        data.setValue(list);
    }
}
