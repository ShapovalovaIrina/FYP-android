package com.fyp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.fyp.response.Pet;

public class PagedPetRepository {
    private final PetDataSourceFactory petDataSourceFactory;

    MutableLiveData<Boolean> zeroItemsLiveData = new MutableLiveData<>(false);

    public PagedPetRepository() {
        petDataSourceFactory = new PetDataSourceFactory();
    }

    public LiveData<PagedList<Pet>> getPagedPetList(String typeFilter, String shelterFilter) {
        petDataSourceFactory.setTypeFilter(typeFilter);
        petDataSourceFactory.setShelterFilter(shelterFilter);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PetDataSource.LIMIT_SIZE)
                .setPrefetchDistance(2)
                .build();

        return new LivePagedListBuilder<>(petDataSourceFactory, pagedListConfig)
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
    }

    public LiveData<Boolean> getZeroItemsLiveData() {
        return zeroItemsLiveData;
    }
}
