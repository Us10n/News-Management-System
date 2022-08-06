package by.stas.nms.renovator;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.renovator.Renovator;
import by.stas.nms.renovator.impl.CommentDtoRenovator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@ActiveProfiles("test")
class CommentDtoRenovatorTest {

    Renovator<CommentDto> commentDtoRenovator = new CommentDtoRenovator();

    @Test
    void updateObject() {
        CommentDto expected = new CommentDto(null, "newsId", LocalDateTime.now(), "text", "username");
        CommentDto actual = new CommentDto();
        commentDtoRenovator.updateObject(actual, expected);
        Assertions.assertEquals(expected, actual);
    }
}