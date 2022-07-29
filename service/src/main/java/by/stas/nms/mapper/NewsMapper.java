package by.stas.nms.mapper;

import by.stas.nms.dto.NewsDto;
import by.stas.nms.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NewsMapper {

    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    NewsDto mapToDto(News comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    News mapToEntity(NewsDto orderDto);
}
