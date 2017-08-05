package net.lvtushiguang.trip.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.BaseRecyclerAdapter;
import net.lvtushiguang.trip.base.ListBaseAdapter;
import net.lvtushiguang.trip.bean.TopInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopInfoAdapter extends BaseRecyclerAdapter<TopInfo> {


    @Override
    protected int getLayoutId() {
        return R.layout.list_cell_top_video;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

    }

}
