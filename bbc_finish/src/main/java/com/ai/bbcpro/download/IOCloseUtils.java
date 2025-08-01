package com.ai.bbcpro.download;

import java.io.Closeable;
import java.io.IOException;

public class IOCloseUtils {

    public static final void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            synchronized (IOCloseUtils.class) {
                closeable.close();
            }
        }
    }
}
