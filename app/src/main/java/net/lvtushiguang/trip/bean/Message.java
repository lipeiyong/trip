package net.lvtushiguang.trip.bean;

import java.io.Serializable;

/**
 * 资讯信息实体类
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressWarnings("serial")
public class Message extends Entity implements Serializable {

    private String title;//标题
    private String content;//内容
    private String authorid;//作者id
    private String authorname;//作者姓名
    private String commentCount;//评论数
    private String sendertime;//发送时间
    private String messagetype;//资讯类别：0-普通资讯，1-视频资讯
    private String sort;//排序：0-置顶 1-热 2-广告 3-普通
    private String image1;
    private String image2;
    private String image3;
    private String videourl;//视频url

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getSendertime() {
        return sendertime;
    }

    public void setSendertime(String sendertime) {
        this.sendertime = sendertime;
    }

    public String getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }
}
