package net.lvtushiguang.trip.bean;

/**
 * Created by PhoenixTree on 2017/6/27.
 * 视频实体类
 */

public class Video extends Entity {
    private String title;//标题
    private String playCount;//播放次数
    private String duration;//持续时间
    private String url;//视频url
    private String authorIcon;//作者头像
    private String authorName;//作者名
    private String attention;//0-未关注 1-已关注
    private String commentCount;//评论数
    private String cover;//封面

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorIcon() {
        return authorIcon;
    }

    public void setAuthorIcon(String authorIcon) {
        this.authorIcon = authorIcon;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
