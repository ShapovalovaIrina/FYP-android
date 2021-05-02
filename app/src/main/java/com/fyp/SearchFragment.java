package com.fyp;

import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyp.adapter.CardPetAdapter;
import com.fyp.pojo.Pet;
import com.fyp.viewmodel.PetViewModel;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

public class SearchFragment extends Fragment {
    private DiscreteScrollView cardPetRecycleView;
    private CardPetAdapter cardPetAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Search fragment is recreated");
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        cardPetRecycleView = view.findViewById(R.id.search_fragment_recycle_view);
        cardPetRecycleView.addItemDecoration(new LinearHorizontalSpacingDecoration(32));
        cardPetRecycleView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.2f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build());
        cardPetAdapter = new CardPetAdapter();
        cardPetRecycleView.setAdapter(cardPetAdapter);

        PetViewModel petModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
        petModel.getPets().observe(getViewLifecycleOwner(), new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> pets) {
                cardPetAdapter.setItems(pets);
            }
        });

        return view;
    }
}

class LinearHorizontalSpacingDecoration extends RecyclerView.ItemDecoration {
    private int innerSpacing;

    public LinearHorizontalSpacingDecoration(int innerSpacing) {
        this.innerSpacing = innerSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int itemPosition = parent.getChildAdapterPosition(view);

        outRect.left = itemPosition == 0 ? 0 : innerSpacing;
        outRect.right = itemPosition == state.getItemCount() - 1 ? 0 : innerSpacing;
    }
}
