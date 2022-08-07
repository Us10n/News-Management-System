package by.stas.nms.mapper;

import by.stas.nms.dto.NewsDto;
import by.stas.nms.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface {@code NewsMapper} provides methods to cast News to NewsDto and vise versa.
 *
 * @see News
 * @see NewsDto
 */
@Mapper
public interface NewsMapper {

    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    /**
     * Map News to NewsDto.
     *
     * @param news the news
     * @return the news dto
     *
     * @see News
     * @see NewsDto
     */
    NewsDto mapToDto(News news);

    /**
     * Map NewsDto to News.
     *
     * @param newsDto the news dto
     * @return the news
     *
     * @see News
     * @see NewsDto
     */
    News mapToEntity(NewsDto newsDto);
}
