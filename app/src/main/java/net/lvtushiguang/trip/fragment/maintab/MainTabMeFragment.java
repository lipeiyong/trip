package net.lvtushiguang.trip.fragment.maintab;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.api.remote.LvTuShiGuangApi;
import net.lvtushiguang.trip.base.BaseFragment;
import net.lvtushiguang.trip.base.BaseRecyclerFragment;
import net.lvtushiguang.trip.bean.SubTab;
import net.lvtushiguang.trip.bean.TopInfo;
import net.lvtushiguang.trip.bean.User;
import net.lvtushiguang.trip.fragment.list.HomeListFragment;
import net.lvtushiguang.trip.fragment.recycler.TopInfoRecyclerFragment;
import net.lvtushiguang.trip.interf.OnTabReselectListener;
import net.lvtushiguang.trip.ui.dialog.CommonDialog;
import net.lvtushiguang.trip.util.PayResult;
import net.lvtushiguang.trip.util.SizeUtils;
import net.lvtushiguang.trip.util.TDevice;
import net.lvtushiguang.trip.widget.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static android.R.attr.fragment;

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
