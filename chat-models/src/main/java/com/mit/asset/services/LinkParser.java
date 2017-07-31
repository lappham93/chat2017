package com.mit.asset.services;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkParser {
	
	public static final int FAIL = -1;
	public static final int SUCCESS = 0;
	// define structure search
	private static final String[][] TITLE = { { "meta", "name", "title", "content" }, { "meta", "property", "og:title", "content" } };
	private static final String[][] DESCRIPTION = { { "meta", "name", "description", "content" }, { "meta", "property", "og:description", "content" }};
	private static final String[][] THUMBNAIL = { { "meta", "property", "og:image", "content" }, {"meta", "itemprop", "image", "content"},
			{ "img", null, null, "src" } };
	private static final String PREFIX = "http://";
	private static final String PREFIXS = "https://";

	private Document parser = null;
	private String link;
	private String title;
	private String description;
	private String site;
	private String thumbUri;
	private byte[] thumbData;
	private int status;

	public LinkParser(String link) {
		link = link.toLowerCase();
		if (!link.contains(PREFIX) && !link.contains(PREFIXS)) {
			link = PREFIX.concat(link);
		}
		this.link = link;
		try {
			parser = Jsoup.connect(link).timeout(10000).validateTLSCertificates(false).get();
			if (parser != null) {
				this.title = parseTitle();
				this.description = parseDescription();
				this.site = parseSite();
				this.thumbUri = parseThumbnail();
				this.status = SUCCESS;
			} else {
				this.status = FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.status = FAIL;
		}
	}

	private String parseTitle() {
		String rs = "";
		if (this.parser != null) {
			rs = this.parser.title();
			if (rs.isEmpty()) {
				boolean exit = false;
				for (String[] filter : TITLE) {
					Elements eles = this.parser.getElementsByTag(filter[0]);
					for (Element ele : eles) {
						if (ele.hasAttr(filter[1]) && ele.attr(filter[1]).equalsIgnoreCase(filter[2])) {
							rs = ele.attr(filter[3]);
							exit = true;
							break;
						}
					}
					if (exit) {
						break;
					}
				}
			}
		}

		return rs;
	}

	private String parseDescription() {
		String rs = "";
		if (this.parser != null) {
			boolean exit = false;
			for (String[] filter : DESCRIPTION) {
				Elements eles = this.parser.getElementsByTag(filter[0]);
				for (Element ele : eles) {
					if (ele.hasAttr(filter[1]) && ele.attr(filter[1]).equalsIgnoreCase(filter[2])) {
						rs = ele.attr(filter[3]);
						exit = true;
						break;
					}
				}
				if (exit) {
					break;
				}
			}
		}
		if (rs.isEmpty()) {
			rs = this.title;
		}

		return rs;
	}

	private String parseSite() {
		String rs = "";
		if (parser != null) {
			rs = parser.baseUri();
			Pattern pattern = Pattern.compile("http(s?)://(www\\.)?([^/]*).*$");
			Matcher match = pattern.matcher(rs);
			if (match.find()) {
				rs = match.group(3);
			}
		}

		return rs;
	}

	private String parseThumbnail() {
		String rs = "";
		if (this.parser != null) {
			boolean exit = false;
			for (String[] filter : THUMBNAIL) {
				Elements eles = this.parser.getElementsByTag(filter[0]);
				for (Element ele : eles) {
					if (filter[1] == null || filter[2] == null
							|| (ele.hasAttr(filter[1]) && ele.attr(filter[1]).equalsIgnoreCase(filter[2]))) {
						rs = ele.attr(filter[3]);
//						if ((this.thumbData = parseThumbnailData(rs)) != null && this.thumbData.length > 0) {
//							exit = true;
//							break;
//						}
						exit = true;
						break;
					}
				}
				if (exit) {
					break;
				}
			}
		}
		if (rs.charAt(0) == '/') {
			rs = "www."+ this.site + rs;
		}
		return rs;
	}
	
//	private byte[] parseThumbnailData(String thumbUri) {
//		List<String> uris = new ArrayList<>();
//		if (thumbUri.charAt(0) == '/') {
//			uris.add(PREFIX.concat(this.site).concat(thumbUri));
//			uris.add(PREFIXS.concat(this.site).concat(thumbUri));
//		} else {
//			uris.add(thumbUri);
//		}
//		byte[] rs = null;
//		if (this.parser != null) {
//			try {
//				for (String uri : uris) {
//					URL url = new URL(uri);
//					try {
//						InputStream is = url.openStream();
//						if (is != null) {
//							rs = IOUtils.toByteArray(is);
//							if (rs != null &&  rs.length > 0) {
//								break;
//							}
//						}
//					} catch (Exception e) {
//						System.out.println("Exception: link = " + uri);
//						e.printStackTrace();
//						continue;
//					}
//				}
//			} catch (Exception e) {
//			}
//		}
//		return rs;
//	}
	
	public static long hashLink(String link) {
		long hash = 0;
		for (int i = 0; i < link.length(); i++) {
			hash = 2 * hash + link.charAt(i);
		}
		return hash;
	}

	public String getLink() {
		return link;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
	
	public String getSite() {
		return site;
	}
	
	public String getThumbUri() {
		return thumbUri;
	}

	public byte[] getThumbData() {
		return thumbData;
	}

	public int getStatus() {
		return status;
	}

	public static void main(String[] args) throws IOException {
			LinkParser parser = new LinkParser("https://tinhte.vn");
			System.out.println(parser.getTitle());
			System.out.println(parser.getDescription());
			System.out.println(parser.getSite());
			System.out.println(parser.getThumbUri());
			System.out.println("parse done ");
	}

}
