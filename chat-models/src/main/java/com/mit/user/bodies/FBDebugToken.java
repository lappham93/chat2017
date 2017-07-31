package com.mit.user.bodies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Hung Le on 2/15/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FBDebugToken {

    private FBDebugTokenData data;

    public FBDebugTokenData getData() {
        return data;
    }

    public void setData(FBDebugTokenData data) {
        this.data = data;
    }
}
