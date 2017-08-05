package net.lvtushiguang.trip.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.HashMap;

import trip.service.INoticeService;

/**
 * Created by 薰衣草 on 2017/2/13.
 */
public class NoticeUtils {

    public static final String TAG = NoticeUtils.class.getSimpleName();

    public static INoticeService sService = null;
    private static HashMap<Context, ServiceBinder> sConnectedMap = new HashMap<>();

    public static boolean bindService(Context context) {
        return bindToService(context, null);
    }

    private static boolean bindToService(Context context, ServiceConnection callback) {
        context.startService(new Intent(context, NoticeService.class));
        ServiceBinder sb = new ServiceBinder(callback);
        sConnectedMap.put(context, sb);

        return context.bindService((new Intent()).setClass(context, NoticeService.class), sb, 0);
    }

    public static void unbindFromService(Context context) {
        ServiceBinder sb = sConnectedMap.remove(context);
        if (sb == null) {
            Log.e("MusicUtils", "Trying to unbind for unknown Context");
            return;
        }
        context.unbindService(sb);

        if (sConnectedMap.isEmpty()) {
            sService = null;
        }
    }

    public static void requestNotice(Context context) {
        if (sService != null) {
            Log.i(TAG, "requestNotice...");
            try {
                sService.requestNotice();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            context.sendBroadcast(new Intent(NoticeService.INTENT_ACTION_REQUEST));
            Log.i(TAG, "requestNotice,service is null");
        }
    }

    public static class ServiceBinder implements ServiceConnection {
        ServiceConnection mCallback;

        ServiceBinder(ServiceConnection callback) {
            mCallback = callback;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sService = INoticeService.Stub.asInterface(service);
            if (mCallback != null) {
                mCallback.onServiceConnected(name, service);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (mCallback != null) {
                mCallback.onServiceDisconnected(name);
            }
            sService = null;
        }
    }
}
