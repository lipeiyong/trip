package net.lvtushiguang.trip.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.webkit.JavascriptInterface;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.base.BaseListFragment;
import net.lvtushiguang.trip.bean.Constants;
import net.lvtushiguang.trip.bean.Notice;
import net.lvtushiguang.trip.bean.SimpleBackPage;
import net.lvtushiguang.trip.fragment.MessageDetailFragment;
import net.lvtushiguang.trip.interf.ICallbackResult;
import net.lvtushiguang.trip.service.DownloadService;
import net.lvtushiguang.trip.service.DownloadService.DownloadBinder;
import net.lvtushiguang.trip.ui.LoginActivity;
import net.lvtushiguang.trip.ui.SimpleBackActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 界面帮助类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月10日 下午3:33:36
 */
public class UIHelper {

    /**
     * 全局web样式
     */
    // 链接样式文件，代码块高亮的处理
    public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/client.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/detail_page.js\"></script>"
            + "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>"
            + "<script type=\"text/javascript\">function showImagePreview(var url){window.location.url= url;}</script>"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/common.css\">";
    public final static String WEB_STYLE = linkCss;

    public static final String WEB_LOAD_IMAGES = "<script type=\"text/javascript\"> var allImgUrls = getAllImgSrc(document.body.innerHTML);</script>";

    private static final String SHOWIMAGE = "ima-api:action=showImage&data=";

    /**
     * 显示登录界面
     *
     * @param context
     */
    public static void showLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void showLinkRedirect(Context context, int objType,
                                        String objId, String objKey) {
        switch (objType) {
            case URLsUtils.URL_OBJ_TYPE_NEWS:
//                showNewsDetail(context, objId, -1);
                break;
            case URLsUtils.URL_OBJ_TYPE_QUESTION:
//                showPostDetail(context, objId, 0);
                break;
            case URLsUtils.URL_OBJ_TYPE_QUESTION_TAG:
//                showPostListByTag(context, objKey);
                break;
            case URLsUtils.URL_OBJ_TYPE_SOFTWARE:
//                showSoftwareDetail(context, objKey);
                break;
            case URLsUtils.URL_OBJ_TYPE_ZONE:
                showUserCenter(context, objId, objKey);
                break;
            case URLsUtils.URL_OBJ_TYPE_TWEET:
//                showTweetDetail(context, null, objId);
                break;
            case URLsUtils.URL_OBJ_TYPE_BLOG:
//                showBlogDetail(context, objId, 0);
                break;
            case URLsUtils.URL_OBJ_TYPE_OTHER:
//                openBrowser(context, objKey);
            case URLsUtils.URL_OBJ_TYPE_TEAM:
                openSysBrowser(context, objKey);
                break;
            case URLsUtils.URL_OBJ_TYPE_GIT:
                openSysBrowser(context, objKey);
                break;
        }
    }

    /**
     * 显示消息详情页面
     *
     * @param context
     * @param message
     */
    public static void showMessageDetail(Context context, net.lvtushiguang.trip.bean.Message message) {
        if (message != null) {
            Bundle args = new Bundle();
            args.putSerializable(MessageDetailFragment.BUNDLE_KEY_MESSAGE, message);
            showSimpleBack(context, SimpleBackPage.MESSAGE_DETAIL, args);
        }
    }

    /**
     * 显示用户中心页面
     *
     * @param context
     * @param hisuid
     * @param hisuid
     * @param hisname
     */
    public static void showUserCenter(Context context, String hisuid,
                                      String hisname) {
        if (StringUtils.isEmpty(hisuid) && hisname.equalsIgnoreCase("匿名")) {
            AppContext.showToast("提醒你，该用户为非会员");
            return;
        }
        Bundle args = new Bundle();
        args.putString("his_id", hisuid);
        args.putString("his_name", hisname);
//        showSimpleBack(context, SimpleBackPage.USER_CENTER, args);
    }

    /**
     * 打开系统中的浏览器
     *
     * @param context
     * @param url
     */
    public static void openSysBrowser(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            AppContext.showToastShort("无法浏览此网页");
        }
    }

    /**
     * ******************************************************************************************
     */

    /**
     * Actionbar Popup QrCode
     */
    public static void showQrCodeGathering(Context context) {

        showSimpleBack(context, SimpleBackPage.ACTIONBAR_POPUP_QRCODE);
    }

    /**
     * 显示用户收藏界面
     *
     * @param context
     */
    public static void showUserFavorite(Context context, int uid) {

        Bundle args = new Bundle();
        args.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, uid);
        showSimpleBack(context, SimpleBackPage.MY_INFORMATION_FAVORITE);
    }

    /**
     * 显示用户的红包中心
     *
     * @param context
     */
    public static void showRedEnvelopes(Context context) {
        showSimpleBack(context, SimpleBackPage.MY_INFORMATION_RED_ENVELOPES);
    }

    /**
     * 显示用户的钱包中心
     *
     * @param context
     */
    public static void shoWallet(Context context) {
        showSimpleBack(context, SimpleBackPage.MY_INFORMATION_WALLET);
    }

    /**
     * 显示用户的福利社界面
     *
     * @param context
     */
    public static void showCommunity(Context context) {
        showSimpleBack(context, SimpleBackPage.MY_INFORMATION_COMMUNITY);
    }

    /**
     * 显示用户的设置中心
     *
     * @param context
     */
    public static void showSetting(Context context) {
        showSimpleBack(context, SimpleBackPage.SETTING);
    }

    /**
     * ******************************************************************************************
     */

    /**
     * 发送App异常崩溃报告
     *
     * @param context
     * @param context
     */
    public static void sendAppCrashReport(final Context context) {

        DialogHelp.getConfirmDialog(context, "程序发生异常", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 退出
                System.exit(-1);
            }
        }).show();
    }

    /**
     * 发送通知广播
     *
     * @param context
     * @param notice
     */
    public static void sendBroadCast(Context context, Notice notice) {
        if (!((AppContext) context.getApplicationContext()).isLogin()
                || notice == null)
            return;
        Intent intent = new Intent(Constants.INTENT_ACTION_NOTICE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("notice_bean", notice);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page,
                                      Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    ;

    /**
     * 清除app缓存
     *
     * @param activity
     */
    public static void clearAppCache(Activity activity) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    AppContext.showToastShort("缓存清除成功");
                } else {
                    AppContext.showToastShort("缓存清除失败");
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    AppContext.getInstance().clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    public static void openDownLoadService(Context context, String downurl,
                                           String tilte) {
        final ICallbackResult callback = new ICallbackResult() {

            @Override
            public void OnBackResult(Object s) {
            }
        };
        ServiceConnection conn = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadBinder binder = (DownloadBinder) service;
                binder.addCallback(callback);
                binder.start();

            }
        };
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, downurl);
        intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, tilte);
        context.startService(intent);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @JavascriptInterface
    public static void showImagePreview(Context context, String[] imageUrls) {
//        ImagePreviewActivity.showImagePrivew(context, 0, imageUrls);
    }

    @JavascriptInterface
    public static void showImagePreview(Context context, int index,
                                        String[] imageUrls) {
//        ImagePreviewActivity.showImagePrivew(context, index, imageUrls);
    }

    /**
     * url跳转
     *
     * @param context
     * @param url
     */
    public static void showUrlRedirect(Context context, String url) {
        if (url == null)
            return;
        if (url.contains("city.oschina.net/")) {
            int id = StringUtils.toInt(url.substring(url.lastIndexOf('/') + 1));
            UIHelper.showEventDetail(context, id);
            return;
        }

        if (url.startsWith(SHOWIMAGE)) {
            String realUrl = url.substring(SHOWIMAGE.length());
            try {
                JSONObject json = new JSONObject(realUrl);
                int idx = json.optInt("index");
                String[] urls = json.getString("urls").split(",");
                showImagePreview(context, idx, urls);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        URLsUtils urls = URLsUtils.parseURL(url);
        if (urls != null) {
            showLinkRedirect(context, urls.getObjType(), urls.getObjId(),
                    urls.getObjKey());
        } else {
            openBrowser(context, url);
        }
    }

    /**
     * 打开内置浏览器
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {

    }

    /**
     * 显示活动详情
     *
     * @param context
     * @param eventId
     */
    public static void showEventDetail(Context context, int eventId) {
    }
}
