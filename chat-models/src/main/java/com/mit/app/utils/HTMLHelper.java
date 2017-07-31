package com.mit.app.utils;

import com.mit.app.enums.AppConstKey;
import com.mit.app.services.AppConstManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Hung Le on 5/30/17.
 */

@Component
public class HTMLHelper {
    private static AppConstManager appConstManager;

    @Autowired
    private AppConstManager _appConstManager;

    @PostConstruct
    public void init() {
        appConstManager = _appConstManager;
    }

    public static String wrapStyle(String html) {
        return appConstManager.getString(AppConstKey.PROVIDER_APPLICATION_STYLE) + html;
    }
}
