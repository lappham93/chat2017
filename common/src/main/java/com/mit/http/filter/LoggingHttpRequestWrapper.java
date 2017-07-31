package com.mit.http.filter;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by Hung Le on 2/13/17.
 */
public class LoggingHttpRequestWrapper extends HttpServletRequestWrapper {

    private byte[] cachedBytes;
    private boolean notConsumed = false;

    public LoggingHttpRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public boolean notConsumed() {
        return notConsumed;
    }

    public void notConsumed(boolean consumed) {
        notConsumed = consumed;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedBytes == null) {
            cacheInputStream();
        }
        return new CachedServletInputStream();
    }

    private void cacheInputStream() throws IOException {
        cachedBytes = IOUtils.toByteArray(super.getInputStream());
    }

    private class CachedServletInputStream extends ServletInputStream {
        private ByteArrayInputStream byteArrayInputStream;

        public CachedServletInputStream() {
            byteArrayInputStream = new ByteArrayInputStream(cachedBytes);
        }

        @Override
        public int read() {
            return byteArrayInputStream.read();
        }

        @Override
        public int available() {
            return byteArrayInputStream.available();
        }

        @Override
        public boolean isFinished() {
            return available() <= 0;
        }

        @Override
        public boolean isReady() {
            return available() >= 0;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }

        @Override
        public synchronized void reset() throws IOException {
            byteArrayInputStream.reset();
        }
    }
}
