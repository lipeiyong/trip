package net.lvtushiguang.trip.bean;

import java.io.Serializable;

/**
 * 通知信息实体类
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressWarnings("serial")
public class Notice extends Entity implements Serializable {

    private String chargeState1;
    private String chargeState2;
    private String chargeState3;
    private String chargeState4;
    private String chargeState5;

    public String getChargeState1() {
        return chargeState1;
    }

    public void setChargeState1(String chargeState1) {
        this.chargeState1 = chargeState1;
    }

    public String getChargeState2() {
        return chargeState2;
    }

    public void setChargeState2(String chargeState2) {
        this.chargeState2 = chargeState2;
    }

    public String getChargeState3() {
        return chargeState3;
    }

    public void setChargeState3(String chargeState3) {
        this.chargeState3 = chargeState3;
    }

    public String getChargeState4() {
        return chargeState4;
    }

    public void setChargeState4(String chargeState4) {
        this.chargeState4 = chargeState4;
    }

    public String getChargeState5() {
        return chargeState5;
    }

    public void setChargeState5(String chargeState5) {
        this.chargeState5 = chargeState5;
    }
}
