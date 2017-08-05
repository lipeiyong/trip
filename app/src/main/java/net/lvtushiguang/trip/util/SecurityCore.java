package net.lvtushiguang.trip.util;

import android.content.Context;
import android.util.Log;

import net.lvtushiguang.trip.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* *
 *类名：SecurityCore
 *功能：APP安全处理类
 *详细：该类是请求、通知返回所调用的公用函数核心处理文件，不需要修改
 *版本：1.0
 *日期：2016-08-26
 *说明：
 *以下代码是针对于CloudManag项目，可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究CManage接口使用，只是提供一个参考
 *
 *************************注意*************************
 *可通过查看log日志的的数据，来检查网站安全情况
 */
public class SecurityCore {
    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("")
                    || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 把数组所有元素，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());

        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord, String filename) {
    }

    /**
     * 对数据进行AES加密并签名
     *
     * @param sArray  待处理数据
     * @param aes_key AES密钥
     * @return 返回已签名的json字符串
     */
    public static String encryptAndSignData(Map<String, String> sArray,
                                            String aes_key) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        JSONObject result = new JSONObject();

        if (sArray == null || sArray.size() <= 0) {
            return null;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("")) {
                continue;
            }

            value = AESUtils.encrypt(value, aes_key);
            params.put(key, value);
            result.put(key, value);
        }

        // 获取待签名字符串
        String preSignStr = createLinkString(params);
        preSignStr = preSignStr + "&key=" + aes_key;
        String sign = MD5Utils.MD5Encode(preSignStr, "utf-8").toUpperCase();
        result.put("sing", sign);

        return result.toString();
    }

    /**
     * 校验数字签名
     *
     * @param sArray  待签名数据
     * @param aes_key AES密钥
     * @param sing    数字签名
     * @return
     */
    public static boolean verify(Map<String, String> sArray, String aes_key,
                                 String sing) {
        // 过滤空值、sign参数
        Map<String, String> sParaNew = paraFilter(sArray);
        // 获取待签名字符串
        String preSignStr = createLinkString(sParaNew);
        preSignStr = preSignStr + "&key=" + aes_key;
        String signValue = MD5Utils.MD5Encode(preSignStr, "utf-8").toUpperCase();

        if (signValue.equals(sing)) {
            return true;
        }

        return false;
    }

    /**
     * 解析PC返回的数据<br/>
     *
     * @param context 上下文
     * @param content 待解析数据
     * @return 解析异常返回null, 否则返回解密后的数据
     */
    public static Map<String, String> decrypt(Context context, JSONObject content) {
        String aes_key = PreferenceHelper.readString(context, AppConfig.SP_CONFIG,
                AppConfig.AES_KEY, "");
        String sign = null;
        try {
            if (!aes_key.equals("")) {
                Map<String, String> params = new HashMap<>();
                Iterator it = content.keys();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    params.put(key, AESUtils.decrypt(content.getString(key), aes_key));
                }
                sign = params.get("sign");
                if (sign != null) {
                    boolean result = verify(params, aes_key, sign);
                    if (result)
                        return params;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * JSONObject转换为Map对象
     *
     * @param content
     * @return 解析异常返回null, 否则返回解密后的数据
     */
    public static Map<String, Object> toMap(JSONObject content) {
        Map<String, Object> resMap = new HashMap<>();
        Iterator it = content.keys();
        try {
            while (it.hasNext()) {
                String key = it.next().toString();
                resMap.put(key, content.getString(key));
            }
        } catch (JSONException e) {
            Log.e("error", "SecurityCore.Java: " + e.toString());
        }

        return resMap;
    }
}
