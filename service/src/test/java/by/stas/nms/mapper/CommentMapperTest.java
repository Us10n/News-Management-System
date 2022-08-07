package by.stas.nms.mapper;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.entity.Comment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@ActiveProfiles("test")
@SpringBootTest(classes = {CommentMapperImpl.class})
class CommentMapperTest {
    @Autowired
    CommentMapper commentMapper;

    @Test
    void mapToDto() {
        Comment news = new Comment("id","newsId", LocalDateTime.now(), "text", "username", 0f);
        CommentDto expected = new CommentDto("id","newsId", LocalDateTime.now(), "text", "username");
        CommentDto actual = commentMapper.mapToDto(news);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void mapToEntity() {
        CommentDto newsDto = new CommentDto("id","newsId", LocalDateTime.now(), "text", "username");
        Comment expected = new Comment("id","newsId", LocalDateTime.now(), "text", "username", null);
        Comment actual = commentMapper.mapToEntity(newsDto);
        Assertions.assertEquals(expected,actual);
    }
}