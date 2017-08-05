package net.lvtushiguang.trip.adapter;

import android.content.Context;
import android.view.ViewGroup;

import net.lvtushiguang.trip.interf.MultiItemTypeSupport;

/**
 * Recycler多种Item的适配器
 * Created by PhoenixTree on 2017/7/16.
 */

public class MultiItemRecyclerAdapter<T> extends RecyclerAdapter<T> {

    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public MultiItemRecyclerAdapter(MultiItemTypeSupport<T> mMultiItemTypeSupport) {
        super();
        this.mMultiItemTypeSupport = mMultiItemTypeSupport;
    }

    public int getItemViewType(int position) {
        return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
        RecyclerViewHolder holder = RecyclerViewHolder.get(parent.getContext(), parent, layoutId);
        return holder;
    }

    @Override
    public RecyclerViewHolder onCreateItem(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindItem(RecyclerViewHolder holder, T t) {

    }
}
