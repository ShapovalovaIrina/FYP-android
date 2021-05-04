package com.fyp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fyp.R;
import com.fyp.adapter.CardPetAdapter;
import com.fyp.adapter.CardPetMockAdapter;
import com.fyp.pojo.PetMock;
import com.fyp.response.Pet;
import com.fyp.utils.LinearHorizontalSpacingDecoration;
import com.fyp.viewmodel.FavouriteMockViewModel;
import com.fyp.viewmodel.FavouriteViewModel;
import com.fyp.viewmodel.PetMockViewModel;
import com.fyp.viewmodel.PetViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

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
                .setMaxScale(1.2f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build());
        cardPetMockAdapter = new CardPetMockAdapter();
        cardPetRecycleView.setAdapter(cardPetMockAdapter);

        if (SERVER_ENABLED) {
            initServerAdapterAndViewModel();
        } else {
            initMockAdapterAndViewModel();
        }

        return view;
    }

    private void initServerAdapterAndViewModel() {
        Log.d(TAG, "Initialize favouriteViewModel and CardPetAdapter");

        cardPetAdapter = new CardPetAdapter();
        cardPetRecycleView.setAdapter(cardPetAdapter);

        FirebaseUser firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseCurrentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    // set up pet view model
                    FavouriteViewModel favouriteViewModel = new ViewModelProvider(requireActivity()).get(FavouriteViewModel.class);
                    favouriteViewModel.getFavouritePets(idToken).observe(getViewLifecycleOwner(), new Observer<List<Pet>>() {
                        @Override
                        public void onChanged(List<Pet> petResponse) {
                            Log.d(TAG, "favouriteViewModel onChanged triggered");
                            if (petResponse != null) {
                                Toast.makeText(getContext(), "Update pet response", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "favouriteViewModel onChanged petResponses:");
                                for (Pet pet : petResponse) {
                                    Log.d(TAG, pet.toString());
                                }

                                Log.d(TAG, "favouriteViewModel onChanged set up adapter data");
                                cardPetAdapter.setItems(petResponse);
                                Toast.makeText(getContext(), "Успешно загрузили избранных питомцев (не пусто)", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Успешно загрузили избранных питомцев (пусто)", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Ошибка во время получения токена", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initMockAdapterAndViewModel() {
        Log.d(TAG, "Initialize FavouriteMockViewModel and CardPetMockAdapter");

        cardPetMockAdapter = new CardPetMockAdapter();
        cardPetRecycleView.setAdapter(cardPetMockAdapter);

        FavouriteMockViewModel favouriteModel = new ViewModelProvider(requireActivity()).get(FavouriteMockViewModel.class);
        favouriteModel.getFavouritePets().observe(getViewLifecycleOwner(), new Observer<List<PetMock>>() {
            @Override
            public void onChanged(List<PetMock> petMocks) {
                Log.d(TAG, "Get pets from view model & set them");
                cardPetMockAdapter.setItems(petMocks);
            }
        });
    }
}