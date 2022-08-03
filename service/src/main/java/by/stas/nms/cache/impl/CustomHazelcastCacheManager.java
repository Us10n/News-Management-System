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
    private Integer ttl;

    @Autowired
    public CustomHazelcastCacheManager(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void putSingleObjectToMap(String name, String key, Object object) {
        hazelcastInstance.getMap(name).put(key, object, ttl, TimeUnit.SECONDS);
    }

    @Override
    public void putCollectionToMap(String name, String key, Object[] objects) {
        hazelcastInstance.getMap(name).put(key, objects, ttl, TimeUnit.SECONDS);
    }

    @Override
    public Optional<Object> getSingleObjectFromMap(String name, String key) {
        Object extractedObject = hazelcastInstance.getMap(name).get(key);
        return Optional.ofNullable(extractedObject);
    }

    @Override
    public Object[] getCollectionFromMap(String name, String key) {
        return (Object[]) hazelcastInstance.getMap(name).get(key);
    }

    @Override
    public String generateKeyForMap(Object... objects) {
        long key = 0L;
        for (Object object : objects) {
            key += object.hashCode();
        }
        return String.valueOf(Long.hashCode(key));
    }
}
