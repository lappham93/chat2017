package com.mit.user.bodies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Hung Le on 2/20/17.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class FBPictureData {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
