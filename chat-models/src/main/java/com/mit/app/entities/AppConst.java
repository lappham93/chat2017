package com.mit.app.entities;

import com.mit.common.entities.TimeDoc;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Hung Le on 3/20/17.
 */

@Document(collection = "app_const")
public class AppConst<T> extends TimeDoc<String> {
    @Id
    private String key;
    private T value;
    private String desc;
    private boolean isImutable;
    private boolean isNoCache;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

	@Override
	public String getId() {
		return key;
	}

	@Override
	public void setId(String id) {
		this.key = id;
	}

    public boolean isImutable() {
        return isImutable;
    }

    public void setImutable(boolean imutable) {
        isImutable = imutable;
    }

    public boolean isNoCache() {
        return isNoCache;
    }

    public void setNoCache(boolean noCache) {
        isNoCache = noCache;
    }
}
