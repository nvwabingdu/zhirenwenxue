package com.zr.videocache.sourcestorage;

import com.zr.videocache.SourceInfo;

/**
 * Storage for {@link SourceInfo}.
 */
public interface SourceInfoStorage {

    SourceInfo get(String url);

    void put(String url, SourceInfo sourceInfo);

    void release();
}
