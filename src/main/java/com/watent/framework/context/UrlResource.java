package com.watent.framework.context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Dylan
 */
public class UrlResource implements Resource {

    private URL url;

    public UrlResource(String url) throws IOException {
        this.url = new URL(url);
    }

    public UrlResource(URL url) {
        this.url = url;
    }

    @Override
    public boolean exists() {
        return null != url;
    }

    @Override
    public boolean isReadable() {
        return exists();
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (null == url) {
            return null;
        }
        return url.openStream();
    }
}
