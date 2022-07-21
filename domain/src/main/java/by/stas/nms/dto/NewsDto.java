package by.stas.nms.dto;

import by.stas.nms.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private long id;
    private LocalDateTime date;
    private String title;
    private String text;
    private List<Comment> tags;
}
