package by.stas.nms.cache;

import java.util.Optional;

public interface CustomCacheManager {
    void putObjectToCacheMap(String cacheName, String key, Object object);

    void putCollectionToCacheMap(String cacheName, String key, Object[] objects);

    Optional<Object> getObjectFromCacheMap(String cacheName, String key);

    Object[] getCollectionFromCacheMap(String cacheName, String key);

    String generateKeyForCacheMap(Object... objects);

    void invalidateCacheMap(String cacheName);
}
