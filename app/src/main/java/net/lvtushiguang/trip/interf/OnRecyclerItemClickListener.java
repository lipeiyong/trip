package net.lvtushiguang.trip.interf;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * RecyclerView实现OnItemClickListener
 */
public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    //手势处理类
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),
                new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        //一次单独的轻击抬起操作,也就是轻击一下屏幕，就是普通点击事件
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onItemClick(vh);
            }
            return true;
        }
    }

    public abstract void onItemClick(RecyclerView.ViewHolder vh);
}
