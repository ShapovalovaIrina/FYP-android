package com.fyp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.fyp.repository.PetDataSource;
import com.fyp.repository.PetDataSourceFactory;
import com.fyp.response.Pet;

public class PagedPetViewModel extends ViewModel {
    private final String TAG = PagedPetViewModel.class.getSimpleName();
    LiveData<PagedList<Pet>> petPagedList;
    LiveData<PageKeyedDataSource<String, Pet>> liveDataSource;
    MutableLiveData<Boolean> zeroItemsLiveData = new MutableLiveData<>(false);

    public LiveData<PagedList<Pet>> getPetPagedList(String typeFilter, String shelterFilter) {
        PetDataSourceFactory petDataSourceFactory = new PetDataSourceFactory(typeFilter, shelterFilter);
        liveDataSource = petDataSourceFactory.getPetLiveDataSource();

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PetDataSource.LIMIT_SIZE)
                .setPrefetchDistance(2)
                .build();
        petPagedList = new LivePagedListBuilder<>(petDataSourceFactory, pagedListConfig)
                .setBoundaryCallback(new PagedList.BoundaryCallback<Pet>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
                        zeroItemsLiveData.setValue(true);
                    }

                    @Override
                    public void onItemAtFrontLoaded(@NonNull Pet itemAtFront) {
                        super.onItemAtFrontLoaded(itemAtFront);
                        zeroItemsLiveData.setValue(false);
                    }
                })
                .build();
        return petPagedList;
    }

    public LiveData<PagedList<Pet>> getPetPagedList() {
        return petPagedList;
    }

    public LiveData<Boolean> getZeroItemsLiveData() {
        return zeroItemsLiveData;
    }
}
