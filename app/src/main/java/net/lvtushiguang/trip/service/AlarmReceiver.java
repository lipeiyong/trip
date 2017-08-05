package net.lvtushiguang.trip.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by 薰衣草 on 2017/2/10.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "AlarmReceiver => onReceive");
        NoticeUtils.requestNotice(context);
    }
}
