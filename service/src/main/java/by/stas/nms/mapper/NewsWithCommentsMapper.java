package by.stas.nms.mapper;

import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {NewsMapper.class, CommentMapper.class})
public interface NewsWithCommentsMapper {

    NewsWithCommentsMapper INSTANCE = Mappers.getMapper(NewsWithCommentsMapper.class);

    NewsWithCommentsDto mapToDto(News comment);

    @Mapping(target = "id", ignore = true)
    News mapToEntity(NewsWithCommentsDto orderDto);
}
