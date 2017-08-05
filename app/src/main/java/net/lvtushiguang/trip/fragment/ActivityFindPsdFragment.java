package net.lvtushiguang.trip.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.BaseFragment;
import net.lvtushiguang.trip.bean.SimpleBackPage;
import net.lvtushiguang.trip.util.UIHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 薰衣草 on 2017/1/18.
 */
public class ActivityFindPsdFragment extends BaseFragment {

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_findpassword, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    @OnClick({R.id.next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.FIND_PASSWORD_CODE);
                break;
            default:
                break;
        }
    }
}
