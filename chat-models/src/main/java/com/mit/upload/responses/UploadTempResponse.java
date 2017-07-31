package com.mit.upload.responses;

import com.mit.upload.entities.UploadTemp;

/**
 * Created by Hung on 3/24/2017.
 */
public class UploadTempResponse {
    private long id;
    private int total;
    private int size;
    private int type;

    public UploadTempResponse(UploadTemp uploadTemp) {
        this.id = uploadTemp.getId();
        this.total = uploadTemp.getTotal();
        this.size = uploadTemp.getSize();
        this.type = uploadTemp.getType();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
