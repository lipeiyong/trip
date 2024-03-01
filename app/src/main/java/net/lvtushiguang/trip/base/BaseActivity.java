package net.lvtushiguang.trip.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.AppManager;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.bean.User;
import net.lvtushiguang.trip.interf.BaseViewInterface;
import net.lvtushiguang.trip.ui.dialog.CommonToast;
import net.lvtushiguang.trip.ui.dialog.DialogControl;
import net.lvtushiguang.trip.util.DialogHelp;
import net.lvtushiguang.trip.util.TDevice;

import org.kymjs.kjframe.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * baseActionBar Activity
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月25日 上午11:30:15 引用自：tonlin
 */
public abstract class BaseActivity extends AppCompatActivity implements
        DialogControl, View.OnClickListener, BaseViewInterface {
    public static final String INTENT_ACTION_EXIT_APP = "INTENT_ACTION_EXIT_APP";
    public User mInfo;
    protected LayoutInflater mInflater;
    protected ActionBar mActionBar;
    private boolean _isVisible;
    private ProgressDialog _waitDialog;
    private TextView mTvActionTitle;
    private Unbinder unbinder;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TDevice.hideSoftKeyboard(getCurrentFocus());
        unbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        onBeforeSetContentLayout();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        mActionBar = getSupportActionBar();
        mInflater = getLayoutInflater();
        if (hasActionBar()) {
            initActionBar(mActionBar);
        }
        // 通过注解绑定控件
        unbinder = ButterKnife.bind(this);

        mInfo = AppContext.getInstance().getLoginUser();

        init(savedInstanceState);
        initView();
        initData();
        _isVisible = true;
    }

    protected void onBeforeSetContentLayout() {
    }

    protected boolean hasActionBar() {
        return true;
    }

    protected int getLayoutId() {
        return 0;
    }

    protected View inflateView(int resId) {
        return mInflater.inflate(resId, null);
    }

    protected int getActionBarTitle() {
        return R.string.app_name;
    }

    public void setActionBarTitle(String title) {
        if (StringUtils.isEmpty(title)) {
            title = getString(R.string.app_name);
        }
        if (hasActionBar() && mActionBar != null) {
            if (mTvActionTitle != null) {
                mTvActionTitle.setText(title);
            }
            mActionBar.setTitle(title);
        }
    }

    protected boolean hasBackButton() {
        return false;
    }

    protected boolean hasCustomEnabled() {
        return false;
    }

    protected int getActionBarLayoutId() {
        return 0;
    }

    protected void init(Bundle savedInstanceState) {
    }

    protected void initActionBar(ActionBar actionBar) {
        if (actionBar == null)
            return;
        if (hasCustomEnabled() && getActionBarLayoutId() != 0) {
            //显示自定义视图
            mActionBar.setDisplayShowCustomEnabled(true);
            //是否显示应用程序图标
            mActionBar.setDisplayShowHomeEnabled(false);
            //是否显示ActionBar标题
            mActionBar.setDisplayShowTitleEnabled(false);
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View actionbarLayout = LayoutInflater.from(this).inflate(
                    getActionBarLayoutId(), null);
            TextView mTvCustomTitle = (TextView) actionbarLayout.findViewById(R.id.title);
            int titleRes = getActionBarTitle();
            if (titleRes != 0) {
                mTvCustomTitle.setText(titleRes);
            }
            mActionBar.setCustomView(actionbarLayout, lp);
        } else if (hasBackButton()) {
            //设置是否将应用程序图标转变成可点击的图标，并在图标上添加一个向左的箭头
            mActionBar.setDisplayHomeAsUpEnabled(true);
            //设置是否将应用程序图标转变成可点击的按钮
            mActionBar.setHomeButtonEnabled(true);
        } else {
            //ActionBar默认如果没有做任何设置，会显示出一个箭头（DISPLAY_HOME_AS_UP），一个logo（DISPLAY_SHOW_HOME），标题（DISPLAY_SHOW_TITLE）
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setDisplayUseLogoEnabled(false);
            int titleRes = getActionBarTitle();
            if (titleRes != 0) {
                actionBar.setTitle(titleRes);
            }
        }
    }

    public void setActionBarTitle(int resId) {
        if (resId != 0) {
            setActionBarTitle(getString(resId));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showToast(int msgResid, int icon, int gravity) {
        showToast(getString(msgResid), icon, gravity);
    }

    public void showToast(String message, int icon, int gravity) {
        CommonToast toast = new CommonToast(this);
        toast.setMessage(message);
        toast.setMessageIc(icon);
        toast.setLayoutGravity(gravity);
        toast.show();
    }

    @Override
    public ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    @Override
    public ProgressDialog showWaitDialog(int resid) {
        return showWaitDialog(getString(resid));
    }

    @Override
    public ProgressDialog showWaitDialog(String message) {
        if (_isVisible) {
            if (_waitDialog == null) {
                _waitDialog = DialogHelp.getWaitDialog(this, message);
            }
            if (_waitDialog != null) {
                _waitDialog.setMessage(message);
                _waitDialog.show();
            }
            return _waitDialog;
        }
        return null;
    }

    @Override
    public void hideWaitDialog() {
        if (_isVisible && _waitDialog != null) {
            try {
                _waitDialog.dismiss();
                _waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        // setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }
}
