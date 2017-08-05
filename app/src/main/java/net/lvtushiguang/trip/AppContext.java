package net.lvtushiguang.trip;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import net.lvtushiguang.trip.api.ApiHttpClient;
import net.lvtushiguang.trip.base.BaseApplication;
import net.lvtushiguang.trip.bean.Constants;
import net.lvtushiguang.trip.bean.User;
import net.lvtushiguang.trip.cache.DataCleanManager;
import net.lvtushiguang.trip.util.MethodsCompat;
import net.lvtushiguang.trip.util.StringUtils;
import net.lvtushiguang.trip.util.TLog;


import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.utils.KJLoger;

import java.util.Properties;
import java.util.UUID;

import static net.lvtushiguang.trip.AppConfig.KEY_FRITST_START;
import static net.lvtushiguang.trip.AppConfig.KEY_LOAD_IMAGE;
import static net.lvtushiguang.trip.AppConfig.KEY_NIGHT_MODE_SWITCH;
import static net.lvtushiguang.trip.AppConfig.KEY_TWEET_DRAFT;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 *
 * @author 火蚁 (http://my.oschina.net/LittleDY)
 * @version 1.0
 * @created 2014-04-22
 */
public class AppContext extends BaseApplication {

    public static final int PAGE_SIZE = 20;// 默认分页大小

    private static AppContext instance;

    private String loginUid;

    private boolean login;

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    public static void setLoadImage(boolean flag) {
        set(KEY_LOAD_IMAGE, flag);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public static String getTweetDraft() {
        return getPreferences().getString(
                KEY_TWEET_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setTweetDraft(String draft) {
        set(KEY_TWEET_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static String getNoteDraft() {
        return getPreferences().getString(
                AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setNoteDraft(String draft) {
        set(AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static boolean isFristStart() {
        return getPreferences().getBoolean(KEY_FRITST_START, true);
    }

    public static void setFristStart(boolean frist) {
        set(KEY_FRITST_START, frist);
    }

    //夜间模式
    public static boolean getNightModeSwitch() {
        return getPreferences().getBoolean(KEY_NIGHT_MODE_SWITCH, false);
    }

    // 设置夜间模式
    public static void setNightModeSwitch(boolean on) {
        set(KEY_NIGHT_MODE_SWITCH, on);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initLogin();
    }

    private void init() {
        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        ApiHttpClient.setHttpClient(client);
        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));

        // Log控制器
        KJLoger.openDebutLog(true);
        TLog.DEBUG = BuildConfig.DEBUG;

        // Bitmap缓存地址
        HttpConfig.CACHEPATH = "LvTuShiGuang/imagecache";
    }

    private void initLogin() {
        User user = getLoginUser();
        if (null != user && !StringUtils.isEmpty(user.getId())) {
            login = true;
            loginUid = user.getId();
        } else {
            this.cleanLoginInfo();
        }
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 保存登录信息
     *
     * @param user 用户信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final User user) {
        this.loginUid = user.getId();
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.uid", user.getId());
                setProperty("user.loginName", user.getLoginName());
                setProperty("user.password", user.getPassword());
                setProperty("user.name", user.getName());
                setProperty("user.mobile", user.getMobile());
            }
        });
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final User user) {
        setProperties(new Properties() {
            {
                setProperty("user.uid", user.getId());
                setProperty("user.loginName", user.getLoginName());
                setProperty("user.password", user.getPassword());
                setProperty("user.name", user.getName());
                setProperty("user.mobile", user.getMobile());
            }
        });
    }

    /**
     * 获得登录用户的信息
     *
     * @return
     */
    public User getLoginUser() {
        User user = new User();
        user.setId(getProperty("user.uid"));
        user.setLoginName(getProperty("user.loginName"));
        user.setName(getProperty("user.name"));
        user.setMobile(getProperty("user.mobile"));
        user.setPassword(getProperty("user.password"));
        return user;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = "";
        this.login = false;
        removeProperty("user.uid", "user.loginName", "user.password",
                "user.name", "user.mobile");
    }

    public String getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        cleanLoginInfo();
        ApiHttpClient.cleanCookie();
        this.cleanCookie();
        this.login = false;
        this.loginUid = "";

        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        sendBroadcast(intent);
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        DataCleanManager.cleanDatabases(this);
        // 清除数据缓存
        DataCleanManager.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanManager.cleanCustomCache(MethodsCompat
                    .getExternalCacheDir(this));
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
        Core.getKJBitmap().cleanCache();
    }
}
