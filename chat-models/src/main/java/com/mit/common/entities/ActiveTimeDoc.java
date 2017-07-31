package com.mit.common.entities;

import java.io.Serializable;

/**
 * Created by Hung Le on 2/21/17.
 */
public abstract class ActiveTimeDoc<T extends Serializable> extends TimeDoc<T> {
    protected boolean isActive;
    protected boolean isDeleted;

    public ActiveTimeDoc() {
    	super();
    	this.isActive = true;
    	this.isDeleted = false;
    }
    
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
