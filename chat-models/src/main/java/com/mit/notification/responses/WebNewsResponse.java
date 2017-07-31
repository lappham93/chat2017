package com.mit.notification.responses;

import com.mit.common.enums.ObjectType;
import com.mit.notification.entities.WebNews;
import com.mit.utils.LinkBuilder;

import java.net.URLDecoder;

/**
 * Created by Hung Le on 4/4/17.
 */
public class WebNewsResponse extends NewsResponse {
    private String url;
    private String msg;
    private String thumb;

    public WebNewsResponse(WebNews webNews) {
        super(webNews);
        try {
            this.url = URLDecoder.decode(webNews.getUrl(), "UTF-8");
        } catch (Exception e) {
            this.url = webNews.getUrl();
        }
        this.msg = webNews.getMsg();
        this.thumb = LinkBuilder.buildPhotoLink(webNews.getThumb(), ObjectType.NEWS.getName());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
