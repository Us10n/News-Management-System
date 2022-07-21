package by.stas.nms.mapper;

import by.stas.nms.dto.NewsDto;
import by.stas.nms.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CommentMapper.class)
public interface NewsMapper {

    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    NewsDto mapToDto(News comment);

    News mapToEntity(NewsDto orderDto);
}
