package net.lvtushiguang.trip.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.api.ApiHttpClient;
import net.lvtushiguang.trip.bean.Constants;
import net.lvtushiguang.trip.bean.User;
import net.lvtushiguang.trip.cache.CacheManager;
import net.oschina.common.helper.SharedPreferencesHelper;

import cz.msebera.android.httpclient.Header;

/**
 * 账户辅助类，
 * 用于更新用户信息和保存当前账户等操作
 */
public final class AccountHelper {
    private static final String TAG = AccountHelper.class.getSimpleName();
    private User user;
    private Application application;
    @SuppressLint("StaticFieldLeak")
    private static AccountHelper instances;

    private AccountHelper(Application application) {
        this.application = application;
    }

    public static void init(Application application) {
        if (instances == null)
            instances = new AccountHelper(application);
        else {
            // reload from source
            instances.user = SharedPreferencesHelper.loadFormSource(instances.application, User.class);
            TLog.d(TAG, "init reload:" + instances.user);
        }
    }

    public static boolean isLogin() {
        return !StringUtils.isEmpty(getUserId()) && !StringUtils.isEmpty(getCookie());
    }

    public static String getCookie() {
        String cookie = getUser().getCookie();
        return cookie == null ? "" : cookie;
    }

    public static String getUserId() {
        return getUser().getId();
    }

    public synchronized static User getUser() {
        if (instances == null) {
            TLog.error("AccountHelper instances is null, you need call init() method.");
            return new User();
        }
        if (instances.user == null)
            instances.user = SharedPreferencesHelper.loadFormSource(instances.application, User.class);
        if (instances.user == null)
            instances.user = new User();
        return instances.user;
    }

    public static boolean updateUserCache(User user) {
        if (user == null)
            return false;
        // 保留Cookie信息
        if (TextUtils.isEmpty(user.getCookie()) && instances.user != user)
            user.setCookie(instances.user.getCookie());
        instances.user = user;
        return SharedPreferencesHelper.save(instances.application, user);
    }

    private static void clearUserCache() {
        instances.user = null;
        SharedPreferencesHelper.remove(instances.application, User.class);
    }

    public static boolean login(final User user, Header[] headers) {
        // 更新Cookie
        String cookie = ApiHttpClient.getCookie(AppContext.getInstance());
        if (TextUtils.isEmpty(cookie) || cookie.length() < 6) {
            return false;
        }

        TLog.d(TAG, "login:" + user + " cookie：" + cookie);

        user.setCookie(cookie);

        int count = 10;
        boolean saveOk;
        // 保存缓存
        while (!(saveOk = updateUserCache(user)) && count-- > 0) {
            SystemClock.sleep(100);
        }

        if (saveOk) {
//            ApiHttpClient.setCookieHeader(getCookie());
            // 登陆成功,重新启动消息服务
//            NoticeManager.init(instances.application);
        }
        return saveOk;
    }

    /**
     * 退出登陆操作需要传递一个View协助完成延迟检测操作
     *
     * @param view     View
     * @param runnable 当清理完成后回调方法
     */
    public static void logout(final View view, final Runnable runnable) {
        // 清除用户缓存
        clearUserCache();
        // 等待缓存清理完成
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.removeCallbacks(this);
                User user = SharedPreferencesHelper.load(instances.application, User.class);
                // 判断当前用户信息是否清理成功
                if (user == null || StringUtils.isEmpty(user.getId())) {
                    clearAndPostBroadcast(instances.application);
                    runnable.run();
                } else {
                    view.postDelayed(this, 200);
                }
            }
        }, 200);

    }

    /**
     * 当前用户信息清理完成后调用方法清理服务等信息
     *
     * @param application Application
     */
    private static void clearAndPostBroadcast(Application application) {
//        // 清理网络相关
//        ApiHttpClient.destroyAndRestore(application);
//
//        // 用户退出时清理红点并退出服务
//        NoticeManager.clear(application, NoticeManager.FLAG_CLEAR_ALL);
//        NoticeManager.exitServer(application);
//
//        // 清理动弹对应数据
//        CacheManager.deleteObject(application, TweetFragment.CACHE_USER_TWEET);
//
//        // Logout 广播
//        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
//        application.sendBroadcast(intent);

    }
}
