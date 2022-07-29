package by.stas.nms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.*;

import java.time.LocalDateTime;

@Document(collection = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @Field("id_news")
    private String newsId;

    @TextIndexed(weight = 1)
    @Field(targetType = FieldType.DATE_TIME)
    private LocalDateTime date;

    @TextIndexed(weight = 2)
    private String text;

    @TextIndexed(weight = 3)
    private String username;

    @TextScore
    private Float score;
}
