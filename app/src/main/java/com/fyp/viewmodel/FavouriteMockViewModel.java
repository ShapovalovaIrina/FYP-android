package com.fyp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.R;
import com.fyp.pojo.PetMock;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMockViewModel extends ViewModel {
    private MutableLiveData<List<PetMock>> favouritePets;

    private final String TAG = FavouriteMockViewModel.class.getSimpleName();

    public LiveData<List<PetMock>> getFavouritePets() {
        Log.d(TAG, "FavouriteMockViewModel get favourite pets");
        if (favouritePets == null) {
            favouritePets = new MutableLiveData<List<PetMock>>();
            loadFavouritePets();
        }
        return favouritePets;
    }

    public PetMock getPet(int position) {
        Log.d(TAG, "View model get pet at position " + position);
        if (favouritePets != null && favouritePets.getValue() != null) {
            return favouritePets.getValue().get(position);
        } else {
            return null;
        }
    }

    private void loadFavouritePets() {
        Log.d(TAG, "Create NEW list with favourite pets");
        List<PetMock> petsList = new ArrayList<>();
        petsList.add(new PetMock("First favourite pet", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Second favourite pet", R.drawable.pet_mock_image));
        petsList.add(new PetMock("Third favourite pet", R.drawable.pet_mock_image));
        favouritePets.setValue(petsList);
    }
}
