package net.lvtushiguang.trip.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 资讯列表
 * Created by 薰衣草 on 2017/2/18.
 */
public class MessageList extends Entity implements ListEntity<Message>{

    public final static int CATALOG_ALL = 1;

    private List<Message> reContent = new ArrayList<Message>();

    public List<Message> getReContent() {
        return reContent;
    }

    public void setReContent(List<Message> reContent) {
        this.reContent = reContent;
    }

    @Override
    public List<Message> getList() {
        return reContent;
    }
}
