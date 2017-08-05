package net.lvtushiguang.trip.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.lvtushiguang.trip.AppConfig;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.fragment.maintab.MainTabMeFragment;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, AppConfig.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String str = null;
            switch (resp.errCode) {
                case 0:
                    str = "交易成功";
                    break;
                case -1:
                    str = "交易错误";
                    break;
                case -2:
                    str = "交易已取消";
                    break;

                default:
                    break;
            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("交易结果");
//            builder.setMessage(str);
//            builder.setPositiveButton("确定", new OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent(WXPayEntryActivity.this, WXPayEntryActivity.class);
//                    startActivity(intent);
//                }
//            });
//            builder.show();
//            Intent intent = new Intent(MainTabMeFragment.REFRESH_BALANCE);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            finish();
        }
    }
}