package by.stas.nms.mapper;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface {@code CommentMapper} provides methods to cast Comment to CommentDto and vise versa.
 *
 * @see Comment
 * @see CommentDto
 */
@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    /**
     * Map Comment to CommentDto.
     *
     * @param comment the comment
     * @return the comment dto
     *
     * @see Comment
     * @see CommentDto
     */
    CommentDto mapToDto(Comment comment);

    /**
     * Map CommentDto to Comment.
     *
     * @param commentDto the comment dto
     * @return the comment
     *
     * @see Comment
     * @see CommentDto
     */
    Comment mapToEntity(CommentDto commentDto);
}
