package com.mit.notification.bodies;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by Hung Le on 5/8/17.
 */

@ApiModel
public class ViewNewsBody {
    private long userId;
    private List<Long> ids;

    @ApiModelProperty(required = false)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
