package com.mit.upload.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Hung on 3/24/2017.
 */

@Document(collection = "upload_temp")
public class UploadTemp {
    private long id;
    private long userId;
    private String name;
    private int type;
    private int total;
    private int size;
    private int totalComplete;
    private int sizeComplete;
    private List<Boolean> check;
    private List<Long> tempFiles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getTotalComplete() {
        return totalComplete;
    }

    public void setTotalComplete(int totalComplete) {
        this.totalComplete = totalComplete;
    }

    public int getSizeComplete() {
        return sizeComplete;
    }

    public void setSizeComplete(int sizeComplete) {
        this.sizeComplete = sizeComplete;
    }

    public List<Boolean> getCheck() {
        return check;
    }

    public void setCheck(List<Boolean> check) {
        this.check = check;
    }

    public List<Long> getTempFiles() {
        return tempFiles;
    }

    public void setTempFiles(List<Long> tempFiles) {
        this.tempFiles = tempFiles;
    }
}
