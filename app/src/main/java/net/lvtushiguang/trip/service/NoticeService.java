package net.lvtushiguang.trip.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;

import net.lvtushiguang.trip.AppConfig;
import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.api.remote.LvTuShiGuangApi;
import net.lvtushiguang.trip.bean.Constants;
import net.lvtushiguang.trip.bean.Notice;
import net.lvtushiguang.trip.ui.MainActivity;
import net.lvtushiguang.trip.util.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import cz.msebera.android.httpclient.Header;
import trip.service.INoticeService;

/**
 * Created by 薰衣草 on 2017/2/10.
 */
public class NoticeService extends Service {

    public static final String INTENT_ACTION_REQUEST = "net.lvtushiguang.trip.service.REQUEST";

    private static final String TAG = NoticeService.class.getSimpleName();

    private static final long INTERVAL = 1000 * 10;
    private final IBinder mBinder = new ServiceStub(this);
    private Notice mNotice;
    private int lastNotifiyCount;
    JsonHttpResponseHandler mHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                String reCode = response.getString("reCode");
                if (reCode.equals("000000")) {
                    Notice notice = JSON.parseObject(response.getJSONObject("reContent").toString(), Notice.class);

                    if (notice != null) {
                        UIHelper.sendBroadCast(NoticeService.this, notice);
                        if (AppContext.get(AppConfig.KEY_NOTIFICATION_ACCEPT, true)) {
                            notification(notice);
                        }
                        mNotice = notice;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.e(TAG, throwable.toString());
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.INTENT_ACTION_NOTICE.equals(action)) {
                Notice notice = (Notice) intent.getSerializableExtra("notice_bean");
                int chargeState1 = Integer.valueOf(notice.getChargeState1());//待拍照
                int chargeState2 = Integer.valueOf(notice.getChargeState2());//已拍照
                int chargeState3 = Integer.valueOf(notice.getChargeState3());//待付费
                int chargeState4 = Integer.valueOf(notice.getChargeState4());//完成
                int chargeState5 = Integer.valueOf(notice.getChargeState5());//逃票

                int count = chargeState1 + chargeState2 + chargeState3 + chargeState4 + chargeState5;
                if (count == 0) {
                    NotificationManagerCompat.from(NoticeService.this).cancel(
                            R.string.you_have_news_messages);
                }
            } else if (INTENT_ACTION_REQUEST.equals(action)) {
                requestNotice();
            }
        }
    };

    private AlarmManager mAlarmMgr;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAlarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        startRequestAlarm();
        requestNotice();

        IntentFilter filter = new IntentFilter(INTENT_ACTION_REQUEST);
        filter.addAction(Constants.INTENT_ACTION_NOTICE);
        registerReceiver(mReceiver, filter);
    }

    private void startRequestAlarm() {
        cancelRequestAlarm();
        mAlarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000,
                INTERVAL, getOperationIntent());
    }

    /**
     * <!-- kymjs --> 即使启动PendingIntent的原进程结束了的话,PendingIntent本身仍然还存在，可在其他进程（
     * PendingIntent被递交到的其他程序）中继续使用.
     * 如果我在从系统中提取一个PendingIntent的，而系统中有一个和你描述的PendingIntent对等的PendingInent,
     * 那么系统会直接返回和该PendingIntent其实是同一token的PendingIntent，
     * 而不是一个新的token和PendingIntent。然而你在从提取PendingIntent时，通过FLAG_CANCEL_CURRENT参数，
     * 让这个老PendingIntent的先cancel()掉，这样得到的pendingInten和其token的就是新的了。
     */
    private void cancelRequestAlarm() {
        mAlarmMgr.cancel(getOperationIntent());
    }

    private PendingIntent getOperationIntent() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        return operation;
    }

    private void notification(Notice notice) {
        int chargeState1 = Integer.valueOf(notice.getChargeState1());
        int chargeState2 = Integer.valueOf(notice.getChargeState2());
        int chargeState3 = Integer.valueOf(notice.getChargeState3());
        int chargeState4 = Integer.valueOf(notice.getChargeState4());
        int chargeState5 = Integer.valueOf(notice.getChargeState5());

        int count = chargeState1 + chargeState2 + chargeState3 + chargeState4 + chargeState5;

        if (count == 0) {
            lastNotifiyCount = 0;
            NotificationManagerCompat.from(this).cancel(
                    R.string.you_have_news_messages);
            return;
        }
        if (count == lastNotifiyCount)
            return;

        lastNotifiyCount = count;

        Resources res = getResources();
        String contentTitle = res.getString(R.string.you_have_news_messages,
                count + "");
        String contentText;
        StringBuffer sb = new StringBuffer();
        int location = 0;

        if (chargeState5 > 0) {
            sb.append(getString(R.string.flee_ticket_count, chargeState5 + ""));
            location = 4;
        }

        if (chargeState4 > 0) {
            sb.append(getString(R.string.deal_success_count, chargeState4 + ""));
            location = 3;
        }

        if (chargeState3 > 0) {
            sb.append(getString(R.string.wait_charge_count, chargeState3 + ""))
                    .append(" ");
            location = 2;
        }

        if (chargeState2 > 0) {
            sb.append(getString(R.string.end_take_picture_count, chargeState2 + "")).append(" ");
            location = 1;
        }

        if (chargeState1 > 0) {
            sb.append(getString(R.string.wait_take_picture_count, chargeState1 + "")).append(" ");
            location = 0;
        }

        contentText = sb.toString();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("NOTICE", true);
        intent.putExtra("LOCATION", location);

        PendingIntent pi = PendingIntent.getActivity(this, 1000, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this).setTicker(contentTitle).setContentTitle(contentTitle)
                .setContentText(contentText).setAutoCancel(true)
                .setContentIntent(pi).setSmallIcon(R.mipmap.icon);

        if (AppContext.get(AppConfig.KEY_NOTIFICATION_SOUND, true)) {
            builder.setSound(Uri.parse("android.resource://"
                    + AppContext.getInstance().getPackageName() + "/"
                    + R.raw.notificationsound));
        }
        if (AppContext.get(AppConfig.KEY_NOTIFICATION_VIBRATION, true)) {
            long[] vibrate = {0, 10, 20, 30};
            builder.setVibrate(vibrate);
        }

        Notification notification = builder.build();

        NotificationManagerCompat.from(this).notify(
                R.string.you_have_news_messages, notification);
    }

    public void requestNotice() {
        LvTuShiGuangApi.getNotice(mHandler);
    }

    private static class ServiceStub extends INoticeService.Stub {
        WeakReference<NoticeService> mService;

        ServiceStub(NoticeService service) {
            mService = new WeakReference<NoticeService>(service);
        }

        @Override
        public void requestNotice() throws RemoteException {
            mService.get().requestNotice();
        }
    }
}
