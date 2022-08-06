package by.stas.nms.cache;

import by.stas.nms.cache.impl.CustomHazelcastCacheManager;
import by.stas.nms.dto.CommentDto;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
class CustomHazelcastCacheManagerTest {

    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    CustomCacheManager customCacheManager = new CustomHazelcastCacheManager(hazelcastInstance);

    private static final String CACHE_NAME = "cache";
    private static final String KEY = "key";

    @Test
    void putObjectToCacheMap() {
        CommentDto expected = new CommentDto("id", "newsId", LocalDateTime.now(), "text", "username");
        customCacheManager.putObjectToCacheMap(CACHE_NAME, KEY, expected);
        CommentDto actual = (CommentDto) hazelcastInstance.getMap(CACHE_NAME).get(KEY);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void putCollectionToCacheMap() {
        List<CommentDto> expected = List.of(new CommentDto("id", "newsId", LocalDateTime.now(), "text", "username"));
        customCacheManager.putObjectToCacheMap(CACHE_NAME, KEY, expected.toArray());
        Object[] cachedValues = (Object[]) hazelcastInstance.getMap(CACHE_NAME).get(KEY);
        List<CommentDto> actual;
        if (Objects.nonNull(cachedValues)) {
            actual = Arrays.stream(cachedValues)
                    .map(object -> (CommentDto) object)
                    .toList();
        } else {
            actual = new ArrayList<>();
        }
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getObjectFromCacheMap() {
        CommentDto expected = new CommentDto("id", "newsId", LocalDateTime.now(), "text", "username");
        hazelcastInstance.getMap(CACHE_NAME).put(KEY, expected);
        CommentDto actual = (CommentDto) customCacheManager.getObjectFromCacheMap(CACHE_NAME, KEY).orElse(new CommentDto());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getCollectionFromCacheMap() {
        List<CommentDto> expected = List.of(new CommentDto("id", "newsId", LocalDateTime.now(), "text", "username"));
        hazelcastInstance.getMap(CACHE_NAME).put(KEY, expected.toArray());
        Object[] cachedValues = customCacheManager.getCollectionFromCacheMap(CACHE_NAME, KEY);
        List<CommentDto> actual;
        if (Objects.nonNull(cachedValues)) {
            actual = Arrays.stream(cachedValues)
                    .map(object -> (CommentDto) object)
                    .toList();
        } else {
            actual = new ArrayList<>();
        }
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void invalidateCacheMap() {
        CommentDto object = new CommentDto("id", "newsId", LocalDateTime.now(), "text", "username");
        hazelcastInstance.getMap(CACHE_NAME).put(KEY, object);
        customCacheManager.invalidateCacheMap(CACHE_NAME);
        Assertions.assertNull(hazelcastInstance.getMap(CACHE_NAME).get(KEY));
    }
}