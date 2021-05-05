package com.fyp.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fyp.R;
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
    private List<CheckBox> childCheckBoxes;

    public FilterView(View view) {
        rootView = view;
        filterLinearLayout = view.findViewById(R.id.filter_view_parent_linear_layout);
        scrollViewLinearLayout = view.findViewById(R.id.search_fragment_discrete_scroll_view);
        childrenCheckBoxLinearLayout = view.findViewById(R.id.filter_view_children_checkbox_linear_layout);
        showMoreButton = view.findViewById(R.id.search_fragment_show_more_button);
        saveButton = view.findViewById(R.id.filter_view_save_button);
        parentCheckBox = view.findViewById(R.id.filter_view_parent_check_box);

        filterLinearLayout.setVisibility(View.GONE);
        scrollViewLinearLayout.setVisibility(View.VISIBLE);

        saveButton.setOnClickListener(saveButtonOnClickListener());
        showMoreButton.setOnCheckedChangeListener(showMoreButtonOnCheckedChangeListener());
        parentCheckBox.setOnCheckedChangeListener(parentCheckBoxOnCheckedChangeListener());

        initChildrenCheckBoxes();
    }

    View.OnClickListener saveButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(rootView.getContext(), "Параметры успешно сохранены", Toast.LENGTH_SHORT).show();
                showMoreButton.setChecked(false);
            }
        };
    }

    CompoundButton.OnCheckedChangeListener showMoreButtonOnCheckedChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show filter view
                    onExpandFilterView();
                } else {
                    // hide filter view
                    onCollapseFilterView();
                }
            }
        };
    }

    CompoundButton.OnCheckedChangeListener childCheckBoxOnCheckedChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
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
            }
        };
    }

    CompoundButton.OnCheckedChangeListener parentCheckBoxOnCheckedChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                for (CheckBox checkBox : childCheckBoxes) {
                    checkBox.setChecked(isChecked);
                }
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

    private void initChildrenCheckBoxes() {
        childCheckBoxes = new ArrayList<>();
        for (String s : getShelterList()) {
            CheckBox checkBox = new MaterialCheckBox(rootView.getContext());
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            checkBox.setText(s);
            checkBox.setChecked(true);
            checkBox.setOnCheckedChangeListener(childCheckBoxOnCheckedChangeListener());

            childrenCheckBoxLinearLayout.addView(checkBox);
            childCheckBoxes.add(checkBox);
        }
    }

    private List<String> getShelterList() {
        List<String> shelters = new ArrayList<>();
        shelters.add("Приют друг");
        shelters.add("Рандомный приют");
        return shelters;
    }
}
