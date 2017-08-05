package net.lvtushiguang.trip.base;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.adapter.ViewPageFragmentAdapter;
import net.lvtushiguang.trip.ui.empty.EmptyLayout;
import net.lvtushiguang.trip.widget.PagerSlidingTabStrip;

/**
 * 带有导航条的基类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年11月6日 下午4:59:50
 * 
 */
public abstract class BaseViewPagerFragment extends BaseFragment {

    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_viewpage_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTabStrip = (PagerSlidingTabStrip) view
                .findViewById(R.id.pager_tabstrip);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);

        mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);

        mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
                mTabStrip, mViewPager);
        setScreenPageLimit();
        onSetupTabAdapter(mTabsAdapter);
    }
    
    protected void setScreenPageLimit() {
    }

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);
}