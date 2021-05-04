package com.fyp.viewmodel;

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
        System.out.println("FavouriteMockViewModel get favourite pets");
        if (favouritePets == null) {
            favouritePets = new MutableLiveData<List<PetMock>>();
            loadFavouritePets();
        }
        return favouritePets;
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
