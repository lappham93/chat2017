package com.mit.upload.responses;

import com.mit.asset.responses.PhotoResponse;

/**
 * Created by Hung on 3/24/2017.
 */
public class FilePartResponse {
    private long id;
    private int part;
    private PhotoResponse photo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public PhotoResponse getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoResponse photo) {
        this.photo = photo;
    }
}
