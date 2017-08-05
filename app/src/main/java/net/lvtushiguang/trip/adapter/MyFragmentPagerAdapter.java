package net.lvtushiguang.trip.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> list;

	public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.list = list;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
