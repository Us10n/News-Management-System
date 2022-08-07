package by.stas.nms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Class {@code CommentDto} represents Comment entity. Implements Serializable.
 *
 * @see by.stas.nms.entity.Comment
 * @see Serializable
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto implements Serializable {
    private String id;
    private String newsId;
    private LocalDateTime date;
    private String text;
    private String username;
}
