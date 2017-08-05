package net.lvtushiguang.trip.ui;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.api.remote.LvTuShiGuangApi;
import net.lvtushiguang.trip.base.BaseActivity;
import net.lvtushiguang.trip.bean.SimpleBackPage;
import net.lvtushiguang.trip.bean.User;
import net.lvtushiguang.trip.util.TDevice;
import net.lvtushiguang.trip.util.UIHelper;
import net.lvtushiguang.trip.widget.AvatarView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import cz.msebera.android.httpclient.Header;

/**
 * 用户登录界面
 * <p/>
 * Created by 薰衣草 on 2016/7/4.
 */
public class LoginActivity extends BaseActivity {
    public static final int REQUEST_CODE_INIT = 0;
    public static final int REQUEST_CODE_OPENID = 1000;
    // 登陆实体类
    public static final String BUNDLE_KEY_LOGINBEAN = "bundle_key_loginbean";
    protected static final String TAG = LoginActivity.class.getSimpleName();
    private static final String BUNDLE_KEY_REQUEST_CODE = "BUNDLE_KEY_REQUEST_CODE";
    private final int requestCode = REQUEST_CODE_INIT;
    @BindView(R.id.iv_avatar)
    AvatarView mIvAvatar;
    @BindView(R.id.et_number)
    EditText mEtNumber;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.iv_clearnumber)
    ImageView mIvClearNumber;
    @BindView(R.id.iv_clearpasd)
    ImageView mIvClearPasd;
    BroadcastReceiver receiver;
    private String mNumber = "";
    private String mPassword = "";
    private final JsonHttpResponseHandler mHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                String reCode = response.getString("reCode");
                if (reCode.equals("000000")) {
                    JSONObject reContent = response.getJSONObject("reContent");
                    User user = new User();
                    user.setId(reContent.getString("userCode"));
                    user.setName(reContent.getString("userName"));
                    user.setMobile(reContent.getString("userMobile"));
                    user.setLoginName(mNumber);
                    user.setPassword(mPassword);

                    AppContext.getInstance().saveUserInfo(user);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String reMsg = response.getString("reMsg");
                    AppContext.showToast(reMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("网络超时，请稍候重试。");
            Log.i("onFailure", "statusCode:" + statusCode + "|throwable:" + throwable.toString() + "|responseString:" + responseString);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            AppContext.showToast("网络超时，请稍候重试。");
            Log.i("onFailure_2", "statusCode:" + statusCode + "|errorResponse:" + throwable.toString());
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

    }

    @Override
    protected int getActionBarTitle() {
        return R.string.login;
    }

    @Override
    protected boolean hasCustomEnabled() {
        return true;
    }

    @Override
    protected int getActionBarLayoutId() {
        return R.layout.item_login_customactionbar;
    }

    @Override
    @OnClick({R.id.btn_login, R.id.iv_clearnumber, R.id.iv_clearpasd, R.id.tv_findpassword})
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
//                handleLogin();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.et_number:
                ivClearState(mEtNumber.getText().toString(), mIvClearNumber);
                break;
            case R.id.et_password:
                ivClearState(mEtPassword.getText().toString(), mIvClearPasd);
                break;
            case R.id.iv_clearnumber:
                mEtNumber.setText("");
                break;
            case R.id.iv_clearpasd:
                mEtPassword.setText("");
                break;
            case R.id.tv_findpassword:
                UIHelper.showSimpleBack(this, SimpleBackPage.FIND_PASSWORD);
                break;

            default:
                break;
        }
    }

    @OnFocusChange(R.id.et_number)
    void onmEtNumberFocusChange(boolean focused) {
        if (focused) {
            ivClearState(mEtNumber.getText().toString(), mIvClearNumber);
        }
    }

    @OnFocusChange(R.id.et_password)
    void onmEtPasswordFocusChange(boolean focused) {
        if (focused) {
            ivClearState(mEtPassword.getText().toString(), mIvClearPasd);
        }
    }

    @OnTextChanged(value = R.id.et_number, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onmEtNumberChanged(CharSequence text) {
        ivClearState(text.toString(), mIvClearNumber);
    }

    @OnTextChanged(value = R.id.et_password, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onmEtPasswordChanged(CharSequence text) {
        ivClearState(text.toString(), mIvClearPasd);
    }

    /**
     * 清除按钮
     *
     * @param str
     * @param iv
     */
    private void ivClearState(String str, ImageView iv) {
        if (iv.getId() == R.id.iv_clearnumber) {
            mIvClearPasd.setVisibility(View.INVISIBLE);
            if (str.isEmpty() || str == null) {
                mIvClearNumber.setVisibility(View.INVISIBLE);
            } else {
                mIvClearNumber.setVisibility(View.VISIBLE);
            }
        } else {
            mIvClearNumber.setVisibility(View.INVISIBLE);
            if (str.isEmpty() || str == null) {
                mIvClearPasd.setVisibility(View.INVISIBLE);
            } else {
                mIvClearPasd.setVisibility(View.VISIBLE);
            }
        }
    }

    private void handleLogin() {

        if (prepareForLogin()) {
            return;
        }

        // if the data has ready
        mNumber = mEtNumber.getText().toString();
        mPassword = mEtPassword.getText().toString();
        showWaitDialog(R.string.progress_login);
        LvTuShiGuangApi.login(mNumber, mPassword, mHandler);
    }

    private boolean prepareForLogin() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return true;
        }
        if (mEtNumber.length() == 0) {
            mEtNumber.setError("请输入手机号码");
            mEtNumber.requestFocus();
            return true;
        }

        if (mEtPassword.length() == 0) {
            mEtPassword.setError("请输入密码");
            mEtPassword.requestFocus();
            return true;
        }

        return false;
    }

    @Override
    public void initData() {
        if (mInfo == null)
            return;
        mIvAvatar.setImageResource(R.drawable.function_icon_manage_assistant);
        mEtNumber.setText(mInfo.getLoginName());
        mEtPassword.setText(mInfo.getPassword());
    }

}
