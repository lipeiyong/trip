package net.lvtushiguang.trip.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import net.lvtushiguang.trip.adapter.RecyclerViewHolder;
import net.lvtushiguang.trip.interf.MultiItemTypeSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PhoenixTree on 2017/7/26.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    protected ArrayList<T> mDatas = new ArrayList<T>();

    protected abstract int getLayoutId();

    public T getItem(int arg0) {
        if (mDatas.size() > arg0) {
            return mDatas.get(arg0);
        }
        return null;
    }

    public void setData(ArrayList<T> data) {
        mDatas = data;
        notifyDataSetChanged();
    }

    public ArrayList<T> getData() {
        return mDatas == null ? (mDatas = new ArrayList<T>()) : mDatas;
    }

    public void addData(List<T> data) {
        if (mDatas != null && data != null && !data.isEmpty()) {
            mDatas.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addItem(T obj) {
        if (mDatas != null) {
            mDatas.add(obj);
        }
        notifyDataSetChanged();
    }

    public void addItem(int pos, T obj) {
        if (mDatas != null) {
            mDatas.add(pos, obj);
        }
        notifyDataSetChanged();
    }

    public void removeItem(Object obj) {
        mDatas.remove(obj);
        notifyDataSetChanged();
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder mViewHolder = null;
        if (mMultiItemTypeSupport == null) {
            mViewHolder = RecyclerViewHolder.get(parent.getContext(), parent, getLayoutId());
        } else {
            int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
            mViewHolder = RecyclerViewHolder.get(parent.getContext(), parent, layoutId);
        }

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
        }

        return super.getItemViewType(position);
    }

}
