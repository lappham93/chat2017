package com.mit.asset.responses;

import com.mit.asset.entities.Link;

public class LinkResponse {
	private long id;
	private String link;
	private String site;
	private String title;
	private String description;
	private String thumbnail;

	public LinkResponse(Link link) {
		this.id = link.getId();
		this.link = link.getLink();
		this.site = link.getSite();
		this.title = link.getTitle();
		this.description = link.getDesc();
//		this.thumbnail = LinkBuilder.buildPhotoLink(link.getThumbnail(), ObjectType.FEED.getLowerName());
		this.thumbnail = link.getThumbnailExt();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

}
