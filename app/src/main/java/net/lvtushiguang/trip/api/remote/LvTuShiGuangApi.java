package net.lvtushiguang.trip.api.remote;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.AppException;
import net.lvtushiguang.trip.api.ApiHttpClient;
import net.lvtushiguang.trip.util.MD5Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LvTuShiGuangApi {

    /**
     * 登陆
     *
     * @param account
     * @param password
     * @param handler
     */
    public static void login(String account, String password, AsyncHttpResponseHandler handler) {
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("code", "00000000");
        jsonParams.put("userCode", account);
        jsonParams.put("userPs", MD5Utils.MD5Encode(password, "GBK").toUpperCase());
        jsonParams.put("userType", "9");
        jsonParams.put("time", (new SimpleDateFormat("yyyyMMDDHHmmss")).format(new Date()));
        jsonParams.put("keyt", MD5Utils.MD5Encode("00000000" + jsonParams.getString("time")
                + ApiHttpClient.key, "GBK").toUpperCase());

        RequestParams params = new RequestParams();
        params.put("data", jsonParams.toJSONString());

        String loginurl = "huaelComm_sysUserChk";

        ApiHttpClient.post(loginurl, params, handler);
    }

    /**
     * 获取消息列表
     *
     * @param uid
     * @param pageNo
     * @param handler
     */
    public static void getMessageList(String uid, int pageNo, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("userid", uid);
        params.put("pageNo", pageNo);
        params.put("pageSize", AppContext.PAGE_SIZE);

        String loginurl = "getMessages";

        ApiHttpClient.post(loginurl, params, handler);
    }

    /**
     * 获取订单列表
     *
     * @param uid
     * @param payType
     * @param pageNo
     * @param handler
     */
    public static void getOrderList(String uid, String payType, int pageNo, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("user.id", uid);
        params.put("payType", payType);
        params.put("page.pageNo", pageNo);
        params.put("page.pageSize", AppContext.PAGE_SIZE);

        String url = String.format(ApiHttpClient.CLOUD_URL, "getOrderList");

        ApiHttpClient.post(url, params, handler);
    }

    /**
     * 上传车牌/交易完成/逃票
     *
     * @param uid
     * @param taskid
     * @param state
     * @param carnumber
     * @param manage
     * @param handler
     */
    public static void updateTaskType(String uid, String taskid, String state,
                                      String carnumber, String manage, AsyncHttpResponseHandler handler) {
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("code", "00000000");
        jsonParams.put("time", (new SimpleDateFormat("yyyyMMDDHHmmss")).format(new Date()));
        jsonParams.put("keyt", MD5Utils.MD5Encode("00000000" + jsonParams.getString("time")
                + ApiHttpClient.key, "GBK").toUpperCase());

        JSONArray contentArray = new JSONArray();
        JSONObject item = new JSONObject();
        item.put("recTaskId", taskid);
        item.put("recChargeState", state);
        item.put("recParkNo", carnumber);
        item.put("recSamount", manage);
        item.put("recAppName", uid);
        item.put("recAppTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
        contentArray.add(item);

        jsonParams.put("content", contentArray);

        RequestParams params = new RequestParams();
        params.put("data", jsonParams.toJSONString());

        String loginurl = "huaelComm_writeParkTaskApp";

        ApiHttpClient.post(loginurl, params, handler);
    }

    /**
     * 获取通知
     *
     * @param handler
     */
    public static void getNotice(AsyncHttpResponseHandler handler) {
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("code", "00000000");
        jsonParams.put("userCode", AppContext.getInstance().getLoginUid());
        jsonParams.put("time", (new SimpleDateFormat("yyyyMMDDHHmmss")).format(new Date()));
        jsonParams.put("keyt", MD5Utils.MD5Encode("00000000" + jsonParams.getString("time")
                + ApiHttpClient.key, "GBK").toUpperCase());

        RequestParams params = new RequestParams();
        params.put("data", jsonParams.toJSONString());

        String loginurl = "huaelComm_readparkTaskNum";

        ApiHttpClient.post(loginurl, params, handler);
    }

    public static void openIdLogin(String s) {

    }

    private static void uploadLog(String data, String report,
                                  AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("app", "1");
        params.put("report", report);
        params.put("msg", data);
        ApiHttpClient.post("action/api/user_report_to_admin", params, handler);
    }

    public static void checkUpdate(AsyncHttpResponseHandler handler) {
        ApiHttpClient.get("http://www.huael.top:8081/userfiles/MobileAppVersion.xml", handler);
    }

    /**
     * BUG上报
     *
     * @param data
     * @param handler
     */
    public static void uploadLog(String data, AsyncHttpResponseHandler handler) {
        uploadLog(data, "1", handler);
    }

    /**
     * 反馈意见
     *
     * @param data
     * @param handler
     */
    public static void feedback(String data, AsyncHttpResponseHandler handler) {
        uploadLog(data, "2", handler);
    }

    /***
     * 客户端扫描二维码登陆
     *
     * @param url
     * @param handler
     * @return void
     * @author 火蚁 2015-3-13 上午11:45:47
     */
    public static void scanQrCodeLogin(String url,
                                       AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        String uuid = url.substring(url.lastIndexOf("=") + 1);
        params.put("uuid", uuid);
        ApiHttpClient.getDirect(url, handler);
    }

    /***
     * 使用第三方登陆
     *
     * @param catalog    类别
     * @param openIdInfo 第三方的info
     * @param handler    handler
     */
    public static void open_login(String catalog, String openIdInfo, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("catalog", catalog);
        params.put("openid_info", openIdInfo);
        ApiHttpClient.post("action/api/openid_login", params, handler);
    }

    /***
     * 第三方登陆账号绑定
     *
     * @param catalog    类别（QQ、wechat）
     * @param openIdInfo 第三方info
     * @param userName   用户名
     * @param pwd        密码
     * @param handler    handler
     */
    public static void bind_openid(String catalog, String openIdInfo, String userName, String pwd, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("catalog", catalog);
        params.put("openid_info", openIdInfo);
        params.put("username", userName);
        params.put("pwd", pwd);
        ApiHttpClient.post("action/api/openid_bind", params, handler);
    }

    /***
     * 使用第三方账号注册
     *
     * @param catalog    类别（qq、wechat）
     * @param openIdInfo 第三方info
     * @param handler    handler
     */
    public static void openid_reg(String catalog, String openIdInfo, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("catalog", catalog);
        params.put("openid_info", openIdInfo);
        ApiHttpClient.post("action/api/openid_reg", params, handler);
    }

    /**
     * 获取所有关注好友列表
     *
     * @param uid     指定用户UID
     * @param handler
     */
    public static void getAllFriendsList(String uid, int relation, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("relation", relation);
        params.put("all", 1);
        ApiHttpClient.get("action/api/friends_list", params, handler);
    }

    public static void getNotices(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", AppContext.getInstance().getLoginUid());
        ApiHttpClient.get("action/api/user_notice", params, handler);
    }

    /**
     * 清空通知消息
     *
     * @param uid
     * @param type 1:私信 2:平台推送 3:订单信息
     * @return
     * @throws AppException
     */
    public static void clearNotice(int uid, int type,
                                   AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("type", type);
        ApiHttpClient.post("action/api/notice_clear", params, handler);
    }

    /**
     * 获取我的个人信息
     *
     * @param uid
     * @param handler
     */
    public static void getMyInformation(int uid,
                                        AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        ApiHttpClient.get("action/api/my_information", params, handler);
    }

    /**
     * 获取个人钱包金额
     *
     * @param userCode
     * @param userName
     */
    public static void getBalance(String userCode, String userName, AsyncHttpResponseHandler handler) {
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("VersionId", "001");
        jsonParams.put("ClientID", "01");
        jsonParams.put("MerchantID", "00000002");
        jsonParams.put("PhoneNumber", "");
        jsonParams.put("ICCID", userCode);
        jsonParams.put("Name", userName);
        jsonParams.put("MAC", MD5Utils.MD5Encode("0010100000002"
                        + userCode + "707C11496522DB45001644CBD413EE44",
                "GBK").toUpperCase());


        RequestParams params = new RequestParams();
        params.put("data", jsonParams.toJSONString());

        String loginurl = "YKT_YHYECX";

        ApiHttpClient.post(loginurl, params, handler);
    }

    /**
     * 生成支付宝订单
     *
     * @param userid
     * @param totalFee
     * @param orderType
     * @param content
     * @param handler
     */
    public static void getALiPay(String userid, String totalFee, String orderType, String content, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("user.id", userid);
        params.put("totalFee", totalFee);
        params.put("orderType", orderType);
        params.put("payType", "1");
        params.put("content", content);

        String loginurl = "http://www.huael.top:8081/sun/api/deal/placeOrder/alipay";
//        String loginurl = "http://192.168.2.113:8083/sun/api/deal/placeOrder/alipay";

        ApiHttpClient.post(loginurl, params, handler);
    }

    /**
     * 生成微信订单
     *
     * @param userid
     * @param totalFee
     * @param orderType
     * @param content
     * @param handler
     */
    public static void getWXPay(String userid, String totalFee, String orderType, String content, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("user.id", userid);
        params.put("totalFee", totalFee);
        params.put("orderType", orderType);
        params.put("payType", "0");
        params.put("content", content);

        String loginurl = "http://www.huael.top:8081/sun/api/deal/placeOrder/weixin";

        ApiHttpClient.post(loginurl, params, handler);
    }
}
