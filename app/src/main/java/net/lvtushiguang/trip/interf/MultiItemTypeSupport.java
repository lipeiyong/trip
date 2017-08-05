package net.lvtushiguang.trip.interf;

/**
 * 多种Item布局支持
 * Created by PhoenixTree on 2017/7/16.
 */

public interface MultiItemTypeSupport<T> {
    int getLayoutId(int itemType);

    int getItemViewType(int position, T t);
}
