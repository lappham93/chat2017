package com.mit.user.bodies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Hung Le on 2/20/17.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class FBPicture {
    private FBPictureData data;

    public FBPictureData getData() {
        return data;
    }

    public void setData(FBPictureData data) {
        this.data = data;
    }
}
