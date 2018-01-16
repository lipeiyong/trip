package net.lvtushiguang.trip.fragment.maintab;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.BaseFragment;
import net.lvtushiguang.trip.bean.SubTab;
import net.lvtushiguang.trip.fragment.list.HomeListFragment;
import net.lvtushiguang.trip.interf.OnTabReselectListener;
import net.lvtushiguang.trip.ui.SearchActivity;
import net.lvtushiguang.trip.util.AppOperator;
import net.lvtushiguang.trip.util.TDevice;
import net.lvtushiguang.trip.widget.TabPickerView;
import net.oschina.common.utils.StreamUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * 消息主界面
 * Created by 薰衣草 on 2016/7/19.
 */
public class MainTabHomeFragment extends BaseFragment implements OnTabReselectListener {

    private static final String TAG = "MainTabHomeFragment";

    @BindView(R.id.search)
    public TextView mSerach;
    @BindView(R.id.layout_tab)
    TabLayout mLayoutTab;
    @BindView(R.id.view_tab_picker)
    TabPickerView mViewTabPicker;
    @BindView(R.id.view_pager)
    public ViewPager mViewPager;
    @BindView(R.id.iv_arrow_down)
    ImageView mViewArrowDown;

    private FragmentPagerAdapter mAdapter;
    private static TabPickerView.TabPickerDataManager mTabPickerDataManager;
    List<SubTab> tabs;
    private Fragment mCurFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_maintab_home;
    }

    public static TabPickerView.TabPickerDataManager initTabPickerManager() {
        if (mTabPickerDataManager == null) {
            mTabPickerDataManager = new TabPickerView.TabPickerDataManager() {
                @Override
                public List<SubTab> setupActiveDataSet() {
                    FileReader reader = null;
                    try {
                        File file = AppContext.getInstance().getFileStreamPath("sub_tab_active.json");
                        if (!file.exists()) return null;
                        reader = new FileReader(file);
                        List<SubTab> list = AppOperator.getGson().fromJson(reader,
                                new TypeToken<ArrayList<SubTab>>() {
                                }.getType());
                        Log.e("TAG", list.toString());
                        return list;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        StreamUtil.close(reader);
                    }
                    return null;
                }

                @Override
                public List<SubTab> setupOriginalDataSet() {
                    InputStreamReader reader = null;
                    try {
                        reader = new InputStreamReader(
                                AppContext.getInstance().getAssets().open("sub_tab_home_original.json")
                                , "UTF-8");
                        return AppOperator.getGson().<ArrayList<SubTab>>fromJson(reader,
                                new TypeToken<ArrayList<SubTab>>() {
                                }.getType());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        StreamUtil.close(reader);
                    }
                    return null;
                }

                @Override
                public void restoreActiveDataSet(List<SubTab> mActiveDataSet) {
                    OutputStreamWriter writer = null;
                    try {
                        writer = new OutputStreamWriter(
                                AppContext.getInstance().openFileOutput(
                                        "sub_tab_active.json", Context.MODE_PRIVATE)
                                , "UTF-8");
                        AppOperator.getGson().toJson(mActiveDataSet, writer);
                        AppContext.set("TabsMask", TDevice.getVersionCode());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        StreamUtil.close(writer);
                    }
                }
            };
        }
        return mTabPickerDataManager;
    }

    @Override
    public void initView(View view) {
        mViewTabPicker.setTabPickerManager(initTabPickerManager());
        mViewTabPicker.setOnTabPickingListener(new TabPickerView.OnTabPickingListener() {

            private boolean isChangeIndex = false;

            @Override
            public void onSelected(final int position) {
                final int index = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(position);
                if (position == index) {
                    mAdapter.notifyDataSetChanged();
                    // notifyDataSetChanged为什么会导致TabLayout位置偏移，而且需要延迟设置才能起效？？？
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLayoutTab.getTabAt(position).select();
                        }
                    }, 50);
                }
            }

            @Override
            public void onRemove(int position, SubTab tab) {
                isChangeIndex = true;
            }

            @Override
            public void onInsert(SubTab tab) {
                isChangeIndex = true;
            }

            @Override
            public void onMove(int op, int np) {
                isChangeIndex = true;
            }

            @Override
            public void onRestore(final List<SubTab> mActiveDataSet) {
                if (!isChangeIndex) return;
                AppOperator.getExecutor().execute(new Runnable() {

                    @Override
                    public void run() {
                        OutputStreamWriter writer = null;
                        try {
                            writer = new OutputStreamWriter(
                                    AppContext.getInstance().openFileOutput(
                                            "sub_tab_active.json", Context.MODE_PRIVATE)
                                    , "UTF-8");
                            AppOperator.getGson().toJson(mActiveDataSet, writer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            StreamUtil.close(writer);
                        }
                    }
                });
                isChangeIndex = false;
                tabs.clear();
                tabs.addAll(mActiveDataSet);
                mAdapter.notifyDataSetChanged();
            }
        });

        mViewTabPicker.setOnShowAnimation(new TabPickerView.Action1<ViewPropertyAnimator>() {
            @Override
            public void call(ViewPropertyAnimator viewPropertyAnimator) {
                mViewArrowDown.setEnabled(false);
//                activity.toggleNavTabView(false);
                mViewArrowDown.animate()
                        .rotation(225)
                        .setDuration(380)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                super.onAnimationEnd(animator);
                                mViewArrowDown.setRotation(45);
                                mViewArrowDown.setEnabled(true);
                            }
                        }).start();
            }
        });

        mViewTabPicker.setOnHideAnimator(new TabPickerView.Action1<ViewPropertyAnimator>() {
            @Override
            public void call(ViewPropertyAnimator viewPropertyAnimator) {
                mViewArrowDown.setEnabled(false);
//                activity.toggleNavTabView(true);
                mViewArrowDown.animate()
                        .rotation(-180)
                        .setDuration(380)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                super.onAnimationEnd(animator);
                                mViewArrowDown.setRotation(0);
                                mViewArrowDown.setEnabled(true);
                            }
                        });
            }
        });

        tabs = new ArrayList<>();
        tabs.addAll(mViewTabPicker.getTabPickerManager().getActiveDataSet());
        for (SubTab tab : tabs) {
            mLayoutTab.addTab(mLayoutTab.newTab().setText(tab.getName()));
        }
        mViewPager.setAdapter(mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return tabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                SubTab tab = tabs.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tab", tab);
                return Fragment.instantiate(getContext(), HomeListFragment.class.getName(), bundle);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabs.get(position).getName();
            }

            //this is called when notifyDataSetChanged() is called
            @Override
            public int getItemPosition(Object object) {
                return PagerAdapter.POSITION_NONE;
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                mCurFragment = (Fragment) object;
                super.setPrimaryItem(container, position, object);
            }
        });

        mLayoutTab.setupWithViewPager(mViewPager);
        mLayoutTab.setSmoothScrollingEnabled(true);
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", newType);
        return bundle;
    }

    @OnClick({R.id.search, R.id.iv_arrow_down})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_arrow_down:
                if (mViewArrowDown.getRotation() != 0) {
                    mViewTabPicker.onTurnBack();
                } else {
                    mViewTabPicker.show(mLayoutTab.getSelectedTabPosition());
                }
                break;
        }
    }

    @OnTouch(R.id.search)
    public boolean onTouch(View v, MotionEvent event) {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN &&
                event.getX() > wm.getDefaultDisplay().getWidth() * 0.35) {
            Drawable drawable = mSerach.getBackground();
            int w = mSerach.getWidth();
            int h = mSerach.getHeight();
            Bitmap.Config config =
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);

            int x = (int) event.getX();
            int y = (int) event.getY();

            int value = bitmap.getPixel(x, y);
            if (value > -530000) {
                Log.e("TAG", value + "");
            }
        }
        return false;
    }


    @Override
    public void onTabReselect() {
        if (mCurFragment != null && mCurFragment instanceof OnTabReselectListener) {
            ((OnTabReselectListener) mCurFragment).onTabReselect();
        }
    }

}
