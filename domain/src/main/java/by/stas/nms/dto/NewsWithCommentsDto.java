package by.stas.nms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewsWithCommentsDto extends NewsDto implements Serializable {
    private List<CommentDto> comments;

    public NewsWithCommentsDto(String id, LocalDateTime date, String title, String text, List<CommentDto> comments) {
        super(id, date, title, text);
        this.comments = comments;
    }

    public NewsWithCommentsDto(NewsDto newsDto) {
        super(newsDto.getId(), newsDto.getDate(), newsDto.getTitle(), newsDto.getText());
        comments = new ArrayList<>();
    }
}


