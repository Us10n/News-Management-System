package by.stas.nms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Class {@code NewsDto} represents News entity. Implements Serializable.
 *
 * @see by.stas.nms.entity.News
 * @see Serializable
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto implements Serializable {
    private String id;
    private LocalDateTime date;
    private String title;
    private String text;
}

