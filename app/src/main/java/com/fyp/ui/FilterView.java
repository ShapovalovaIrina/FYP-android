package com.fyp.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.fyp.R;
import com.fyp.response.Shelter;
import com.fyp.response.Type;
import com.fyp.viewmodel.TypeShelterViewModel;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class FilterView {
    private View rootView;

    private LinearLayout filterLinearLayout;
    private LinearLayout scrollRecyclerView;
    private LinearLayout childrenCheckBoxLinearLayoutTypes;
    private LinearLayout childrenCheckBoxLinearLayoutShelters;

    private CheckBox parentCheckBoxTypes;
    private List<CheckBox> childCheckBoxesTypes = new ArrayList<>();
    private List<Integer> childCheckBoxesTypesID = new ArrayList<>();
    private CheckBox parentCheckBoxShelters;
    private List<CheckBox> childCheckBoxesShelters = new ArrayList<>();
    private List<Integer> childCheckBoxesSheltersID = new ArrayList<>();

    private CheckBox showMoreButton;
    private Button saveButton;

    /* Функции, которые вызываются извне */
    /* Сохранение состояния check boxes */
    public boolean getShelterParentCheckBoxState() {
        return parentCheckBoxShelters.isChecked();
    }

    public List<Boolean> getShelterChildrenCheckBoxesState() {
        List<Boolean> states = new ArrayList<>();
        for (CheckBox checkBox : childCheckBoxesShelters) states.add(checkBox.isChecked());
        return states;
    }

    public boolean getTypeParentCheckBoxState() {
        return parentCheckBoxTypes.isChecked();
    }

    public List<Boolean> getTypeChildrenCheckBoxesState() {
        List<Boolean> states = new ArrayList<>();
        for (CheckBox checkBox : childCheckBoxesTypes) states.add(checkBox.isChecked());
        return states;
    }

    /* Считывание состояние check boxes для реализации фильтрации */
    public String getTypeFilter() {
        if (parentCheckBoxTypes.isChecked()) return null;
        StringJoiner typeFilter = new StringJoiner(",");
        for (int i = 0; i < childCheckBoxesTypes.size(); i++) {
            if (childCheckBoxesTypes.get(i).isChecked()) typeFilter.add(Integer.toString(childCheckBoxesTypesID.get(i)));
        }
        return typeFilter.toString();
    }

    public String getShelterFilter() {
        if (parentCheckBoxShelters.isChecked()) return null;
        StringJoiner shelterFilter = new StringJoiner(",");
        for (int i = 0; i < childCheckBoxesShelters.size(); i++) {
            if (childCheckBoxesShelters.get(i).isChecked()) shelterFilter.add(Integer.toString(childCheckBoxesSheltersID.get(i)));
        }
        return shelterFilter.toString();
    }

    /* Функции, которые реализуют внутренюю логику */
    /* Конструктор */
    public FilterView(
            View view,
            ViewModelStoreOwner requiredActivity,
            LifecycleOwner lifecycleOwner,
            Boolean savedShelterParentCheckBoxState,
            Boolean savedTypeParentCheckBoxState,
            List<Boolean> savedShelterChildrenCheckBoxesState,
            List<Boolean> savedTypeChildrenCheckBoxesState) {
        rootView = view;
        filterLinearLayout = view.findViewById(R.id.filter_view_parent_linear_layout);
        scrollRecyclerView = view.findViewById(R.id.search_fragment_discrete_scroll_view);
        showMoreButton = view.findViewById(R.id.search_fragment_show_more_button);

        parentCheckBoxTypes = view.findViewById(R.id.filter_view_parent_check_box_types);
        parentCheckBoxShelters = view.findViewById(R.id.filter_view_parent_check_box_shelters);
        childrenCheckBoxLinearLayoutTypes = view.findViewById(R.id.filter_view_children_checkbox_linear_layout_types);
        childrenCheckBoxLinearLayoutShelters = view.findViewById(R.id.filter_view_children_checkbox_linear_layout_shelters);

        saveButton = view.findViewById(R.id.filter_view_save_button);

        onCollapseFilterView();

        TypeShelterViewModel typeShelterViewModel = new ViewModelProvider(requiredActivity).get(TypeShelterViewModel.class);
        typeShelterViewModel.getShelters().observe(lifecycleOwner, shelters -> {
            if (shelters != null) {
                createChildrenCheckBoxesShelter(shelters, parentCheckBoxShelters);
                if (savedShelterParentCheckBoxState != null && savedShelterChildrenCheckBoxesState != null) {
                    parentCheckBoxShelters.setChecked(savedShelterParentCheckBoxState);
                    for (int i = 0; i < savedShelterChildrenCheckBoxesState.size(); i++)
                        childCheckBoxesShelters.get(i).setChecked(savedShelterChildrenCheckBoxesState.get(i));
                }
            }
        });
        typeShelterViewModel.getTypes().observe(lifecycleOwner, types -> {
            if (types != null) {
                createChildrenCheckBoxesTypes(types, parentCheckBoxTypes);
                if (savedTypeParentCheckBoxState != null && savedTypeChildrenCheckBoxesState != null) {
                    parentCheckBoxTypes.setChecked(savedTypeParentCheckBoxState);
                    for (int i = 0; i < savedTypeChildrenCheckBoxesState.size(); i++)
                        childCheckBoxesTypes.get(i).setChecked(savedTypeChildrenCheckBoxesState.get(i));
                }
            }
        });

        saveButton.setOnClickListener(saveButtonOnClickListener());
        showMoreButton.setOnCheckedChangeListener(showMoreButtonOnCheckedChangeListener());
        parentCheckBoxShelters.setOnCheckedChangeListener(parentCheckBoxOnCheckedChangeListener(childCheckBoxesShelters));
        parentCheckBoxTypes.setOnCheckedChangeListener(parentCheckBoxOnCheckedChangeListener(childCheckBoxesTypes));
    }

    View.OnClickListener saveButtonOnClickListener() {
        return view -> {
            Toast.makeText(rootView.getContext(), "Параметры успешно сохранены", Toast.LENGTH_SHORT).show();
            showMoreButton.setChecked(false);
        };
    }

    CompoundButton.OnCheckedChangeListener showMoreButtonOnCheckedChangeListener() {
        return (compoundButton, isChecked) -> {
            if (isChecked) {
                onExpandFilterView();
            } else {
                onCollapseFilterView();
            }
        };
    }

    CompoundButton.OnCheckedChangeListener childCheckBoxOnCheckedChangeListener(CheckBox parentCheckBox, List<CheckBox> childCheckBoxes) {
        return (compoundButton, isChecked) -> {
            if (parentCheckBox.isChecked() && !isChecked) {
                parentCheckBox.setOnCheckedChangeListener(null);
                parentCheckBox.setChecked(false);
                parentCheckBox.setOnCheckedChangeListener(parentCheckBoxOnCheckedChangeListener(childCheckBoxes));
            } else if (!parentCheckBox.isChecked() && isChecked) {
                boolean allChildrenChecked = true;
                for (CheckBox child : childCheckBoxes) {
                    if (!child.isChecked()) {
                        allChildrenChecked = false;
                        break;
                    }
                }
                if (allChildrenChecked) {
                    parentCheckBox.setOnCheckedChangeListener(null);
                    parentCheckBox.setChecked(true);
                    parentCheckBox.setOnCheckedChangeListener(parentCheckBoxOnCheckedChangeListener(childCheckBoxes));
                }
            }
        };
    }

    CompoundButton.OnCheckedChangeListener parentCheckBoxOnCheckedChangeListener(List<CheckBox> childCheckBoxes) {
        return (compoundButton, isChecked) -> {
            for (CheckBox checkBox : childCheckBoxes) {
                checkBox.setChecked(isChecked);
            }
        };
    }

    public void onExpandFilterView() {
        // scrollViewLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorGray));
        scrollRecyclerView.setVisibility(View.GONE);

        filterLinearLayout.setVisibility(View.VISIBLE);
        filterLinearLayout.bringToFront();
    }

    public void onCollapseFilterView() {
        filterLinearLayout.setVisibility(View.GONE);

        // scrollViewLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        scrollRecyclerView.setVisibility(View.VISIBLE);
    }

    private void createChildrenCheckBoxesShelter(List<Shelter> shelterList, CheckBox parentCheckBox) {
        for (Shelter s : shelterList) {
            CheckBox checkBox = new MaterialCheckBox(rootView.getContext());
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            checkBox.setText(s.getTitle());
            checkBox.setChecked(true);
            checkBox.setOnCheckedChangeListener(childCheckBoxOnCheckedChangeListener(parentCheckBox, childCheckBoxesShelters));

            childrenCheckBoxLinearLayoutShelters.addView(checkBox);
            childCheckBoxesShelters.add(checkBox);
            childCheckBoxesSheltersID.add(s.getId());
        }
    }

    private void createChildrenCheckBoxesTypes(List<Type> typeList, CheckBox parentCheckBox) {
        for (Type t : typeList) {
            CheckBox checkBox = new MaterialCheckBox(rootView.getContext());
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            checkBox.setText(t.getType());
            checkBox.setChecked(true);
            checkBox.setOnCheckedChangeListener(childCheckBoxOnCheckedChangeListener(parentCheckBox, childCheckBoxesTypes));

            childrenCheckBoxLinearLayoutTypes.addView(checkBox);
            childCheckBoxesTypes.add(checkBox);
            childCheckBoxesTypesID.add(t.getId());
        }
    }
}
