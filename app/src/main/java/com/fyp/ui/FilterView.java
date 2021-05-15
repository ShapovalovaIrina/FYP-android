package com.fyp.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.fyp.R;
import com.fyp.response.Shelter;
import com.fyp.viewmodel.ShelterViewModel;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

public class FilterView {
    private View rootView;

    private LinearLayout filterLinearLayout;
    private LinearLayout scrollViewLinearLayout;
    private LinearLayout childrenCheckBoxLinearLayout;

    private CheckBox showMoreButton;
    private Button saveButton;

    private CheckBox parentCheckBox;
    private List<CheckBox> childCheckBoxes = new ArrayList<>();

    public boolean getParentCheckBoxState() {
        return parentCheckBox.isChecked();
    }

    public List<Boolean> getChildrenCheckBoxesState() {
        List<Boolean> states = new ArrayList<>();
        for (CheckBox checkBox : childCheckBoxes) states.add(checkBox.isChecked());
        return states;
    }

    public FilterView(
            View view,
            ViewModelStoreOwner requiredActivity,
            LifecycleOwner lifecycleOwner,
            Boolean savedParentCheckBoxState,
            List<Boolean> savedChildrenCheckBoxesState) {
        rootView = view;
        filterLinearLayout = view.findViewById(R.id.filter_view_parent_linear_layout);
        scrollViewLinearLayout = view.findViewById(R.id.search_fragment_discrete_scroll_view);
        childrenCheckBoxLinearLayout = view.findViewById(R.id.filter_view_children_checkbox_linear_layout);
        showMoreButton = view.findViewById(R.id.search_fragment_show_more_button);
        saveButton = view.findViewById(R.id.filter_view_save_button);
        parentCheckBox = view.findViewById(R.id.filter_view_parent_check_box);

        filterLinearLayout.setVisibility(View.GONE);
        scrollViewLinearLayout.setVisibility(View.VISIBLE);

        ShelterViewModel shelterViewModel = new ViewModelProvider(requiredActivity).get(ShelterViewModel.class);
        shelterViewModel.getShelters().observe(lifecycleOwner, new Observer<List<Shelter>>() {
            @Override
            public void onChanged(List<Shelter> shelters) {
                if (shelters != null) initChildrenCheckBoxes(shelters);
            }
        });

        if (savedParentCheckBoxState != null && savedChildrenCheckBoxesState != null) {
            parentCheckBox.setChecked(savedParentCheckBoxState);
            for (int i = 0; i < savedChildrenCheckBoxesState.size(); i++)
                childCheckBoxes.get(i).setChecked(savedChildrenCheckBoxesState.get(i));
        }

        saveButton.setOnClickListener(saveButtonOnClickListener());
        showMoreButton.setOnCheckedChangeListener(showMoreButtonOnCheckedChangeListener());
        parentCheckBox.setOnCheckedChangeListener(parentCheckBoxOnCheckedChangeListener());
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

    CompoundButton.OnCheckedChangeListener childCheckBoxOnCheckedChangeListener() {
        return (compoundButton, isChecked) -> {
            if (parentCheckBox.isChecked() && !isChecked) {
                Toast.makeText(rootView.getContext(), "Uncheck parent (child)", Toast.LENGTH_SHORT).show();
                parentCheckBox.setOnCheckedChangeListener(null);
                parentCheckBox.setChecked(false);
                parentCheckBox.setOnCheckedChangeListener(parentCheckBoxOnCheckedChangeListener());
            } else if (!parentCheckBox.isChecked() && isChecked) {
                boolean allChildrenChecked = true;
                for (CheckBox child : childCheckBoxes) {
                    if (!child.isChecked()) {
                        allChildrenChecked = false;
                        break;
                    }
                }
                if (allChildrenChecked) {
                    Toast.makeText(rootView.getContext(), "Check parent (child)", Toast.LENGTH_SHORT).show();
                    parentCheckBox.setOnCheckedChangeListener(null);
                    parentCheckBox.setChecked(true);
                    parentCheckBox.setOnCheckedChangeListener(parentCheckBoxOnCheckedChangeListener());
                }
            }
        };
    }

    CompoundButton.OnCheckedChangeListener parentCheckBoxOnCheckedChangeListener() {
        return (compoundButton, isChecked) -> {
            for (CheckBox checkBox : childCheckBoxes) {
                checkBox.setChecked(isChecked);
            }
        };
    }

    public void onExpandFilterView() {
        Toast.makeText(rootView.getContext(), "Expand filter view", Toast.LENGTH_SHORT).show();
        // scrollViewLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorGray));
        scrollViewLinearLayout.setVisibility(View.GONE);

        filterLinearLayout.setVisibility(View.VISIBLE);
        filterLinearLayout.bringToFront();
    }

    public void onCollapseFilterView() {
        Toast.makeText(rootView.getContext(), "Collapse filter view", Toast.LENGTH_SHORT).show();
        filterLinearLayout.setVisibility(View.GONE);

        // scrollViewLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        scrollViewLinearLayout.setVisibility(View.VISIBLE);
    }

    private void initChildrenCheckBoxes(List<Shelter> shelterList) {
        for (Shelter s : shelterList) {
            CheckBox checkBox = new MaterialCheckBox(rootView.getContext());
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            checkBox.setText(s.getTitle());
            checkBox.setChecked(true);
            checkBox.setOnCheckedChangeListener(childCheckBoxOnCheckedChangeListener());

            childrenCheckBoxLinearLayout.addView(checkBox);
            childCheckBoxes.add(checkBox);
        }
    }
}
