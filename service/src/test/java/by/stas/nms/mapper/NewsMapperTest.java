package by.stas.nms.mapper;

import by.stas.nms.dto.NewsDto;
import by.stas.nms.entity.News;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@ActiveProfiles("test")
@SpringBootTest(classes = {NewsMapperImpl.class})
class NewsMapperTest {
    @Autowired
    NewsMapper newsMapper;

    @Test
    void mapToDto() {
        News news = new News("id", LocalDateTime.now(), "title", "text", 0f);
        NewsDto expected = new NewsDto("id", LocalDateTime.now(), "title", "text");
        NewsDto actual = newsMapper.mapToDto(news);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void mapToEntity() {
        NewsDto newsDto = new NewsDto("id", LocalDateTime.now(), "title", "text");
        News expected = new News("id", LocalDateTime.now(), "title", "text", null);
        News actual = newsMapper.mapToEntity(newsDto);
        Assertions.assertEquals(expected,actual);
    }
}