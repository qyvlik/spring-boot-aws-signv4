package io.github.qyvlik.signv4.web.request;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * https://www.baeldung.com/spring-reading-httpservletrequest-multiple-times
 */
public class CachedInputStream extends ServletInputStream {

    private final InputStream stream;

    public CachedInputStream(byte[] cachedBody) {
        this.stream = new ByteArrayInputStream(cachedBody);
    }

    @Override
    public boolean isFinished() {
        try {
            return stream.available() == 0;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        return stream.read();
    }
}
