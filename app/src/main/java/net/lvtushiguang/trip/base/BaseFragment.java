package net.lvtushiguang.trip.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.interf.BaseFragmentInterface;
import net.lvtushiguang.trip.ui.dialog.DialogControl;
import net.lvtushiguang.trip.util.UiUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 碎片基类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月25日 上午11:18:46
 */
public class BaseFragment extends Fragment implements View.OnClickListener, BaseFragmentInterface {
    private final static String TAG = BaseFragment.class.getSimpleName();
    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;
    public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;

    private boolean HEAD_PADDING_STATE = true;

    /**
     * 接收传递过来的参数
     */
    private Bundle args;
    private Unbinder unbinder;
    //缓存Fragment view
    private View rootView;

    public AppContext getApplication() {
        return (AppContext) getActivity().getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    /**
     * 设置顶部是否空出状态栏距离
     *
     * @param state
     */
    public void setHeadPaddingState(boolean state) {
        HEAD_PADDING_STATE = state;
    }

    public boolean getHeadPaddingState() {
        return HEAD_PADDING_STATE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        rootView = view;
        //--
        if (getHeadPaddingState()) {
            setRootViewTopPadding(true);
        }

        unbinder = ButterKnife.bind(this, view);
        initView(view);
        initData();

        return view;
    }

    public void setRootViewTopPadding(boolean type) {
        if (type) {
//            view.setPadding(rootView.getLeft(), view.getHeight() +UiUtil.getStatusBarHeight(getContext())
//                    , view.getRight(), view.getBottom());

            rootView.setPadding(0, UiUtil.getStatusBarHeight(getContext()), 0, 0);
        } else {
            rootView.setPadding(0, 0, 0, 0);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    protected int getLayoutId() {
        return 0;
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void hideWaitDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            ((DialogControl) activity).hideWaitDialog();
        }
    }

    protected ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    protected ProgressDialog showWaitDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(resid);
        }
        return null;
    }

    protected ProgressDialog showWaitDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(str);
        }
        return null;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

}
