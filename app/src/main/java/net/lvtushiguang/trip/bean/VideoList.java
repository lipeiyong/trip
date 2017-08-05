package net.lvtushiguang.trip.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频列表
 * Created by 薰衣草 on 2017/2/18.
 */
public class VideoList extends Entity implements ListEntity<Video>{

    public final static int CATALOG_ALL = 1;

    private List<Video> reContent = new ArrayList<Video>();

    public List<Video> getReContent() {
        return reContent;
    }

    public void setReContent(List<Video> reContent) {
        this.reContent = reContent;
    }

    @Override
    public List<Video> getList() {
        return reContent;
    }
}
