package com.fyp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.fyp.repository.PagedPetRepository;
import com.fyp.response.Pet;

public class PagedPetViewModel extends ViewModel {
    private LiveData<PagedList<Pet>> petPagedList;
    private PagedPetRepository pagedPetRepository;

    public PagedPetViewModel() {
        pagedPetRepository = new PagedPetRepository();
    }

    public LiveData<PagedList<Pet>> getPetPagedList(String typeFilter, String shelterFilter) {
        petPagedList = pagedPetRepository.getPagedPetList(typeFilter, shelterFilter);
        return petPagedList;
    }

    public LiveData<PagedList<Pet>> getPetPagedList() {
        return petPagedList;
    }

    public LiveData<Boolean> getZeroItemsLiveData() {
        return pagedPetRepository.getZeroItemsLiveData();
    }
}
