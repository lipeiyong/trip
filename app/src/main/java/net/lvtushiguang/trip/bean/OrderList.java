package net.lvtushiguang.trip.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表
 * Created by 薰衣草 on 2017/2/18.
 */
public class OrderList extends Entity implements ListEntity<Order> {

    private List<Order> reContent = new ArrayList<Order>();

    public List<Order> getReContent() {
        return reContent;
    }

    public void setReContent(List<Order> reContent) {
        this.reContent = reContent;
    }

    @Override
    public List<Order> getList() {
        return reContent;
    }
}
