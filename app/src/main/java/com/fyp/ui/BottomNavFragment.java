package com.fyp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_nav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        Boolean isAuthenticated = getArguments().getBoolean("isAuthenticated");
        if (!isAuthenticated) {
            bottomNavigationView.getMenu().findItem(R.id.favouriteFragment).setVisible(false);
            bottomNavigationView.getMenu().findItem(R.id.profileFragment).setVisible(false);
        } else {
            bottomNavigationView.getMenu().findItem(R.id.favouriteFragment).setVisible(true);
            bottomNavigationView.getMenu().findItem(R.id.profileFragment).setVisible(true);
        }
        NavController navController = Navigation.findNavController(getActivity(), R.id.bottom_fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}