package net.lvtushiguang.trip.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 微头条列表
 * Created by 薰衣草 on 2017/2/18.
 */
public class TopInfoList extends Entity implements ListEntity<TopInfo> {

    public final static int CATALOG_ALL = 1;

    private List<TopInfo> reContent = new ArrayList<TopInfo>();

    public List<TopInfo> getReContent() {
        return reContent;
    }

    public void setReContent(List<TopInfo> reContent) {
        this.reContent = reContent;
    }

    @Override
    public List<TopInfo> getList() {
        return reContent;
    }
}
