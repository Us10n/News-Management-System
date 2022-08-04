package by.stas.nms.mapper;

import by.stas.nms.dto.NewsDto;
import by.stas.nms.entity.News;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {NewsMapperImpl.class})
class NewsMapperTest {
    @Autowired
    NewsMapper newsMapper;

    LocalDateTime currentTime = LocalDateTime.now();
    List<News> newsList = List.of(
            new News("1", currentTime,"title1","text1",0f)
    );
    List<NewsDto> newsDtoLis;

    @BeforeAll
    void setUp() {

    }

    @Test
    void mapToDto() {
    }

    @Test
    void mapToEntity() {
    }
}