package com.fyp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.fyp.repository.PetDataSource;
import com.fyp.repository.PetDataSourceFactory;
import com.fyp.response.Pet;

public class PagedPetViewModel extends ViewModel {
    LiveData<PagedList<Pet>> petPagedList;
    LiveData<PageKeyedDataSource<String, Pet>> liveDataSource;

    public PagedPetViewModel() {
        PetDataSourceFactory petDataSourceFactory = new PetDataSourceFactory();
        liveDataSource = petDataSourceFactory.getPetLiveDataSource();

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PetDataSource.LIMIT_SIZE)
                .build();
        petPagedList = new LivePagedListBuilder<>(petDataSourceFactory, pagedListConfig)
                .build();
    }

    public LiveData<PagedList<Pet>> getPetPagedList() {
        return petPagedList;
    }
}
