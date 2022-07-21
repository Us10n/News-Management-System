package by.stas.nms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private long id;
    private String date;
    private String title;
    private String text;
    private List<CommentDto> comments;
}
