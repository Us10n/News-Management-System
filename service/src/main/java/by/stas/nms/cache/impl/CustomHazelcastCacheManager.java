package by.stas.nms.cache.impl;

import by.stas.nms.cache.CustomCacheManager;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class CustomHazelcastCacheManager implements CustomCacheManager {
    private final HazelcastInstance hazelcastInstance;
    @Value("${hazelcast.map.ttl:10}")
    private Integer ttl = 10;

    @Autowired
    public CustomHazelcastCacheManager(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void putObjectToCacheMap(String name, String key, Object object) {
        hazelcastInstance.getMap(name).put(key, object, ttl, TimeUnit.SECONDS);
    }

    @Override
    public void putCollectionToCacheMap(String name, String key, Object[] objects) {
        hazelcastInstance.getMap(name).put(key, objects, ttl, TimeUnit.SECONDS);
    }

    @Override
    public Optional<Object> getObjectFromCacheMap(String name, String key) {
        Object extractedObject = hazelcastInstance.getMap(name).get(key);
        return Optional.ofNullable(extractedObject);
    }

    @Override
    public Object[] getCollectionFromCacheMap(String name, String key) {
        return (Object[]) hazelcastInstance.getMap(name).get(key);
    }

    @Override
    public String generateKeyForCacheMap(Object... objects) {
        long key = 0L;
        for (Object object : objects) {
            key += object.hashCode();
        }
        return String.valueOf(Long.hashCode(key));
    }

    @Override
    public void invalidateCacheMap(String cacheName) {
        hazelcastInstance.getMap(cacheName).clear();
    }
}
