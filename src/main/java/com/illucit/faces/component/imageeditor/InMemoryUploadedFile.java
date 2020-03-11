package com.illucit.faces.component.imageeditor;

import java.io.*;

import org.primefaces.model.file.UploadedFile;

/**
 * {@link UploadedFile} implementation for in-memory data (small image data).
 *
 * @author Christian Simon
 */
public class InMemoryUploadedFile implements UploadedFile {

    private final byte[] data;

    private final String filename;

    private final String contentType;

    /**
     * Create instance with fixed data.
     *
     * @param data        byte data
     * @param filename    filename of the virtual file
     * @param contentType content type of the virtual file
     */
    public InMemoryUploadedFile(byte[] data, String filename, String contentType) {
        this.data = data;
        this.filename = filename;
        this.contentType = contentType;
    }

    @Override
    public String getFileName() {
        return filename;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    @Override
    public long getSize() {
        return data.length;
    }

    @Override
    public byte[] getContent() {
        return data;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void write(String fileName) throws Exception {
        File target = new File(fileName);
        try (FileOutputStream fileOut = new FileOutputStream(target)) {
            fileOut.write(data);
        }
    }

}
