package com.coreui.utils;

import android.graphics.Rect;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EqualSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;
    private boolean interSide = false;
    private boolean interTop = false;
    private float widthPercent;
    private int displayMode;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int GRID = 2;

    public EqualSpacingItemDecoration(int spacing) {
        this(spacing, -1);
    }

    public EqualSpacingItemDecoration(int spacing, int displayMode) {
        this.spacing = spacing;
        this.displayMode = displayMode;
    }

    public EqualSpacingItemDecoration(int spacing, int displayMode, float widthPercent) {
        this.spacing = spacing;
        this.displayMode = displayMode;
        this.widthPercent = widthPercent;
    }

    public EqualSpacingItemDecoration(int spacing, int displayMode, boolean interSide, boolean interTop) {
        this.spacing = spacing;
        this.displayMode = displayMode;
        this.widthPercent = widthPercent;
        this.interSide = interSide;
        this.interTop = interTop;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int itemCount = state.getItemCount();
        if (widthPercent > 0) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = (int) (DimensionUtils.INSTANCE.getScreenWidthInPixels(view.getContext(), true) * widthPercent);
            view.setLayoutParams(params);
        }
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        setSpacingForDirection(view.getContext().getResources().getConfiguration().locale.getLanguage(), outRect, layoutManager, position, itemCount);


    }

    private void setSpacingForDirection(String language, Rect outRect,
                                        RecyclerView.LayoutManager layoutManager,
                                        int position,
                                        int itemCount) {

        // Resolve display mode automatically
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager);
        }

        if (language.equals("ar")) {
            switch (displayMode) {
                case HORIZONTAL:
                    outRect.right = interSide?-spacing:spacing;
                    outRect.left = position == itemCount - 1 ? interSide?-spacing:spacing : 0;
                    if (position == 0)outRect.top = 8;
                    else
                    outRect.top = interTop?-spacing:spacing;
                    if (position == 0)outRect.bottom = 8;
                    else if (position == itemCount-1 && interTop){
                        outRect.bottom = spacing;
                    }else
                    outRect.bottom = interTop?-spacing:spacing;
                    break;
                case VERTICAL:
                    outRect.right = interSide?-spacing:spacing;
                    outRect.left = interSide?-spacing:spacing;
                    if (position == 0)outRect.top = 8;
                    else
                    outRect.top = interTop?-spacing:spacing;
                    if (position == 0)outRect.bottom = 8;
                    else if (position == itemCount-1 && interTop){
                        outRect.bottom = spacing;
                    }else
                    outRect.bottom = position == itemCount - 1 ? interTop?-spacing:spacing: 0;
                    break;
                case GRID:
                    if (layoutManager instanceof GridLayoutManager) {
                        GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                        int cols = gridLayoutManager.getSpanCount();
                        int rows = itemCount / cols;

                        outRect.right = interSide?-spacing:spacing;
                        outRect.left = position % cols == cols - 1 ? interSide?-spacing:spacing : 0;
                        if (position == 0)outRect.top = 8;
                        else
                        outRect.top = interTop?-spacing:spacing;
                        if (position == 0)outRect.bottom = 8;
                        else if (position == itemCount-1 && interTop){
                            outRect.bottom = spacing;
                        }else
                        outRect.bottom = position / cols == rows - 1 ? interTop?-spacing:spacing : 0;
                    }
                    break;
            }
        } else {
            switch (displayMode) {
                case HORIZONTAL:
                    outRect.left = interSide?-spacing:spacing;
                    outRect.right = position == itemCount - 1 ? interSide?-spacing:spacing: 0;
                    if (position == 0)outRect.top = 8;
                    else
                    outRect.top = interTop?-spacing:spacing;
                    if (position == 0)outRect.bottom = 8;
                    else if (position == itemCount-1 && interTop){
                        outRect.bottom = spacing;
                    }else
                    outRect.bottom = interTop?-spacing:spacing;
                    break;
                case VERTICAL:
                    outRect.left = interSide?-spacing:spacing;
                    outRect.right = interSide?-spacing:spacing;
                    if (position == 0)outRect.top = 8;
                    else
                    outRect.top = interTop?-spacing:spacing;
                    if (position == 0)outRect.bottom = 8;
                    else if (position == itemCount-1 && interTop){
                        outRect.bottom = spacing;
                    }else
                    outRect.bottom = position == itemCount - 1 ? interTop?-spacing:spacing : 0;
                    break;
                case GRID:
                    if (layoutManager instanceof GridLayoutManager) {
                        GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                        int cols = gridLayoutManager.getSpanCount();
                        int rows = itemCount / cols;

                        outRect.left = interSide?-spacing:spacing;
                        outRect.right = position % cols == cols - 1 ? interSide?-spacing:spacing: 0;
                        if (position == 0)outRect.top = 8;
                        else
                        outRect.top = interTop?-spacing:spacing;
                        if (position == 0)outRect.bottom = 8;
                        else if (position == itemCount-1 && interTop){
                            outRect.bottom = spacing;
                        }else
                        outRect.bottom = position / cols == rows - 1 ? interTop?-spacing:spacing : 0;
                    }
                    break;
            }
        }
    }

    private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) return GRID;
        if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
        return VERTICAL;
    }
}