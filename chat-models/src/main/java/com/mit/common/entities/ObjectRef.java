package com.mit.common.entities;

/**
 * Created by Hung Le on 2/21/17.
 */
public class ObjectRef {
    private long id;
    private int type;

    public ObjectRef() {
    	super();
    }
    
    public ObjectRef(int type, long id) {
    	super();
    	this.type = type;
    	this.id = id;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
