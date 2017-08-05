package net.lvtushiguang.trip.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

/**
 * RecyclerView中Item间距
 * Created by PhoenixTree on 2017/7/26.
 */

public class SpacesItemDecoration extends ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) != 0)
            outRect.top = space;
    }
}
