package com.mit.utils;

import com.mit.conts.Common;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.util.UriComponents;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkBuilder {

	private static Logger logger = LoggerFactory.getLogger(LinkBuilder.class);

	public static final String _photoLinkFormat = Common.DOMAIN_FILE + "/bq/load/%s/photo/%s";
	public static final String _photoResizeLinkFormat = Common.DOMAIN_FILE + "/bq/load/%s/photo/%s?size=%d";
	public static final String _shareLinkFormat = Common.DOMAIN_FILE + "/bq/load/%s/share/%s";
	public static final Pattern _feedSocketLinkPattern = Pattern.compile("/user/queue/topic/feed-(\\d+)$");
	public static final Pattern _subfeedSocketLinkPattern = Pattern.compile("/user/queue/topic/subfeed-(\\d+)$");
	public static final String _messageUserQueue = "/user/queue/message";

	public static String buildPhotoLink(long id, String objectName) {
		String link = "";
		if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
			link = String.format(_photoLinkFormat, objectName, idNoise);
		}

		return link;
	}
	
	public static String buildPhotoLink(long id, String objectName, int size) {
		String link = "";
		if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
			link = String.format(_photoResizeLinkFormat, objectName, idNoise, size);
		}

		return link;
	}

	public static String buildApiSocketLink() {
		return Common.DOMAIN_API_SOCKET + "/api/websocket";
	}

	public static String buildUploadSocketLink() {
		return Common.DOMAIN_UPLOAD_SOCKET + "/upload/websocket";
	}

	public static String buildCommentSocketLink(long id, boolean isSubFeed) {
		if (isSubFeed) {
			return "/topic/subfeed-" + id;
		} 
		return "/topic/feed-" + id;
	}
	
	public static String buildShareLink(long id, String objectName) {
		String link = "";
		if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
			link = String.format(_shareLinkFormat, objectName, idNoise);
		}

		return link;
	}

	public static String buildControllerLink(Object invocationValue) {
        ControllerLinkBuilder linkBuilder = ControllerLinkBuilder.linkTo(invocationValue);
        UriComponents uriComponents = linkBuilder.toUriComponentsBuilder().build();
        String localhost;
        if (uriComponents.getPort() >=0) {
        	localhost = String.format("%s://%s:%s", uriComponents.getScheme(), uriComponents.getHost(), uriComponents.getPort());
		} else {
			localhost = String.format("%s://%s", uriComponents.getScheme(), uriComponents.getHost());
		}

		return uriComponents.toUriString().replace(localhost, Common.DOMAIN_APP + Common.API_PREFIX);
	}

	public static long matchFeedSocketLink(String link) {
		Matcher m = _feedSocketLinkPattern.matcher(link);
		if (m.find()) {
			return NumberUtils.toLong(m.group(1));
		}

		return 0;
	}

	public static long matchSubfeedSocketLink(String link) {
		Matcher m = _subfeedSocketLinkPattern.matcher(link);
		if (m.find()) {
			return NumberUtils.toLong(m.group(1));
		}

		return 0;
	}

	public static boolean matchMessageQueueLink(String link) {
		return _messageUserQueue.equals(link);
	}
	
	public static String buildBillLink(long landscapingId) {
		String link = "";
		if (landscapingId > 0) {
			String idNoise = MIdNoise.enNoiseLId(landscapingId);
			link = Common.DOMAIN_FILE + "/homber/load/bill/" + idNoise; 
		}

		return link;
	}
}
