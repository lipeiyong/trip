package net.lvtushiguang.trip.bean;

/**
 * 操作结果实体类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月14日 下午2:59:27
 */
@SuppressWarnings("serial")
public class ResultBean extends Base {

    private Result result;

    private Notice notice;

    private Comment comment;

    //现在pub_message接口返回的是comment对象。
    //@XStreamAlias("message")
    private MessageDetail message;

    private int relation;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public MessageDetail getMessage() {
        //现在pub_message接口返回的是comment对象。所以要转成message
        message = new MessageDetail();
        if (comment != null) {
            message.setId(comment.getId());
            message.setPortrait(comment.getPortrait());
            message.setAuthor(comment.getAuthor());
            message.setAuthorId(comment.getId());
            message.setContent(comment.getContent());
            message.setPubDate(comment.getPubDate());
        }
        return message;
    }

    public void setMessage(MessageDetail message) {
        this.message = message;
    }
}
