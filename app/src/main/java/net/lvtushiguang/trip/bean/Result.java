package net.lvtushiguang.trip.bean;

import java.io.Serializable;

/**
 * 数据操作结果实体类
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressWarnings("serial")
public class Result implements Serializable {

    private String reCode;

    private String reMsg;

    public boolean OK() {
        return reCode.equals("000000");
    }

    public String getReCode() {
        return reCode;
    }

    public void setReCode(String reCode) {
        this.reCode = reCode;
    }

    public String getReMsg() {
        return reMsg;
    }

    public void setReMsg(String reMsg) {
        this.reMsg = reMsg;
    }
}
