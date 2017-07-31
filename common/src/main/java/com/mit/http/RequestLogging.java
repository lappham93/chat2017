package com.mit.http;

import com.mit.utils.ByteUtils;
import com.mit.utils.IpUtils;
import com.mit.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hung Le on 2/13/17.
 */
public class RequestLogging {
    private String time = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME );
    private String ip;
    private String uri;
    private String method;
    private String apiKey;
    private Map<String, String> params = new HashMap<>();
    private String body;
    private int bodyLength;
    private int status;
    private String response;
    private int spendTime;

    public RequestLogging() {
    }

    public RequestLogging(HttpServletRequest request) throws IOException {
        this(request, false);

    }

    public RequestLogging(HttpServletRequest request, boolean isUploadFile) throws IOException {
        ip = IpUtils.getIpAddress(request);
        uri = request.getRequestURI();
        method = request.getMethod();
        apiKey = request.getHeader("Api-Key");
        Enumeration<String> paramsName = request.getParameterNames();
        while(paramsName.hasMoreElements()) {
            String name = paramsName.nextElement();
            params.put(name, request.getParameter(name));
        }

        if (!isUploadFile) {
            InputStream stream = request.getInputStream();
            byte[] data = ByteUtils.readInputStream(stream);
            body = new String(data);
            bodyLength = body.length();
            stream.reset();
        }
    }

    public RequestLogging(String ip, String method, String apiKey) {
        this.ip = ip;
        this.method = method;
        this.apiKey = apiKey;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(int spendTime) {
        this.spendTime = spendTime;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
