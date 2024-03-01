package net.lvtushiguang.trip.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.util.TDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Recycler通用适配器-单布局
 * Created by PhoenixTree on 2017/7/16.
 */

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    public static final int STATE_EMPTY_ITEM = 0;
    public static final int STATE_LOAD_MORE = 1;
    public static final int STATE_NO_MORE = 2;
    public static final int STATE_NO_DATA = 3;
    public static final int STATE_LESS_ONE_PAGE = 4;
    public static final int STATE_NETWORK_ERROR = 5;
    public static final int STATE_OTHER = 6;

    protected int state = STATE_LESS_ONE_PAGE;

    private static final int ITEM_TYPE_NORMAL = 0x1111;
    private static final int ITEM_TYPE_HEADER = 0x1112;
    private static final int ITEM_TYPE_FOOTER = 0x1113;

    protected int _loadmoreText;
    protected int _loadFinishText;
    protected int _noDateText;
    protected int mScreenWidth;

    private LayoutInflater mInflater;

    protected LayoutInflater getLayoutInflater(Context context) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return mInflater;
    }

    public void setScreenWidth(int width) {
        mScreenWidth = width;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    protected ArrayList<T> mDatas = new ArrayList<T>();

    public RecyclerAdapter() {
        _loadmoreText = R.string.loading;
        _loadFinishText = R.string.loading_no_more;
        _noDateText = R.string.error_view_no_data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder mViewHolder = null;
        switch (viewType) {
            case ITEM_TYPE_NORMAL:
//                mViewHolder = RecyclerViewHolder.get(parent.getContext(), parent, mLayoutId);
                mViewHolder = onCreateItem(parent, viewType);
                break;
            case ITEM_TYPE_HEADER:
                //--
                mViewHolder = RecyclerViewHolder.get(parent.getContext(), parent,
                        R.layout.recy_cell_header);
                break;
            case ITEM_TYPE_FOOTER:
                mViewHolder = RecyclerViewHolder.get(parent.getContext(), parent,
                        R.layout.list_cell_footer);
                break;
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (position == 0 && hasHeaderView()) {//第一条
            //--
        } else if (position == getItemCount() - 1 && hasFooterView()) {//最后一条

            LinearLayout mFooterView = (LinearLayout) holder.getConvertView();

            if (getState() == STATE_LOAD_MORE || getState() == STATE_NO_MORE
                    || state == STATE_EMPTY_ITEM
                    || getState() == STATE_NETWORK_ERROR) {
                if (!loadMoreHasBg()) {
                    mFooterView.setBackgroundDrawable(null);
                }
                ProgressBar progress = (ProgressBar) holder.getView(R.id.progressbar);
                TextView text = (TextView) holder.getView(R.id.text);
                switch (getState()) {
                    case STATE_LOAD_MORE:
                        mFooterView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                        text.setText(_loadmoreText);
                        break;
                    case STATE_NO_MORE:
                        mFooterView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        text.setVisibility(View.VISIBLE);
                        text.setText(_loadFinishText);
                        break;
                    case STATE_EMPTY_ITEM:
                        progress.setVisibility(View.GONE);
                        mFooterView.setVisibility(View.VISIBLE);
                        text.setText(_noDateText);
                        break;
                    case STATE_NETWORK_ERROR:
                        mFooterView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        text.setVisibility(View.VISIBLE);
                        if (TDevice.hasInternet()) {
                            text.setText("加载出错了");
                        } else {
                            text.setText("没有可用的网络");
                        }
                        break;
                    default:
                        progress.setVisibility(View.GONE);
                        mFooterView.setVisibility(View.GONE);
                        text.setVisibility(View.GONE);
                        break;
                }
            }
        } else {
            //        holder.updatePosition(position);
            onBindItem(holder, mDatas.get(position));
        }
    }

    protected boolean loadMoreHasBg() {
        return true;
    }

    public abstract RecyclerViewHolder onCreateItem(ViewGroup parent, int viewType);

    public abstract void onBindItem(RecyclerViewHolder holder, T t);

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && hasHeaderView()) {//第一条
            return ITEM_TYPE_HEADER;
        } else if (position == getItemCount() - 1 && hasFooterView()) {//最后一条
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        switch (getState()) {
            case STATE_EMPTY_ITEM:
                return getDataSizePlus1();
            case STATE_NETWORK_ERROR:
            case STATE_LOAD_MORE:
                return getDataSizePlus1();
            case STATE_NO_DATA:
                return 1;
            case STATE_NO_MORE:
                return getDataSizePlus1();
            case STATE_LESS_ONE_PAGE:
                return getDataSize();
            default:
                break;
        }
        return mDatas.size();
    }

    public int getDataSizePlus1() {
        if (hasFooterView()) {
            return getDataSize() + 1;
        }
        return getDataSize();
    }

    public int getDataSize() {
        return mDatas.size();
    }

    protected boolean hasHeaderView() {
        return true;
    }

    protected boolean hasFooterView() {
        return true;
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
            notifyDataSetChanged();
        }
    }

    public void addItem(int pos, T obj) {
        if (mDatas != null) {
            mDatas.add(pos, obj);
            notifyDataSetChanged();
        }
    }

    public void removeItem(T obj) {
        if (mDatas != null) {
            mDatas.remove(obj);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (mDatas != null) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }
}
