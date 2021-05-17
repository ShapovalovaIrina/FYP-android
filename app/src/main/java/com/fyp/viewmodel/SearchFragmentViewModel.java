package com.fyp.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class SearchFragmentViewModel extends ViewModel {
    private Integer recycleViewItemPosition;

    private boolean shelterFilterParentCheckBox;
    private List<Boolean> shelterFilterChildrenCheckBoxes;
    private boolean typeFilterParentCheckBox;
    private List<Boolean> typeFilterChildrenCheckBoxes;


    public Integer getRecycleViewItemPosition() {
        return recycleViewItemPosition;
    }

    public void setRecycleViewItemPosition(int recycleViewItemPosition) {
        this.recycleViewItemPosition = recycleViewItemPosition;
    }

    public boolean getShelterFilterParentCheckBox() {
        return shelterFilterParentCheckBox;
    }

    public void setShelterFilterParentCheckBox(boolean shelterFilterParentCheckBox) {
        this.shelterFilterParentCheckBox = shelterFilterParentCheckBox;
    }

    public List<Boolean> getShelterFilterChildrenCheckBoxes() {
        return shelterFilterChildrenCheckBoxes;
    }

    public void setShelterFilterChildrenCheckBoxes(List<Boolean> shelterFilterChildrenCheckBoxes) {
        this.shelterFilterChildrenCheckBoxes = shelterFilterChildrenCheckBoxes;
    }

    public boolean getTypeFilterParentCheckBox() {
        return typeFilterParentCheckBox;
    }

    public void setTypeFilterParentCheckBox(boolean typeFilterParentCheckBox) {
        this.typeFilterParentCheckBox = typeFilterParentCheckBox;
    }

    public List<Boolean> getTypeFilterChildrenCheckBoxes() {
        return typeFilterChildrenCheckBoxes;
    }

    public void setTypeFilterChildrenCheckBoxes(List<Boolean> typeFilterChildrenCheckBoxes) {
        this.typeFilterChildrenCheckBoxes = typeFilterChildrenCheckBoxes;
    }
}
