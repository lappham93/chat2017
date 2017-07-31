package com.mit.common.entities;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import com.mit.utils.ZonedDateTimeUtils;

/**
 * Created by Hung Le on 2/21/17.
 */
public abstract class TimeDoc<T extends Serializable> implements Persistable<T> {
    @CreatedDate
    protected Date createdDate;
    @LastModifiedDate
    protected Date updatedDate;
    @Transient
    private boolean newed;

    public Date getCreatedDate() {
        if(createdDate == null) {
            createdDate = ZonedDateTimeUtils.toDate(ZonedDateTimeUtils.now());
        }
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        if(updatedDate == null) {
            updatedDate = getCreatedDate();
        }
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

	public boolean isNewed() {
		return newed;
	}

	public void setNewed(boolean newed) {
		this.newed = newed;
	}
	
	public abstract void setId(T id);

	@Override
	public boolean isNew() {
		return newed;
	}

}
