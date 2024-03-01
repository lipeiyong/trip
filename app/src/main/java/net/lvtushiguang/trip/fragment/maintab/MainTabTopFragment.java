package net.lvtushiguang.trip.fragment.maintab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.BaseFragment;
import net.lvtushiguang.trip.widget.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 薰衣草 on 2016/7/19.
 */
public class MainTabTopFragment extends BaseFragment {

    @BindView(R.id.ll_header)
    LinearLayout mHeader;
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.ctv_text)
    CustomTextView mCtvText;
    @BindView(R.id.ctv_image)
    CustomTextView mCtvImage;
    @BindView(R.id.ctv_video)
    CustomTextView mCtvVideo;

    private ListAdapter mAdapter;
    private ArrayList<String> mDatas = new ArrayList<>();

    protected static final String TAG = MainTabTopFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "topinfolist_";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", TAG + "-onCreate");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_miantab_top;
    }

    @Override
    public void initView(View view) {
        for (int i = 0; i < 6; i++) {
            mDatas.add("Hello World");
        }

        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public Object getItem(int i) {
                return mDatas.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_cell_top_video, null);
                return view;
            }
        };

        mListView.setAdapter(mAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    @OnClick({R.id.ctv_text, R.id.ctv_image, R.id.ctv_video})
    public void onClick(View view) {

    }

}
