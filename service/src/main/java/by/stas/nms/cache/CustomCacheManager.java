package by.stas.nms.cache;

import java.util.Optional;

/**
 * The interface {@code CustomCacheManager} provides custom methods to cache.
 *
 * @see by.stas.nms.cache.impl.CustomHazelcastCacheManager
 */
public interface CustomCacheManager {
    /**
     * Put object to cache map.
     *
     * @param cacheName the cache name
     * @param key       the key
     * @param object    the object to cache
     */
    void putObjectToCacheMap(String cacheName, String key, Object object);

    /**
     * Put collection to cache map.
     *
     * @param cacheName the cache name
     * @param key       the key
     * @param objects   the objects to cache
     */
    void putCollectionToCacheMap(String cacheName, String key, Object[] objects);

    /**
     * Gets object from cache map.
     *
     * @param cacheName the cache name
     * @param key       the key
     * @return the object from cache map
     */
    Optional<Object> getObjectFromCacheMap(String cacheName, String key);

    /**
     * Get collection from cache map object [ ].
     *
     * @param cacheName the cache name
     * @param key       the key
     * @return the object objects from cache map
     */
    Object[] getCollectionFromCacheMap(String cacheName, String key);

    /**
     * Generate key string using passed objects for cache map.
     *
     * @param objects the objects to generate key from
     * @return the key string
     */
    String generateKeyForCacheMap(Object... objects);

    /**
     * Invalidate cache map.
     *
     * @param cacheName the cache name
     */
    void invalidateCacheMap(String cacheName);
}
