package net.lvtushiguang.trip.fragment.list;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.adapter.OrderAdapter;
import net.lvtushiguang.trip.api.remote.LvTuShiGuangApi;
import net.lvtushiguang.trip.base.BaseListFragment;
import net.lvtushiguang.trip.base.ListBaseAdapter;
import net.lvtushiguang.trip.bean.ListEntity;
import net.lvtushiguang.trip.bean.Order;
import net.lvtushiguang.trip.bean.OrderList;
import net.lvtushiguang.trip.bean.User;
import net.lvtushiguang.trip.interf.OnTabReselectListener;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 支付宝记录查询
 * Created by 薰衣草 on 2017/4/19.
 */

public class ScreeAlipayFragment extends BaseListFragment<Order> implements
        OnTabReselectListener {

    private static final String CACHE_KEY_PREFIX = "alipaylist_";
    private User user;
    private String type = "0";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = AppContext.getInstance().getLoginUser();
    }

    @Override
    protected ListBaseAdapter<Order> getListAdapter() {
        return new OrderAdapter();
    }

    @Override
    protected void sendRequestData() {
        LvTuShiGuangApi.getOrderList(user.getId(), "1", mCurrentPage + 1, mHandler);
    }

    @Override
    protected ListEntity<Order> parseList(JSONObject jo) throws Exception {
        OrderList list = JSON.parseObject(jo.toString(), OrderList.class);

        return list;
    }

    @Override
    public void onTabReselect() {
        onRefresh();
    }

    @Override
    public String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
    }

    @Override
    protected OrderList readList(Serializable seri) {
        return (OrderList) seri;
    }
}
