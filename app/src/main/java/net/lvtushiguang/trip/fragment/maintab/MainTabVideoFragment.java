package net.lvtushiguang.trip.fragment.maintab;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.adapter.ViewPageFragmentAdapter;
import net.lvtushiguang.trip.base.BaseFragment;
import net.lvtushiguang.trip.fragment.list.VideoListFragment;
import net.lvtushiguang.trip.interf.OnViewPagerChangeListener;
import net.lvtushiguang.trip.widget.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 薰衣草 on 2016/7/19.
 */
public class MainTabVideoFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private static final String TAG = MainTabVideoFragment.class.getSimpleName();

    @BindView(R.id.tablayout)
    public LinearLayout tablayout;
    @BindView(R.id.pager_tabstrip)
    public PagerSlidingTabStrip mTabStrip;
    @BindView(R.id.iv_arrow_down)
    public ImageView mViewArrowDown;
    @BindView(R.id.view_pager)
    public ViewPager mViewPager;
    @BindView(R.id.line)
    public View mLine;

    private String[] tabs = {"推荐", "音乐", "搞笑", "社会", "小品", "生活", "影视"};
    private ViewPageFragmentAdapter mTabsAdapter;
    private Fragment mLastFragment;
    private int mLastIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_maintab_video;
    }

    @Override
    public void initView(View view) {
        mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
                mTabStrip, mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnPageChangeListener(this);
        mViewArrowDown.setImageResource(R.drawable.search_icon);
        //添加Tab
        for (int i = 0; i < tabs.length; i++) {
            mTabsAdapter.addTab(tabs[i], tabs[i], VideoListFragment.class,
                    getBundle(i));
        }
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", newType);
        return bundle;
    }

    @OnClick({R.id.iv_arrow_down})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_arrow_down:
                Log.i("TAG", "iv_arrow_down");
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            tablayout.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);
            setRootViewTopPadding(false);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            tablayout.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.VISIBLE);
            setRootViewTopPadding(true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 1) {
            //正在滑动时的Fragment
            mLastFragment = mTabsAdapter.getCurrentFragment();
            mLastIndex = mViewPager.getCurrentItem();
        } else if (state == 2 && mLastIndex != mViewPager.getCurrentItem()) {
            if (mLastFragment != null
                    && mLastFragment instanceof OnViewPagerChangeListener) {
                OnViewPagerChangeListener listener = (OnViewPagerChangeListener) mLastFragment;
                listener.onViewPagerChange();
            }
        }
    }
}
