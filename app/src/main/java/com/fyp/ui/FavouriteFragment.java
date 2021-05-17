package com.fyp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fyp.R;
import com.fyp.adapter.CardPetAdapter;
import com.fyp.adapter.CardPetMockAdapter;
import com.fyp.adapter.NavigationDirection;
import com.fyp.utils.LinearHorizontalSpacingDecoration;
import com.fyp.viewmodel.FavouriteMockViewModel;
import com.fyp.viewmodel.FavouriteViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import static com.fyp.constant.Constants.SERVER_ENABLED;


public class FavouriteFragment extends Fragment {
    private final String TAG = FavouriteFragment.class.getSimpleName();

    private DiscreteScrollView cardPetRecycleView;

    private CardPetMockAdapter cardPetMockAdapter;
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

        if (SERVER_ENABLED) {
            initServerAdapterAndViewModel();
        } else {
            initMockAdapterAndViewModel();
        }

        return view;
    }

    private void initServerAdapterAndViewModel() {
        FirebaseUser firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseCurrentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    // set up pet view model
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
            }
        });
    }

    private void initMockAdapterAndViewModel() {
        FavouriteMockViewModel favouriteModel = new ViewModelProvider(requireActivity()).get(FavouriteMockViewModel.class);

        cardPetMockAdapter = new CardPetMockAdapter(
                NavigationDirection.FROM_FAVOURITE_TO_PET,
                favouriteModel,
                getViewLifecycleOwner());
        cardPetRecycleView.setAdapter(cardPetMockAdapter);

        favouriteModel.getFavouritePets().observe(getViewLifecycleOwner(), petMocks -> {
            cardPetMockAdapter.clearItems();
            cardPetMockAdapter.setItems(petMocks);
            cardPetRecycleView.scrollToPosition(0);
        });
    }
}