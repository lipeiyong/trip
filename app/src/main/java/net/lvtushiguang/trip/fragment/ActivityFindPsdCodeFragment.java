package net.lvtushiguang.trip.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 薰衣草 on 2017/1/18.
 */
public class ActivityFindPsdCodeFragment extends BaseFragment {

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_findpassword_code, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
}
