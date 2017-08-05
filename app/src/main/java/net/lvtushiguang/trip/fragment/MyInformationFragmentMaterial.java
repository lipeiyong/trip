package net.lvtushiguang.trip.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.BaseFragment;

/**
 * 我的资料
 * <p>
 * Created by 薰衣草 on 2016/8/12.
 */
public class MyInformationFragmentMaterial extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info_material, container, false);
        return view;
    }
}
