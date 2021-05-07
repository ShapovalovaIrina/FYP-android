package com.fyp.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class SearchFragmentViewModel extends ViewModel {
    private Integer recycleViewItemPosition;
    private List<Integer> checkedIds;
    private boolean filterParentCheckBox;
    private List<Boolean> filterChildrenCheckBoxes;

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

    public boolean getFilterParentCheckBox() {
        return filterParentCheckBox;
    }

    public void setFilterParentCheckBox(boolean filterParentCheckBox) {
        this.filterParentCheckBox = filterParentCheckBox;
    }

    public List<Boolean> getFilterChildrenCheckBoxes() {
        return filterChildrenCheckBoxes;
    }

    public void setFilterChildrenCheckBoxes(List<Boolean> filterChildrenCheckBoxes) {
        this.filterChildrenCheckBoxes = filterChildrenCheckBoxes;
    }
}
