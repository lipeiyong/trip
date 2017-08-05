package net.lvtushiguang.trip.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天详细信息实体类
 *
 * @author 铂金小鸟（http://my.oschina.net/fants）
 * @Created 2015年9月16日 上午4:20:01
 */
@SuppressWarnings("serial")
public class MessageDetailList extends Entity implements ListEntity<MessageDetail> {

    private int allCount;

    private int pageSize;

    private List<MessageDetail> messagelist = new ArrayList<MessageDetail>();

    public int getPageSize() {
        return pageSize;
    }

    public int getMessageCount() {
        return allCount;
    }

    public List<MessageDetail> getMessagelist() {
        return messagelist;
    }

    @Override
    public List<MessageDetail> getList() {
        return messagelist;
    }

}
