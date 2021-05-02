package com.fyp.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LinearHorizontalSpacingDecoration extends RecyclerView.ItemDecoration {
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
