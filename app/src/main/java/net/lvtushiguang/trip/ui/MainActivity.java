package net.lvtushiguang.trip.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import net.lvtushiguang.trip.AppConfig;
import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.AppManager;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.adapter.MyFragmentPagerAdapter;
import net.lvtushiguang.trip.bean.Constants;
import net.lvtushiguang.trip.bean.Notice;
import net.lvtushiguang.trip.interf.BaseViewInterface;
import net.lvtushiguang.trip.interf.OnTabReselectListener;
import net.lvtushiguang.trip.service.NoticeUtils;
import net.lvtushiguang.trip.util.UpdateManager;
import net.lvtushiguang.trip.widget.BadgeView;
import net.lvtushiguang.trip.widget.MyFragmentTabHost;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 薰衣草 on 2016/7/4.
 */
public class MainActivity extends AppCompatActivity implements
        TabHost.OnTabChangeListener, View.OnTouchListener,
        BaseViewInterface {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static Notice mNotice;
    @BindView(android.R.id.tabhost)
    public MyFragmentTabHost mTabHost;
    @BindView(R.id.h_line)
    public View mHLine;
    private DoubleClickExitHelper mDoubleClickExit;
    private BadgeView mBvNotice;

    private List<Fragment> list;
    private BadgeView mBvOne, mBvTwo, mBvThree;
    private MyFragmentPagerAdapter adapter;

    private Unbinder unbinder;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.INTENT_ACTION_NOTICE)) {
                mNotice = (Notice) intent.getSerializableExtra("notice_bean");
            }
        }
    };

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏颜色-（也可通过values-v19 的sytle.xml设置,但是在某些手机上不会生效）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //--
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initView();

        AppManager.getAppManager().addActivity(this);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    /**
     * 处理传进来的intent
     *
     * @param intent
     * @return void
     * @author 火蚁 2015-1-28 下午3:48:44
     */
    private void handleIntent(Intent intent) {
        if (intent == null)
            return;

        if (intent.getBooleanExtra("NOTICE", false)) {
            int location = intent.getIntExtra("LOCATION", 0);
        }
    }

    @Override
    public void initView() {
        mDoubleClickExit = new DoubleClickExitHelper(this);
        mTitle = getTitle();

        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        }

        initTabs();

        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);

        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_NOTICE);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        registerReceiver(mReceiver, filter);
        NoticeUtils.bindService(this);

        if (mTabHost.getTabWidget().getChildCount() == 4) {
            View view = mTabHost.getTabWidget().getChildAt(3);
            TextView title = (TextView) view.findViewById(R.id.tab_title);
            title.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_icon_me), null,
                    null);
            title.setText(getString(R.string.main_tab_name_me));
        }

//        checkUpdate();
    }

    private void checkUpdate() {
        if (!AppContext.get(AppConfig.KEY_CHECK_UPDATE, true)) {
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                new UpdateManager(MainActivity.this, false).checkUpdate();
            }
        }, 2000);
    }

    @Override
    public void initData() {

    }

    private void initTabs() {
        MainTab[] tabs = MainTab.values();
        final int size = tabs.length;
        for (int i = 0; i < size; i++) {
            MainTab mainTab = tabs[i];
            TabHost.TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(
                    mainTab.getResIcon());
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,
                    null);
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);
            tab.setContent(new TabHost.TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            mTabHost.addTab(tab, mainTab.getClz(), null);

            if (mainTab.equals(MainTab.ME)) {
                View cn = indicator.findViewById(R.id.tab_mes);
                mBvNotice = new BadgeView(MainActivity.this, cn);
                mBvNotice.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                mBvNotice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                mBvNotice.setBackgroundResource(R.drawable.notification_bg);
                mBvNotice.setGravity(Gravity.CENTER);
            }
            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabChanged(String tabId) {
        final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
        if (tabId.equals(getString(MainTab.ME.getResName()))) {
            mBvNotice.setText("");
            mBvNotice.hide();
        }
        //重新调用onCreateOptionMenu()方法
        supportInvalidateOptionsMenu();
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //判断屏幕方向
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //视频横屏时强制竖屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (AppContext.get(AppConfig.KEY_DOUBLE_CLICK_EXIT, true)) {
                // 是否退出应用
                return mDoubleClickExit.onKeyDown(keyCode, event);
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean consumed = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && v.equals(mTabHost.getCurrentTabView())) {
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment != null
                    && currentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect();
                consumed = true;
            }
        }
        return consumed;
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(
                mTabHost.getCurrentTabTag());
    }

    /**
     * 由于视频在 listView 中,横屏后仍然后滑动
     * 此处判断当处于横屏时将滑动事件消耗掉
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            mTabHost.setVisibility(View.GONE);
            mHLine.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            mTabHost.setVisibility(View.VISIBLE);
            mHLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NoticeUtils.unbindFromService(this);
        unregisterReceiver(mReceiver);
        mReceiver = null;
//        NoticeUtils.tryToShutDown(this);
        unbinder.unbind();
    }

}
