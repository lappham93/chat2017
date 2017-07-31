package com.mit.notification.responses;

import com.mit.common.enums.ObjectType;
import com.mit.notification.entities.MessageNews;
import com.mit.utils.LinkBuilder;

/**
 * Created by Hung Le on 4/4/17.
 */
public class MessageNewsResponse extends NewsResponse {
    private String msg;
    private String thumb;

    public MessageNewsResponse(MessageNews messageNews) {
        super(messageNews);

        this.msg = messageNews.getMsg();
        this.thumb = LinkBuilder.buildPhotoLink(messageNews.getThumb(), ObjectType.NEWS.getName());
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
