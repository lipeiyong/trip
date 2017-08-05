package net.lvtushiguang.trip.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.adapter.MessageDetailAdapter;
import net.lvtushiguang.trip.api.remote.LvTuShiGuangApi;
import net.lvtushiguang.trip.base.BaseFragment;
import net.lvtushiguang.trip.bean.Message;
import net.lvtushiguang.trip.ui.empty.EmptyLayout;
import net.lvtushiguang.trip.util.StringUtils;
import net.lvtushiguang.trip.widget.AvatarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * Created by 薰衣草 on 2017/2/20.
 */
public class MessageDetailFragment extends BaseFragment {

    public static final String BUNDLE_KEY_MESSAGE = "BUNDLE_KEY_MESSAGE";

    private static final String TAG = MessageDetailFragment.class.getSimpleName();

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Message message;
    private MessageDetailAdapter mAdapter;

    @BindView(R.id.webview)
    public WebView webView;

    @BindView(R.id.error_layout)
    public EmptyLayout mErrorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            message = (Message) args.getSerializable(BUNDLE_KEY_MESSAGE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_detail;
    }

    @Override
    public void initView(View view) {
        // 开启 localStorage
        webView.getSettings().setDomStorageEnabled(true);
        // 设置支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 启动缓存
        webView.getSettings().setAppCacheEnabled(true);
        // 设置缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //使用自定义的WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }
        });

        if (message != null) {
            webView.loadUrl(message.getVideourl());
        } else {
            webView.loadUrl("http://www.toutiao.com/a6431787154586992897/");
        }
    }
}
