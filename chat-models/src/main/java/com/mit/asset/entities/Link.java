package com.mit.asset.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mit.common.entities.TimeDoc;

@Document(collection = "link")
public class Link extends TimeDoc<Long> {
	@Id
	private long id;
	private String link;
	private String site;
	private String title;
	private String desc;
	private long thumbnail;
	private String thumbnailExt;

	private int status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(long thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getThumbnailExt() {
		return thumbnailExt;
	}

	public void setThumbnailExt(String thumbnailExt) {
		this.thumbnailExt = thumbnailExt;
	}

}
