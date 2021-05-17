package com.trippl3dev.listlibrary;


import androidx.recyclerview.widget.RecyclerView;

class CustomScrollListener extends RecyclerView.OnScrollListener {
    private int mLastDx = 0;
    int width = 0;
    int dx = 0;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (mLastDx != dx) {
                scrollToMostVisibleItem();
            } else {
                dx = 0;
                mLastDx = 0;
            }
        }
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        this.dx += dx;
    }


    private void scrollToMostVisibleItem() {
        int direction = (dx > 0) ? 1 : -1;
        dx = Math.abs(dx);
        int shiftCount = Math.round(dx / width);
        int pixelShift = dx % width;
        if (pixelShift > width / 2) {
            shiftCount++;
        }

        float targetScrollPixels = shiftCount * width;
        float finalScrollPixels = (targetScrollPixels - dx) * direction;
        if (finalScrollPixels != 0) {
//            mRecyclerVIew.smoothScrollBy((int) finalScrollPixels, 0);
            mLastDx = (int) finalScrollPixels;
            dx = 0;
        }
    }
}