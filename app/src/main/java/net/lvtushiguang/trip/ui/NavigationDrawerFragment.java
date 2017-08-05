package net.lvtushiguang.trip.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.BaseFragment;
import net.lvtushiguang.trip.bean.SimpleBackPage;
import net.lvtushiguang.trip.util.UIHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 侧滑菜单界面
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月25日 下午6:00:05
 */
public class NavigationDrawerFragment extends BaseFragment implements
        OnClickListener {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private View mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;

    @BindView(R.id.menu_item_team)
    View mMenu_item_team;

    @BindView(R.id.menu_item_feedback)
    View mMenu_item_feedback;

    @BindView(R.id.menu_item_about)
    View mMenu_item_about;

    @BindView(R.id.menu_item_help)
    View mMenu_item_help;

    @BindView(R.id.menu_item_setting)
    View mMenu_item_setting;

    @BindView(R.id.menu_item_theme)
    View mMenu_item_theme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState
                    .getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = inflater.inflate(R.layout.fragment_navigation_drawer,
                container, false);
        mDrawerListView.setOnClickListener(this);
        ButterKnife.bind(this, mDrawerListView);
        initView(mDrawerListView);
        initData();
        return mDrawerListView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.menu_item_team:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TEAM);
                break;
            case R.id.menu_item_feedback:
                UIHelper.showSimpleBack(getActivity(),
                        SimpleBackPage.Feedback);
                break;
            case R.id.menu_item_about:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.ABOUT);
                break;
            case R.id.menu_item_help:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.HELP);
                break;
            case R.id.menu_item_setting:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.SETTING);
                break;
            case R.id.menu_item_theme:
                switchTheme();
                break;
            default:
                break;

        }
        mDrawerLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                mDrawerLayout.closeDrawers();
            }
        }, 800);
    }

    private void switchTheme() {
        if (AppContext.getNightModeSwitch()) {
            AppContext.setNightModeSwitch(false);
        } else {
            AppContext.setNightModeSwitch(true);
        }

        if (AppContext.getNightModeSwitch()) {
            getActivity().setTheme(R.style.AppBaseTheme_Night);
        } else {
            getActivity().setTheme(R.style.AppBaseTheme_Light);
        }

        getActivity().recreate();
    }

    @Override
    public void initView(View view) {

        TextView night = (TextView) view.findViewById(R.id.tv_night);
        if (AppContext.getNightModeSwitch()) {
            night.setText("日间");
        } else {
            night.setText("夜间");
        }

        mMenu_item_team.setOnClickListener(this);
        mMenu_item_feedback.setOnClickListener(this);
        mMenu_item_about.setOnClickListener(this);
        mMenu_item_help.setOnClickListener(this);

        mMenu_item_setting.setOnClickListener(this);
        mMenu_item_theme.setOnClickListener(this);

    }

    @Override
    public void initData() {
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null
                && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * 这个片段的用户必须调用这个方法来设置导航
     * 抽屉相互作用。
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                null, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActivity().invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void openDrawerMenu() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ActionBar getActionBar() {
//        return ((ActionBarActivity) getActivity()).getSupportActionBar();
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }
}
