package com.mit.upload.bodies;

import com.mit.common.enums.ObjectType;
import com.mit.upload.entities.UploadTemp;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hung on 3/24/2017.
 */
public class FileInfo {

    private String name;
    private int total;
    private int size;
    @ApiModelProperty(value= ObjectType.VALUE_DESC)
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public UploadTemp toUploadTemp() {
        UploadTemp uploadTemp = new UploadTemp();
        uploadTemp.setName(getName());
        uploadTemp.setTotal(getTotal());
        uploadTemp.setSize(getSize());
        uploadTemp.setType(getType());
        List<Boolean> check = new ArrayList<>(getTotal());
        List<Long> tempFiles =  new ArrayList<>(getTotal());
        for(int i = 0; i < getTotal(); i++) {
            tempFiles.add(0L);
            check.add(false);
        }
        uploadTemp.setCheck(check);
        uploadTemp.setTempFiles(tempFiles);

        return uploadTemp;
    }
}
