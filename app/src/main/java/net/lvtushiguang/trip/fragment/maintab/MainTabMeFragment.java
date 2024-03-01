package net.lvtushiguang.trip.fragment.maintab;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.BaseFragment;
import net.lvtushiguang.trip.fragment.recycler.TopInfoRecyclerFragment;

/**
 * 个人中心界面
 * <p>
 * Created by 薰衣草 on 2016/7/19.
 */
public class MainTabMeFragment extends BaseFragment {

    private static final String TAG = MainTabMeFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "topinfolist_";

    private Fragment fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_maintab_me;
    }

    @Override
    public void initView(View view) {
        if (fragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            fragment = Fragment.instantiate(getContext(),
                    TopInfoRecyclerFragment.class.getName(), null);

            ft.replace(R.id.realcontent, fragment);
            ft.commit();
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.attach(fragment);
            ft.commit();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.detach(fragment);
        ft.commit();
    }
}
