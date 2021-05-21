package com.fyp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fyp.R;
import com.fyp.adapter.CardPetAdapter;
import com.fyp.adapter.NavigationDirection;
import com.fyp.utils.LinearHorizontalSpacingDecoration;
import com.fyp.viewmodel.FavouriteViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

public class FavouriteFragment extends Fragment {
    private final String TAG = FavouriteFragment.class.getSimpleName();

    private DiscreteScrollView cardPetRecycleView;

    private CardPetAdapter cardPetAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        cardPetRecycleView = view.findViewById(R.id.favourite_fragment_recycle_view);
        cardPetRecycleView.addItemDecoration(new LinearHorizontalSpacingDecoration(32));
        cardPetRecycleView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build());

        initServerAdapterAndViewModel();

        return view;
    }

    private void initServerAdapterAndViewModel() {
        FirebaseUser firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseCurrentUser.getIdToken(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String idToken = task.getResult().getToken();

                FavouriteViewModel favouriteViewModel = new ViewModelProvider(requireActivity()).get(FavouriteViewModel.class);

                cardPetAdapter = new CardPetAdapter(
                        NavigationDirection.FROM_FAVOURITE_TO_PET,
                        favouriteViewModel,
                        getViewLifecycleOwner(),
                        idToken);
                cardPetRecycleView.setAdapter(cardPetAdapter);

                favouriteViewModel.getCodeResponse().observe(getViewLifecycleOwner(), integer -> Toast.makeText(getContext(), "Favourite code response " + integer, Toast.LENGTH_SHORT).show());
                favouriteViewModel.getFavouritePets(idToken).observe(getViewLifecycleOwner(), petResponse -> {
                    if (petResponse != null) {
                        Toast.makeText(getContext(), "Успешно загрузили избранных питомцев", Toast.LENGTH_SHORT).show();
                        cardPetAdapter.clearItems();
                        cardPetAdapter.setItems(petResponse);
                        cardPetRecycleView.scrollToPosition(0);
                    }
                });
            } else {
                Toast.makeText(getContext(), "Ошибка во время получения токена", Toast.LENGTH_SHORT).show();
            }
        });
    }
}