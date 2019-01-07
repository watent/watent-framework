package com.watent.framework.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Dylan
 */
public class FileSystemResource implements Resource {

    private File file;

    public FileSystemResource(File file) {
        this.file = file;
    }

    public FileSystemResource(String fileName) {
        this.file = new File(fileName);
    }

    @Override
    public boolean exists() {
        return null != file && file.exists();
    }

    @Override
    public boolean isReadable() {
        return null != file && file.canRead();
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }
}
