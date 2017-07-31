package com.mit.http;

import com.mit.define.ApiError;

import java.util.Date;

/**
 * Created by Hung Le on 2/13/17.
 */
public class MessageApiResponse<T> extends ApiResponse<T> {
    public MessageApiResponse() {
    }

    public MessageApiResponse(int code, String msg) {
        super(code, msg);
    }

    public MessageApiResponse(ApiError apiError) {
        super(apiError);
    }

    public MessageApiResponse(T data) {
        super(data);
    }

    public Date getLastUpdate() {
        return new Date();
    }
}
