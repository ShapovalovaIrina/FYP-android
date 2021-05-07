package com.fyp.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class SearchFragmentViewModel extends ViewModel {
    private Integer recycleViewItemPosition;
    private List<Integer> checkedIds;

    public Integer getRecycleViewItemPosition() {
        return recycleViewItemPosition;
    }

    public void setRecycleViewItemPosition(int recycleViewItemPosition) {
        this.recycleViewItemPosition = recycleViewItemPosition;
    }

    public List<Integer> getCheckedIds() {
        return checkedIds;
    }

    public void setCheckedIds(List<Integer> checkedIds) {
        this.checkedIds = checkedIds;
    }
}
