package by.stas.nms.mapper;

import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.entity.News;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;

@ActiveProfiles("test")
@SpringBootTest(classes = {NewsWithCommentsMapperImpl.class})
class NewsWithCommentsMapperTest {
    @Autowired
    NewsWithCommentsMapper newsWithCommentsMapper;

    @Test
    void mapToDto() {
        News news = new News("id", LocalDateTime.now(), "title", "text", 0f);
        NewsWithCommentsDto expected = new NewsWithCommentsDto("id", LocalDateTime.now(), "title", "text", null);
        NewsWithCommentsDto actual = newsWithCommentsMapper.mapToDto(news);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void mapToEntity() {
        NewsWithCommentsDto newsDto = new NewsWithCommentsDto("id", LocalDateTime.now(), "title", "text", new ArrayList<>());
        News expected = new News("id", LocalDateTime.now(), "title", "text", null);
        News actual = newsWithCommentsMapper.mapToEntity(newsDto);
        Assertions.assertEquals(expected, actual);
    }
}