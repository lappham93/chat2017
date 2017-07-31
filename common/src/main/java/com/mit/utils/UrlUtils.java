package com.mit.utils;

import com.mit.conts.Common;

/**
 * Created by Hung Le on 2/16/17.
 */
public class UrlUtils {

    public static String getUserPhotoUrl(long id, int size) {
        String url = "";
        if (id > 0) {
            String linkIdNoise = MIdNoise.enNoiseLId(id);
            url = Common.DOMAIN_FILE + "/ts/load/photo/user/" + linkIdNoise;
        }

        if (size > 0) {
            url += "?size=" + size;
        }

        return url;
    }

    public static String getUserPhotoUrl(long id) {
        return getUserPhotoUrl(id, 0);
    }
}
