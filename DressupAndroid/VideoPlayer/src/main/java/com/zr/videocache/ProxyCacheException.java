package com.zr.videocache;

/**
 * Indicates any error in work of {@link ProxyCache}.
 */
public class ProxyCacheException extends Exception {

//    private static final String LIBRARY_VERSION = ". Version: " + BuildConfig.VERSION_NAME;
    private static final String LIBRARY_VERSION = ". Version: " + "007";

    public ProxyCacheException(String message) {
        super(message + LIBRARY_VERSION);
    }

    public ProxyCacheException(String message, Throwable cause) {
        super(message + LIBRARY_VERSION, cause);
    }

    public ProxyCacheException(Throwable cause) {
        super("No explanation error" + LIBRARY_VERSION, cause);
    }
}
