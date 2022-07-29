package by.stas.nms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.*;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @TextIndexed(weight = 1)
    @Field(targetType = FieldType.DATE_TIME)
    private LocalDateTime date;

    @TextIndexed(weight = 4)
    private String title;

    @TextIndexed(weight = 2)
    private String text;

    @TextScore
    private Float score;
}
