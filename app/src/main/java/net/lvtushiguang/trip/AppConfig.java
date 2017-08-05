package net.lvtushiguang.trip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月25日 下午5:29:00
 */
public class AppConfig {

    private final static String APP_CONFIG = "config";

    public final static String CONF_COOKIE = "cookie";

    public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";

    public final static String SP_CONFIG = "CManageConfig";
    public final static String AES_KEY = "aes_key";

    public static final String KEY_LOAD_IMAGE = "KEY_LOAD_IMAGE";
    public static final String KEY_NOTIFICATION_ACCEPT = "KEY_NOTIFICATION_ACCEPT";
    public static final String KEY_NOTIFICATION_SOUND = "KEY_NOTIFICATION_SOUND";
    public static final String KEY_NOTIFICATION_VIBRATION = "KEY_NOTIFICATION_VIBRATION";
    public static final String KEY_NOTIFICATION_DISABLE_WHEN_EXIT = "KEY_NOTIFICATION_DISABLE_WHEN_EXIT";
    public static final String KEY_CHECK_UPDATE = "KEY_CHECK_UPDATE";
    public static final String KEY_DOUBLE_CLICK_EXIT = "KEY_DOUBLE_CLICK_EXIT";

    public static final String KEY_TWEET_DRAFT = "KEY_TWEET_DRAFT";
    public static final String KEY_NOTE_DRAFT = "KEY_NOTE_DRAFT";

    public static final String KEY_FRITST_START = "KEY_FRIST_START";

    public static final String KEY_NIGHT_MODE_SWITCH = "night_mode_switch";

    public static final String APP_QQ_KEY = "SiFCfgIKLH2IBciF";
    public static final String APP_QQ_ID = "1105470357";

    public static final String CMANAGE_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNCV0FEz2lZ8r27L6HdMM3STLoW4oIcdjfJ3rQfVVHJj4Tb5JVbOF4tSzshIGP3LL6tEUXO+9+6Yt1Pnk+HLif1qtOTCudyw98lrv0ra4aQ5aNsm0WGw8H7P2eGZbWBNjjKIwGiX6yBEY68btgy01tQx4pwEZZ73YRxPdEMJsOsQIDAQAB";

    //----微信 begin -----//
    public static final String APP_ID = "wxd930ea5d5a258f4f";
    //----微信 end -----//

    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "LvTuShiGuang"
            + File.separator + "ltsg_img" + File.separator;

    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "LvTuShiGuang"
            + File.separator + "download" + File.separator;

    private Context mContext;
    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }

    /**
     * 获取Preference设置
     * <br/>
     * 默认为MODE_PRIVATE
     * <br/>
     * 生成文件名为:<包名>_preferences.xml
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取files目录下的config
            // fis = activity.openFileInput(APP_CONFIG);

            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在files目录下
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public void set(Properties ps) {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }
}
