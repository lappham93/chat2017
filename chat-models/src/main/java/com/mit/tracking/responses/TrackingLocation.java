package com.mit.tracking.responses;

import com.mit.map.entities.Coordinate;

/**
 * Created by Hung Le on 3/13/17.
 */
public class TrackingLocation {
    private Coordinate location;
    private int status;

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
