package net.lvtushiguang.trip.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ReCycleView通用ViewHolder
 * Created by PhoenixTree on 2017/7/16.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public RecyclerViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }

    public static RecyclerViewHolder get(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        RecyclerViewHolder holder = new RecyclerViewHolder(context, itemView, parent);
        return holder;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public RecyclerViewHolder setText(int viewId, String text) {
        TextView textView = (TextView) mViews.get(viewId);
        if (textView != null)
            textView.setText(text);
        return this;
    }

    public RecyclerViewHolder setImageResuorce(int viewId, int resId) {
        ImageView imageView = (ImageView) mViews.get(viewId);
        if (imageView != null)
            imageView.setImageResource(resId);
        return this;
    }

    public RecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = mViews.get(viewId);
        if (view != null)
            view.setOnClickListener(listener);
        return this;
    }
}
