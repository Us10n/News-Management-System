package by.stas.nms.cache;

import java.util.Optional;

public interface CustomCacheManager {
    void putSingleObjectToMap(String cacheName,String key, Object object);

    void putCollectionToMap(String cacheName,String key, Object[] objects);

    Optional<Object> getSingleObjectFromMap(String cacheName,String key);

    Object[] getCollectionFromMap(String cacheName,String key);

    String generateKeyForMap(Object... objects);
}
