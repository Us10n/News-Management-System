package by.stas.nms.mapper;

import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface {@code NewsWithCommentsMapper} provides methods to cast NewsWithComments to NewsWithCommentsDto and vise versa.
 * Uses {@code NewsMapper} and {@code CommentMapper}
 *
 * @see NewsMapper
 * @see CommentMapper
 */
@Mapper(uses = {NewsMapper.class, CommentMapper.class})
public interface NewsWithCommentsMapper {

    NewsWithCommentsMapper INSTANCE = Mappers.getMapper(NewsWithCommentsMapper.class);

    /**
     * Map News to NewsWithCommentsDto.
     *
     * @param news the news
     * @return the news with comments dto
     *
     * @see News
     * @see NewsWithCommentsDto
     */
    NewsWithCommentsDto mapToDto(News news);

    /**
     * Map NewsWithCommentsDto to News.
     *
     * @param newsWithCommentsDto the news with comments dto
     * @return the news
     *
     * @see News
     * @see NewsWithCommentsDto
     */
    News mapToEntity(NewsWithCommentsDto newsWithCommentsDto);
}
